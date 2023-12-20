package com.example.androidpractice.recyclerview.swipeRecyclerview2

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R
import java.lang.ref.WeakReference

class ItemVIewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    private val view = WeakReference(itemView) // ?
    private lateinit var textView: TextView
    private lateinit var textViewDelete: TextView

    var index = 0

    var onDelteClick: ((RecyclerView.ViewHolder)-> Unit)? = null

    init {
        view.get()?.let {

            it.setOnClickListener {
                // click item to reset swiped position
                if(view.get()?.scrollX != 0){
                    view.get()?.scrollTo(0,0)
                }
            }

            textView = it.findViewById(R.id.textView)
            textViewDelete = it.findViewById(R.id.textViewDelete)

            textViewDelete.setOnClickListener {
                onDelteClick?.let { onDelteClick ->
                    onDelteClick(this)
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