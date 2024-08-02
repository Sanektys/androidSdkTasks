package com.example.androidsdktasks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random
import kotlin.random.nextInt


class CustomView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null)
    : View(context, attributeSet) {

    private val paint = Paint()
    private var progress: Int = 1
    private var filledWidth: Float = 0f

    init {
        changeColor()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        calculateFilledWidth()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(
            0f,
            0f,
            filledWidth,
            height.toFloat(),
            paint
        )
    }

    override fun performClick(): Boolean {
        changeProgress()
        calculateFilledWidth()
        changeColor()
        invalidate()

        return super.performClick()
    }

    private fun changeProgress() {
        ++progress
        if (progress > NUMBER_OF_DIVISIONS) {
            progress = 1
        }
    }

    private fun calculateFilledWidth() {
        filledWidth = width.toFloat() / NUMBER_OF_DIVISIONS * progress
    }

    private fun changeColor() {
        paint.color = Color.rgb(
            Random.nextInt(0..255),
            Random.nextInt(0..255),
            Random.nextInt(0..255)
        )
    }


    companion object {
        private const val NUMBER_OF_DIVISIONS = 10
    }
}