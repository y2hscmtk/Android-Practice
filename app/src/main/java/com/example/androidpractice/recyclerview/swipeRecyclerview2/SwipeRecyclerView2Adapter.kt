package com.example.androidpractice.recyclerview.swipeRecyclerview2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidpractice.R

class SwipeRecyclerView2Adapter : RecyclerView.Adapter<ItemVIewHolder>() {

    private val list = mutableListOf<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVIewHolder{
        return ItemVIewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_swipe2,parent,false))
    }

    override fun onBindViewHolder(holder: ItemVIewHolder, position: Int) {
        holder.index = list[position]

        // 삭제 이벤트?
        holder.onDelteClick = {
            removeItem(it)
        }

        holder.updateView()
    }

    fun reload(list: List<Int>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    // 아이템 삭제
    private fun removeItem(viewHolder: RecyclerView.ViewHolder){

        val position = viewHolder.adapterPosition

        list.remove(position) //remove data
        notifyItemRemoved(position) //remove item
    }

    override fun getItemCount(): Int {
        return list.size
    }


}