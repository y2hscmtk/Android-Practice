package com.example.androidpractice

import android.app.ProgressDialog.show
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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


// 3.아이템 값 DTO
// Data Transfer Object : 계층 간 데이터 전송을 위한 도메인 모델 대신 사용되는 객체
data class Group(
    var groupName: String, // 그룹 이름
    var groupMemberCount : Int // 그룹에 포함된 전체 인원 수
){} //{} : 클래스 본문에 해당, 현재 추가적인 로직이 없기 때문에 비워져 있음

// 4.아이템을 유지/관리하는 Adapter
class GroupRecyclerViewAdapter(var groups: Array<Group>, var context: Context) : //화면에 데이터를 붙이기 위해 context가 필요함
    RecyclerView.Adapter<GroupRecyclerViewAdapter.ViewHolder>() { //리사이클러뷰 어댑터를 상속, Generic 값으로 innerClass인 ViewHolder를 넣어줘야함

    //(2)
    //ViewHolder패턴 => View를 Holder에 넣어두었다가 재사용을 하기 위함
    //=> itemView는 onCreateViewHolder에서 전달받은 아이템 뷰의 레이아웃에 해당
    //=> onBindViewHolder에서 view에 groups의 값을 할당함
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv_group_name: TextView // 그룹의 이름을 보여주기 위함
        lateinit var tv_group_member_count: TextView // 전체 인원수를 보여주기 위함
        init { //innerClass의 생성자에 해당 => 뷰의 레이아웃 가져오기 => 화면에 붙이기 위한 하나의 뷰를 만드는 과정에 해당
            tv_group_name = itemView.findViewById(R.id.tv_group_name)
            tv_group_member_count = itemView.findViewById(R.id.tv_group_member_count)

            //아이템 클릭에 대한 이벤트 정의
            itemView.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    var position = adapterPosition // 클릭한 값이 몇번째에 위치한 값인지
                    var group = groups[position] // 클릭한 그룹 정보 받아오기
                    setTitle(group.groupName)
                    setMessage(group.groupMemberCount.toString())
                    setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(context, "OK Button Click", Toast.LENGTH_SHORT).show()
                    })
                    show()
                }
            }
        }
    }

    //아이템 뷰의 레이아웃을 가져와서 화면에 붙임 (1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        //화면에 뷰를 붙이기 위해 inflater가 필요
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //아이템 뷰 레이아웃 가져오기
        val view = inflater.inflate(R.layout.item_group, parent, false)

        return ViewHolder(view)
    }


    //(3)
    //itemView에 Array<Group>의 값을 할당함
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group: Group = groups[position]
        holder.tv_group_name.text = group.groupName
        holder.tv_group_member_count.text = group.groupMemberCount.toString()
    }


    //리사이클러뷰의 아이템의 개수가 총 몇개인지를 리턴
    override fun getItemCount(): Int {
        return groups.size
    }
}


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