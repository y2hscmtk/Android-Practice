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

    //화면에 그려줄 모양
    private var currentShapeType = ShapeType.RECTANGLE

    //사각형이 최초에 그려질 위치
    private var shape = Rect(10, 10, 110, 110)


    private var color = Color.BLUE
    private var paint = Paint()

    constructor(context: Context?) : super(context) //Activity에서 setContent시에 주입
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) //레이아웃에서 view tag를 사용하여 뷰를 그릴 때 필요

    //모양 설정
    public fun changeShape(shape:ShapeType){
        this.currentShapeType = shape
    }


    //시스템이 화면을 그려줄 때 호출
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.setBackgroundColor(Color.YELLOW)
        paint.color = color
        //사용자가 선택한 모양 정보에 맞춰서 다른 모양을 그려주기 위함
        when (currentShapeType) {
            ShapeType.RECTANGLE -> canvas.drawRect(shape, paint)
            ShapeType.CIRCLE -> canvas.drawCircle(shape.left + 50f, shape.top + 50f, 50f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            shape.left = event.x.toInt() - 50
            shape.top = event.y.toInt() - 50
            //클릭 한 현재 위치로 변경
            shape.right = shape.left + 100
            shape.bottom = shape.top + 100
            invalidate() //invalidate()가 호출되면, 시스템이 onDraw()를 호출함
            return true //true를 리턴하지 않으면 Parent의 다른 이벤트를 호출 할 수 있음
        }
        return super.onTouchEvent(event)
    }

}