package com.example.pas19.minesweeper.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pas19.minesweeper.R
import com.example.pas19.minesweeper.model.RecordItem

class RecordAdapter(var items: List<RecordItem>) : RecyclerView.Adapter<RecordAdapter.MainHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_record_item, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }
    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val time = itemView.findViewById<TextView>(R.id.tv_time)
        private val status = itemView.findViewById<TextView>(R.id.tv_status)
        fun bind(item: RecordItem) {
            time.text = item.time
            status.text = item.status
        }
    }
}