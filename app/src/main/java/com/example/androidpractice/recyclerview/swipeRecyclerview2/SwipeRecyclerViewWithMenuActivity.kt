package com.example.androidpractice.recyclerview.swipeRecyclerview2

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
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

    // 리사이클러뷰에 적용할 스와이프 이벤트 정의
    private fun setItemTouchHelper(){
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            // the limit of swipe, same as the delte button in item 120dp
            private val limitScrollX = dipToPx(120f,this@SwipeRecyclerViewWithMenuActivity)
            private var currentScrollX = 0
            private var currentScrollXWhenInActive = 0
            private var initXWhenInActive = 0f
            private var firstInActive = false

            // 스와이프 기능 활성화, 드래그는 비활성화
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags,swipeFlags)
            }

            // 항목이 이동될 경우에 대한 동작 (위,아래 이동)
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            // 항목이 스와이프(좌,우) 될 경우에 대한 동작
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

            // 항목이 '스와이프 되었다'고 판단되는 임계값(threshold) 정의
            // 예를 들어 0.5라면, 사용자가 절반의 너비만큼 스와이프해야 항목이 완전히 스와이프 된 것으로 간주됨
            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }


            // ItemTouchHelper.Callback Method
            // RecyclerView의 항목이 사용자에 의해 드래그되거나 스와이프 될때 그려지는 방식을 제어함
            // 사용자가 스와이프를 계속 하는 동안 onChildDraw()가 계속 호출되면서 업데이트됨 => isCurrentlyActive(true)
            // isCurruentlyActive는 항목이 엑션을 소유하고 있다는 의미로 해석 가능할듯, 활성 상태에서는 firstInActive의 값이 변경되지 않음
            // 즉, firstInActive의 값이 변경되었다는 것은 isCurrentlyInActive 의 상태가 변경되었다는 것을 의미하고
            // 드래그 엑션이 한번 수행된 이후, 종료되었다는 것을 의미 => 스와이프가 끝났다
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float, // 사용자가 얼마나 스와이프 했는지(거리)
                dY: Float,
                actionState: Int, // 현재 엑션 상태를 정의 => 스와이프 중인지, 드래그 중인지
                isCurrentlyActive: Boolean // 활성화 상태인지(스크롤 또는 드래그 중인지)파악
            ) {

                // 현재 엑션이 스와이프인지 확인(드래그인 경우에는 동작하지 않음)
                // 스와이프 상태인 경우에 한해 아래 동작 수행
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    // 스와이프가 시작될 때 한 번만 실행됨
                    // 1. 스와이프 시작(손가락이 화면에 닿았음)
                    // 사용자가 항목을 선택하고 손을 댔을때 dx의 시점이 0이라면(리셋이후 스와이프 엑션을 시작한다면)
                    // firstInActive를 true로 설정해둠 => 스와이프 동작이 시작되었음을 의미함
                    // => 스와이프 동작 중에 dX 값이 0으로 재설정될 때마다 실행됨, 즉 사용자가 항목을 원래 위치로 되돌렸을 때도 발생
                    if(dX == 0f){
                        currentScrollX = viewHolder.itemView.scrollX // 현재 뷰의 스크롤 위치 저장
                        firstInActive = true // 스와이프 활성상태 추적을 위해
                    }

                    // 스와이프 중인 경우(손가락이 화면에 닿아있는 상태)
                    if(isCurrentlyActive){
                        var scrollOffset = currentScrollX + (-dX).toInt() // 항목이 얼마나 스크롤 되어야 할지 계산

                        // 항목이 지정된 범위(limitScrollX : 스와이프 메뉴의 크기)를 넘어서서 스와이프 되지 않도록 조정
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX
                        } else if (scrollOffset < 0){
                            scrollOffset = 0
                        }

                        // 스크롤 되어야하는 만큼만 스크롤해서 위치 조정
                        viewHolder.itemView.scrollTo(scrollOffset,0)
                    }
                    else { // 스와이프가 비활성화 된 경우(사용자가 손을 뗀 상태)
                        // 사용자가 손을 땠을 때가 스와이프 이후에 손을 뗀 것인지 여부는 firstInActive를 통해 판단
                        // firstInActive 가 true라는 것은, onChildeDraw가 스와이프 엑션에 의해 호출된 적이 있다는 것을 의미
                        // 따라서 firstInActive 는 스와이프 동작이 비활성화되고 처음으로 onChildDraw가 호출되는 순간에 활성화됨

                        // swipe with auto animation
                        if (firstInActive){
                            firstInActive = false // 비활성화
                            // 사용자가 스와이프를 멈추고 손을 뗀 순간의 뷰의 스크롤 위치와 스와이프 양(dX)을 저장
                            currentScrollXWhenInActive = viewHolder.itemView.scrollX // 사용자가 스와이프를 뗀 순간의 스크롤 위치
                            initXWhenInActive = dX // 사용자가 손을 덴 순간의 스와이프 양을 저장 => 얼마나 스와이프 했는지 판단하기 위함
                        }

                        // 사용자가 손을 뗀 이후, 뷰가 스와이프 제한(limitScrollX)에 도달하지 않았다면 초기 상태로 서서히 되돌아감
                        if (viewHolder.itemView.scrollX < limitScrollX){
                            // 사용자가 손을 뗀 순간부터 항목이 어떻게 스크롤되어야 할지를 결정함.
                            // 스와이프 제한에 도달하지 않았다면, 계산된 비율에 따라 항목을 천천히 원래 위치로 되돌림
                            viewHolder.itemView.scrollTo((currentScrollXWhenInActive * dX/initXWhenInActive).toInt(),0)
                        }
                    }
                }
            }

            // 사용자의 스와이프 동작이 완료된 이후, 사용자의 상호작용이 끝난 이후 호출됨
            // RecyclerView 항목의 스크롤 위치를 조정하여, 뷰가 정상적인 범위 내에서 표시되도록 보장
            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                // 항목 뷰의 스크롤 위치가 스와이프 제한을 초과한 경우
                if(viewHolder.itemView.scrollX > limitScrollX){
                    // 뷰를 스와이프 제한 위치로 스크롤 => 스와이프 메뉴(삭제 버튼)영역을 넘어서지 않도록
                    viewHolder.itemView.scrollTo(limitScrollX,0)
                }
                // 항목 뷰의 스크롤 위치가 음수인 경우 (왼쪽으로 너무 많이 스크롤된 경우)
                else if(viewHolder.itemView.scrollX < 0){
                    viewHolder.itemView.scrollTo(0,0)
                }
            }

        }).apply {
            attachToRecyclerView(recyclerview)
        }
    }

    // DP를 PX값으로 변환
    private fun dipToPx(dipValue: Float, context: Context): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }
}