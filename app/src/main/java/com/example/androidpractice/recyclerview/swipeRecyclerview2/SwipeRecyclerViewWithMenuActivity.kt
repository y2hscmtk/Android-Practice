package com.example.androidpractice.recyclerview.swipeRecyclerview2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidpractice.R

// 목표 :  리사이클러뷰에서 스와이프 할 경우, 추가 메뉴를 보여주자.
// 참고영상 https://www.youtube.com/watch?v=1s4bMAyK7oM

// 필요한 의존성 : implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
class SwipeRecyclerViewWithMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_recycler_view_with_menu)
    }
}