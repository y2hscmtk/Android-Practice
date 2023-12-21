package com.example.androidpractice.recyclerview.swipeRecyclerview2

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R
import java.lang.ref.WeakReference

class ItemVIewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    private val view = WeakReference(itemView) // 아이템 뷰에 대한 약한 참조 생성
    private lateinit var textView: TextView
    private lateinit var textViewDelete: TextView

    var index = 0

    // 삭제 버튼이 클릭될 경우 실행될 콜백 함수
    var onDeleteClick: ((RecyclerView.ViewHolder)-> Unit)? = null

    init {
        // 아이템 뷰에 대한 참ㅈ에 대해
        view.get()?.let {

            // 클릭 이벤트 정의
            it.setOnClickListener {
                // click item to reset swiped position

                // 셀 클릭시, 스와이프 상태를 취소하고 기본상태로 되돌아올 수 있도록(선택사항)
//                if(view.get()?.scrollX != 0){
//                    view.get()?.scrollTo(0,0)
//                }
            }

            // 참조 찾기
            textView = it.findViewById(R.id.textView) // 인덱스 표시용
            textViewDelete = it.findViewById(R.id.textViewDelete) // 삭제버튼

            // 삭제 버튼 클릭시 동작 정의
            textViewDelete.setOnClickListener {
                onDeleteClick?.let { onDeleteClick ->
                    onDeleteClick(this)
                }
            }
        }
    }

    fun updateView() {
        // must rest swiped position because item is reused
        // 스와이프 이후 포지션을 다시 리셋해줘야함(재사용을 위해?)
        view.get()?.scrollTo(0,0)
        textView.text = "index $index"
    }
}