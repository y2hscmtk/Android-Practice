package com.example.androidpractice.recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R
import com.example.androidpractice.item.Group

/*
* <리사이클러뷰>
* reference : https://greensky0026.tistory.com/223
* ViewHolder패턴을 활용하여 아이템을 재활용하는 리스트
* 뷰를 하나 만들어놓고, Holder에 넣어두었다가 재활용하는 패턴
* 좌우 스와이프 및 검색을 위한 필터 등에 대한 추가기능을 부착하기 용이하다.
*
* <필요한 재료>
* 1. 아이템을 출력하기 위한 Recycler View => activity_recyclerview_practice.xml
* 2. 아이템을 표한하기 위한 view => item_group.xml
* 3. 아이템의 값(DTO)
* 4. 아이템을 유지/관리하는 Adapter
* 5. Recyclerview를 출력할 방법을 결정할 layout manager
* */



class RecyclerViewPracticeActivity : AppCompatActivity() {
    //recycler view layout
    lateinit var recyclerViewGroup : RecyclerView

    //recycler view adapter
    lateinit var recyclerViewGroupAdapter : GroupRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview_practice)
        recyclerViewGroup = findViewById(R.id.recyclerview_group) //그룹 리사이클러뷰에 대한 참조
        var groups = initGroupDTOArray() //더미데이터 생성
        setAdapter(groups) //어댑터 붙이기
    }


    //더미 데이터 생성용
    fun initGroupDTOArray(): Array<Group> {
        return arrayOf(
            Group("투밋투미",9),
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
    fun setAdapter(groups: Array<Group>){
        recyclerViewGroup.layoutManager = LinearLayoutManager(this)
        //어탭더 생성
        recyclerViewGroupAdapter = GroupRecyclerViewAdapter(groups, this)
        recyclerViewGroup.adapter = recyclerViewGroupAdapter
    }
}