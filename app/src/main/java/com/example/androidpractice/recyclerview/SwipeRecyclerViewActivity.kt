package com.example.androidpractice.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R


//Swipe를 통해, 리사이클러뷰 아이템의 목록을 제거하는 예제
class SwipeRecyclerViewActivity : AppCompatActivity() {

    lateinit var recyclerview : RecyclerView // 리사이클러뷰에 대한 참조

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_recycler_view)

        //리사이클러뷰에 대한 레이아웃 받아오기
        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.adapter = SwipeListAdapter() //어댑터 부착
        recyclerview.layoutManager = LinearLayoutManager(applicationContext)

        //ItemTouchHelper.Callback을 리사이클러뷰와 연결한다.
        val swipeHelper = SwipeHelper() // ItemTouchHelper.Callback 구현 클래스
        val itemTouchHelper = ItemTouchHelper(swipeHelper) //ItemTouchHelper클래스에 Callback클래스를 넘겨준다.
        // => itemTouchHelper가 ItemTouchHelper.CallBack과 recyclerview간의 중계 역할을 수행한다.
        itemTouchHelper.attachToRecyclerView(recyclerview) //itemTouchHelper에 리사이클러뷰 붙이기 => 아이템터치핼퍼에 연결
        //리사이클러뷰 - 아이템 터치 핼퍼 - 아이템터치핼퍼 콜백

    }



}