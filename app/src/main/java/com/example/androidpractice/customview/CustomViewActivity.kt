package com.example.androidpractice.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import com.example.androidpractice.R


/* <CustomView> - 고급 안드로이드 프로그래밍 Week1
* 안드로이드에서 제공하지 않는 UI요소, 애니메이션 등을 개발할때 사용
* View를 상속하여 외형을 그리고,UI이벤트를 처리함
* 1. View 클래스 함수
* - onDraw() : 시스템이 호출하여 뷰의 외형을 그림
* - onTouchEvent() : UI 이벤트 처리
* 2. View를 그리는 방법
* - 레이아웃에서 그리기(xml-view tag)
* : View를 상속받은 커스텀뷰에서 AttributeSet에 대한 생성자 필요
* - contentView
* : setContentView(MyCanvas(this)) // 커스텀 뷰 객체를 직접 생성하여 지정 => context를 매개변수로 넘겨야함
* - Invalidate()
* : View를 다시 그리고 싶을때 호출 자바에서 repaint()에 해당
* : 예를 들어 터치할 때마다 invalidate()를 호출하여 뷰의 위치가 움직이는 것 처럼 보여줄 수 있다.
* 3. TouchE vent()
* : View의 onTouchEvent()를 Override하여 뷰에 대한 터치 이벤트 처리 가능
* : 이벤트 처리 후 반드시 true를 리턴해야함 => 리턴하지 않으면 터치 이벤트가 제대로 수행 되지 않을 수 있음
* : view에 직접 setOnClickListener를 등록하더라도, view의 onTouchEvent에서 true를 리턴한다면 동작하지 않음(터치 이벤트가 종료되므로)
* : view에 직접 터치이벤트를 적용시키고 싶다면?
* : - onTouchEvent에서 true를 리턴하지 않도록 한다.
* : - performClick()을 호출한다.
*
* 화면을 클릭하면 사각형의 위치를 동적으로 변환하는(다시 그려주는) 예제
* */




/* <Used Class>
* CustomViewActivity.kt : Container about CustomView
* MyView.kt : CutomView
*
* <Used layout>
* activity_custom_view.xml : using 'view' tag
* */


class CustomViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view)

        val myView = findViewById<MyView>(R.id.view) //xml로부터 id가 view인 MyView인 참조 얻어오기
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        //사용자가 선택한 모양에 따라 모양 변경
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.radioRect -> myView.changeShape(ShapeType.RECTANGLE)
                R.id.radioCircle -> myView.changeShape(ShapeType.CIRCLE)
            }
            //모양을 변경했으니, 다시 그려달라는 요청을 해야함 invalidate()
            myView.invalidate() //다시 그려주기 => 변경된 모양으로
        }
    }
}