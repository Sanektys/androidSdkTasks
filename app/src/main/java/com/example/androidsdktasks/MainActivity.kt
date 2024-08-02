package com.example.androidsdktasks

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var router: Router

    private var isNotificationWorkStarted: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        savedInstanceState?.let {
            isNotificationWorkStarted = it.getBoolean(IS_NOTIFICATION_WORK_STARTED, false)
        }


        router = Router(supportFragmentManager, R.id.fragmentHost)
        router.openNextFragment(SimpleFragment(), "initialCommit")

        startChargingNotificationWork()
    }

    private fun startChargingNotificationWork() {
        if (isNotificationWorkStarted) return

        val pushNotificationRequest = PeriodicWorkRequestBuilder<ChargeNotificationWorker>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MILLISECONDS
        ).setConstraints(Constraints.Builder().setRequiresCharging(true).build())
            .setInputData(
                workDataOf(ChargeNotificationWorker.IS_NOTIFICATION_CANCELED to false)
            )
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()
        val cancelNotificationRequest = PeriodicWorkRequestBuilder<ChargeNotificationWorker>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MILLISECONDS
        ).setConstraints(Constraints.Builder().setRequiresCharging(false).build())
            .setInputData(
                workDataOf(ChargeNotificationWorker.IS_NOTIFICATION_CANCELED to true)
            )
            .build()

        val workManager = WorkManager.getInstance(this)

        workManager.enqueueUniquePeriodicWork(
            PUSH_NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            pushNotificationRequest
        )
        workManager.enqueueUniquePeriodicWork(
            CANCEL_NOTIFICATION_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            cancelNotificationRequest
        )

        isNotificationWorkStarted = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_NOTIFICATION_WORK_STARTED, isNotificationWorkStarted)
    }


    companion object {
        private const val PUSH_NOTIFICATION_WORK = "push_notification_work"
        private const val CANCEL_NOTIFICATION_WORK = "cancel_notification_work"

        private const val IS_NOTIFICATION_WORK_STARTED = "is_notification_started"
    }
}