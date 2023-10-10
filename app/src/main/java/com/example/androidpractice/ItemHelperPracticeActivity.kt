package com.example.androidpractice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/* ItemHelper.callback
* 1. getMovementFlags(RecyclerView, ViewHolder)
* 각각의 뷰에 수행할 작업을 업데이트?
* 2. onMove(recyclerView, dragged, target)
* 사용자가 아이템을 드래그하면 ItemTouchHelper가 onMove()를 호출
* => 아이템을 새로운 위치로 이동시키고, RecyclerView의 Adapter에서 notifyItemMoved를 호출해야함
* 3. onSwiped(ViewHolder, int)
* View가 스와이프되면 ItemTouchHelper는 범위를 벗어날 때까지 View를 애니메이션화한 다음 이 메소드를 호출함
* => 여기서 어댑터를 업데이트 해야하고 관련된 Adapter의 notify 이벤트를 호출해야 함
* */


/* <Used Class>
* GroupRecyclerViewAdapterWithItemHelper.kt : Adapter with ItemHelper Callback
* Group.kt : DTO
* ItemTouchHelperCallback.kt : ItemTouchHelperCallback, ItemTouchHelperListner(interface)
* <Used layout>
* activity_item_helper_practice.xml
* item_group.xml
* */
class ItemHelperPracticeActivity : AppCompatActivity() {

    //recycler view layout
    lateinit var recyclerViewGroup : RecyclerView

    //recycler view adapter
    lateinit var recyclerViewAdapterWithItemHelper: GroupRecyclerViewAdapterWithItemHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_helper_practice)
        recyclerViewGroup = findViewById(R.id.recyclerview_group) //그룹 리사이클러뷰에 대한 참조
        var groups = initGroupDTOArray() //더미데이터 생성
        setAdapter(groups) //어댑터 붙이기
    }


    //더미 데이터 생성용
    fun initGroupDTOArray(): MutableList<Group> {
        return mutableListOf(
            (Group("투밋투미",9)),
            Group("공학경진대회",4),
            Group("캡스톤 디자인",4),
            Group("안드로이드 스터디",3),
            Group("설계패턴 스터디",3),
            Group("운영체제 스터디",3),
            Group("멋쟁이 사자처럼",40),
            Group("UMC",45),
        )

    }

    //리사이클러뷰에 리사이클러뷰 어댑터 부착
    fun setAdapter(groups: MutableList<Group>){
        recyclerViewGroup.layoutManager = LinearLayoutManager(this)
        //어탭더 생성
        recyclerViewAdapterWithItemHelper = GroupRecyclerViewAdapterWithItemHelper(groups, this)
        recyclerViewGroup.adapter = recyclerViewAdapterWithItemHelper

        // 리스너를 구현한 Adapter 클래스를 Callback 클래스의 생성자로 지정
        val itemTouchHelperCallback = ItemTouchHelperCallback(recyclerViewAdapterWithItemHelper)

        // ItemTouchHelper의 생성자로 ItemTouchHelper.Callback 객체 세팅
        val helper = ItemTouchHelper(itemTouchHelperCallback)

        // RecyclerView에 ItemTouchHelper 연결
        helper.attachToRecyclerView(recyclerViewGroup)
    }

}