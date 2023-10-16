package com.example.androidpractice.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class MyView : View {
    enum class ShapeType {
        RECTANGLE, CIRCLE
    }

    var currentShapeType = ShapeType.RECTANGLE

    private var rect = Rect(10, 10, 110, 110)


    private var color = Color.BLUE
    private var paint = Paint()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        when (currentShapeType) {
            ShapeType.RECTANGLE -> canvas.drawRect(rect, paint)
            ShapeType.CIRCLE -> canvas.drawCircle(rect.left + 50f, rect.top + 50f, 50f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            rect.left = event.x.toInt() - 50
            rect.top = event.y.toInt() - 50
            //클릭 한 현재 위치로 변경
            rect.right = rect.left + 100
            rect.bottom = rect.top + 100
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

}