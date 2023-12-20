package com.example.androidpractice.recyclerview.swipeRecyclerview2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R
import java.lang.ref.WeakReference

class Adapter : RecyclerView.Adapter<Adapter.ItemViewHolder>() {

    private val list = mutableListOf<Int>()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val view = WeakReference(itemView) // ?
        private lateinit var textView: TextView
        private lateinit var textViewDelete: TextView

        var index = 0

        init {
            view.get()?.let {
                textView = it.findViewById(R.id.textView)
                textViewDelete = it.findViewById(R.id.textViewDelete)
            }
        }

        fun updateView() {
            // must rest swiped position because item is reused
            // 스와이프 이후 포지션을 다시 리셋해줘야함(재사용을 위해?)
            view.get()?.scrollTo(0,0)
            textView.text = "index $index"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_swipe2,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.index = list[position]
        holder.updateView()
    }

    fun reload(list: List<Int>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return list.size
    }


}