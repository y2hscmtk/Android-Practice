package com.example.androidpractice.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.transition.Fade
import androidx.transition.Scene
import androidx.transition.TransitionManager
import com.example.androidpractice.R


/* <Animation> - 고급 안드로이드 프로그래밍 Week3
* 애니메이션을 그리는 방법
* - Drawable animation : drawable 여러 장으로 만드는 애니메이션
* : 애니메이션에 포함도리 이미지들의 리스트를 갖는 리소스를 생성 :
* : - res/moving-circle.xml
* <animation-list xmlns:android="http://schemas,android.com/apk/res/android" android:oneshot="true">
*    <item android:drawable="@drawable/frame0" android:duration="200" />
*    ..
*    <item android:drawable="@drawable/frame0" android:duration="300" />
* </animation-list> //시간대별로 화면에 보여줄 drawble을 결정
* : ImageView의 배경으로 위 리소스를 지정하고 애니메이션 start()
* imageView.setBackgroundResource(R.drawable.moving_circle)
* val animation = imageView.background as AnimationDrawable
* animation.stop() //첫 프레임부터 시작하도록 하기 위해서 stop()먼저 호출
* animation.start() //애니메이션 시작

* - Property : 객체의 속성을 시간에 따라 변경하도록 만들어서 애니메이션 효과 적용
* : view의 위치를 시간에 따라 5초 동안 변경시키는것이 예시에 해당
* : Duration(애니메이션 시간, 기본은 300ms),
* Time Interpolation(시간에 따른 애니메이션 값 계산 방법),
* 반복 여부, 되감기 여부 등,
* Animator Set(보여줄 애니메이션의 집합),
* 프레임 간격(얼마나 자주 값을 업데이트 할 것인지,기본은 10ms)
* : 관련 클래스
* : - ValueAnimator - 애니메이션에서 사용할 수 있는 값을 계산 => 값을 직접 사용하여 애니메이션을 만들어야 함
* : - ObjectAnimator - 객체의 특정 속성 값을 애니메이터가 직접 변경
* : - AnimatorSet - 2개 이상의 애니메이터를 동시에, 순서대로 사용하기 위한 방법 => xml로 연달아 사용할 애니메이션을 적용해서도 가능
* : - Evaluators - 값들의 타입에 따라 Interpolator가 계산한 비율값을 기반으로 계산
* : - Interpolators : 시간에 따른 0과 1 사이의 비율 값을 계산
*
* - Transition
* : 시작과 끝 레이아웃을 지정하면 자동으로 애니메이션을 만들어 줌
* : 펲이드 효과 애니메이션, 뷰의 위치 변경 애니메이션등 모두 가능
* : 필요한 요소 - 시작과 끝 Scene, Scene이 바뀌고 Transition이 일어날 root view(container)
* : 사용 - 생성한 Scene을 인자로 하여 TransitionManager.go(scene, transition)호출, transition은 전환 애니메이션 종류 지정
* : FrameLayout을 사용하여 레이아웃을 갈아끼우는 형태의 애니메이션
* : FrameLayout(id/scene_root) - Scene_1 - Scene_2
* : val sceneRoot = findViewById<FrameLayout>(R.id.scene_root)
*  //시작 씬 : sceneRoot, 끝 씬 : R.layout.scene_1,transition이 일어날 root view
* : scene1 = Scene.getScenForLayout(sceneRoot, R.layout.scene_1,this)
* : scene2 = Scene.getScenForLayout(sceneRoot, R.layout.scene_2,this)
* : TransitionManager.go(scene1, ChangeBound()) //씬1의 애니메이션 수행 => 루트에서 씬1로 전환하는 애니메이션
* : TransitionManager.go(scene2. Fade())
*
* - MotionLayout
* : Motion과 위젯 애니메이션을 다루기 쉽게 해주는 레이아웃
* : ContraintLayout 라이브러리의 일부로 포함되어 있음
* : 모션 레이아웃 정의 XML과 모션 씬을 정의하는 XML로 구성됨
* : 사용자가 위젯을 클릭하거나 화면을 드래그하여 레이아웃 변화나 위젯 애니메이션을 작동 시킬 수 있음
*
* */


//데이터를 저장하기 위한 데이터 => 화면 전환 이후에도 값을 유지 하기 위해
class AnimationActivityViewModel : ViewModel() {
    var studentTextName: String = ""
    var studentTextAddr: String = ""
    var staffTextName : String = ""
    var staffTextAddr: String = ""
    //씬1에서 선택한 라디오 버튼 번호 값 저장
    var selectedRadioButtonId: Int = -1
}

class AnimationActivity : AppCompatActivity() {

    private val viewModel: AnimationActivityViewModel by viewModels()

    private lateinit var scene1: Scene
    private lateinit var scene2: Scene

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)

        // Transition을 수행할 root view
        val sceneRoot = findViewById<FrameLayout>(R.id.scene_root)
        // Transition에 필요한 정보
        // 시작 씬 : sceneRoot, 끝 씬 : R.layout.layout_student&staff ,transition이 일어날 root view(activity_animation)
        scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.layout_student,this)
        scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.layout_staff,this)

        val radioGroup1 = findViewById<RadioGroup>(R.id.radioGroup1)
        radioGroup1.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                //Staff(씬2) -> Student(씬1)로 이동
                R.id.radioStudent -> {
                    saveTextFieldsStaff() //Student 씬으로 이동하기 전에 현재 Staff값 저장
                    TransitionManager.go(scene1, Fade()) //Transition 수행 => scene1에 대한~
                    setUpListners(sceneRoot) //numberGroup에 대한 리스너 다시 생성
                    updateTextFieldsStudent() //기존 Student값으로 업데이트
                }
                //Student(씬1) -> Staff(씬2)로 이동할때 호출
                R.id.radioStaff -> {
                    //이동하기전 현재 Student의 값 저장
                    saveTextFieldsStudent()
                    TransitionManager.go(scene2, Fade()) //Transition 수행 => scene2에 대한~
                    //이동후 기존 Staff필드 값으로 업데이트
                    updateTextFieldsStaff()
                }
            }
        }
    }

    private fun setUpListners(sceneRoot: FrameLayout){
        Log.d("확인","성공")
        //라디오 그룹 가져오기


        //사용자가 선택한 라디오버튼의 번호 저장
        val radioGroupNumber = findViewById<RadioGroup>(R.id.radioNumber)
        radioGroupNumber.setOnCheckedChangeListener { _, checkedId ->
            viewModel.selectedRadioButtonId = checkedId  // 저장: 현재 선택된 라디오 버튼의 ID
        }
    }

    //Staff필드의 데이터 저장
    private fun saveTextFieldsStaff() {
        viewModel.staffTextName = findViewById<EditText>(R.id.editTextName).text.toString()
        viewModel.staffTextAddr = findViewById<EditText>(R.id.editTextAddr).text.toString()
    }

    //Student필드의 데이터 저장
    private fun saveTextFieldsStudent(){
        viewModel.studentTextName = findViewById<EditText>(R.id.editTextName).text.toString()
        viewModel.studentTextAddr = findViewById<EditText>(R.id.editTextAddr).text.toString()
        val radioGroupNumber = findViewById<RadioGroup>(R.id.radioNumber)
        viewModel.selectedRadioButtonId = radioGroupNumber.checkedRadioButtonId
    }

    //Student필드로 되돌아왔을때 데이터 업데이트
    private fun updateTextFieldsStudent() {
        findViewById<EditText>(R.id.editTextAddr).setText(viewModel.studentTextAddr)
        findViewById<EditText>(R.id.editTextName).setText(viewModel.studentTextName)
        //사용자가 선택한 번호로 업데이트
        val radioGroupNumber = findViewById<RadioGroup>(R.id.radioNumber)
        if (viewModel.selectedRadioButtonId != -1) {
            radioGroupNumber.check(viewModel.selectedRadioButtonId)  // 복원: 이전에 선택된 라디오 버튼의 선택 상태
        } else {
            radioGroupNumber.clearCheck()
        }
    }

    //Staff필드로 되돌아왔을때 데이터 업데이트
    private fun updateTextFieldsStaff() {
        findViewById<EditText>(R.id.editTextName).setText(viewModel.staffTextName)
        findViewById<EditText>(R.id.editTextAddr).setText(viewModel.staffTextAddr)
    }


}