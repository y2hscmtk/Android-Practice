package com.example.androidpractice.recyclerview.swipeRecyclerview2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androidpractice.R

// 목표 :  리사이클러뷰에서 스와이프 할 경우, 추가 메뉴를 보여주자.
// 참고영상 https://www.youtube.com/watch?v=1s4bMAyK7oM

// 필요한 의존성 : implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
class SwipeRecyclerViewWithMenuActivity : AppCompatActivity() {

    // === Layout ===
    private val swipeRefreshLayout : SwipeRefreshLayout by lazy {
        findViewById(R.id.swipeRefreshLayout)
    }

    private val recyclerview : RecyclerView by lazy {
        findViewById(R.id.recyclerview)
    }

    private val adapter = SwipeRecyclerView2Adapter()
    private val layoutManager = LinearLayoutManager(this)


    // === Life Cycle ===
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_recycler_view_with_menu)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false

            val list = mutableListOf<Int>()
            for (i in 0 until  50){
                list.add(i)
            }

            adapter.reload(list)

        }


        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter

        val list = mutableListOf<Int>()
        for (i in 0 until 50){
            list.add(i)
        }

        adapter.reload(list)

        setItemTouchHelper()
    }

    private fun setItemTouchHelper(){

    }
}