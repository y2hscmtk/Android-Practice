package com.example.androidpractice.recyclerview.swipeRecyclerview


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R


class SwipeListAdapter : RecyclerView.Adapter<SwipeListAdapter.SwipeViewHolder>() {

    private val items: MutableList<String> = mutableListOf<String>().apply {
        for (i in 0..10)
            add("$i")
    }

    //뷰 홀더
    class SwipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var isClamped: Boolean = false // clamp 되었는지 여부를 저장하기 위함
        fun setClamped(clamped: Boolean) {
            this.isClamped = clamped
        }

        fun getClamped(): Boolean {
            return isClamped
        }

        var id : TextView = itemView.findViewById(R.id.tv_textView)
    }

    //화면에 붙일 아이템의 리소스
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_swipe, parent, false)
        return SwipeViewHolder(view)
    }



    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
        val item = items[position]
        holder.id.text = item
    }


    override fun getItemCount(): Int = items.size


}