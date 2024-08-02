package com.example.androidsdktasks

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters


class ChargeNotificationWorker(context: Context, workerParameters: WorkerParameters)
    : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val notificationManager = applicationContext.getSystemService<NotificationManager>()
        if (notificationManager?.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        if (inputData.getBoolean(IS_NOTIFICATION_CANCELED, true)) {
            notificationManager?.cancel(NOTIFICATION_ID)
        } else {
            val notification = Notification.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Device is charging")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true)
                .build()
            notificationManager?.notify(NOTIFICATION_ID, notification)
        }
        return Result.success()
    }


    companion object {
        const val IS_NOTIFICATION_CANCELED = "is_notification_canceled"

        private const val NOTIFICATION_CHANNEL_ID = "com.example.androidsdktasks.charge_notification_channel"
        private const val NOTIFICATION_CHANNEL_NAME = "device charging notification"
        private const val NOTIFICATION_ID = 115
    }
}