package com.example.androidpractice.recyclerview.swipeRecyclerview2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R

class SwipeRecyclerView2Adapter : RecyclerView.Adapter<ItemVIewHolder>() {

    private val list = mutableListOf<Int>()

    // RecyclerView Adapter Implementation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVIewHolder{
        return ItemVIewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_swipe2,parent,false))
    }

    override fun onBindViewHolder(holder: ItemVIewHolder, position: Int) {
        holder.index = list[position]

        // 삭제 이벤트 정의
        holder.onDeleteClick = {
            removeItem(it)
        }

        holder.updateView()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun reload(list: List<Int>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    // 아이템 삭제
    private fun removeItem(viewHolder: RecyclerView.ViewHolder){
        // 삭제할 아이템의 인덱스
        val position = viewHolder.adapterPosition

        list.remove(position) //remove data
        notifyItemRemoved(position) //remove item
    }




}