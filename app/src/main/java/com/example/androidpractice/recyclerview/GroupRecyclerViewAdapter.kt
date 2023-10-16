package com.example.androidpractice.recyclerview

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R
import com.example.androidpractice.item.Group

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