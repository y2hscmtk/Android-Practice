package com.example.androidpractice

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

//삽입과 삭제, 수정이 필요하므로 전달받는 데이터 리스트를 MutableList타입으로 받아야함
class GroupRecyclerViewAdapterWithItemHelper(var groups: MutableList<Group>, var context: Context):
    RecyclerView.Adapter<GroupRecyclerViewAdapterWithItemHelper.ViewHolder>(),ItemTouchHelperListener {

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
                        Toast.makeText(context, "OK Button Clicked : GroupRecyclerViewAdapterWithItemHelper", Toast.LENGTH_SHORT).show()
                    })
                    show()
                }
            }
        }
    }


    //뷰 홀더 생성 => 재활용 하기 위한 뷰 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        //화면에 뷰를 붙이기 위해 inflater가 필요
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //아이템 뷰 레이아웃 가져오기
        val view = inflater.inflate(R.layout.item_group, parent, false)

        return ViewHolder(view)
    }

    //뷰 홀더에 위치한 뷰에 값 할당(항목 초기화)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group: Group = groups[position]
        holder.tv_group_name.text = group.groupName
        holder.tv_group_member_count.text = group.groupMemberCount.toString()
    }

    override fun getItemCount(): Int {
        return groups.size
    }


    //ItemTouchHelperListner interface implements

    //from_position에서 to_postion으로 아이템의 위치 이동
    //groups의 Group item에 대하여 사용자의 움직임에 맞춰서 배열에서의 위치도 변경해줘야함
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Log.d("onItemMove","onMove")
        val group = groups[fromPosition]
        // 리스트 갱신
        groups.removeAt(fromPosition)
        groups.add(toPosition, group)
        // fromPosition에서 toPosition으로 아이템 이동 공지
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    //사용자가 아이템을 Swipe(왼쪽 혹은 오른쪽으로 밀때)할때 호출됨
    override fun onItemSwipe(position: Int) {
        // 리스트 아이템 삭제
        groups.removeAt(position) //해당 포지션의 아이템 삭제
        // 아이템 삭제되었다고 공지
        notifyItemRemoved(position)
    }

}