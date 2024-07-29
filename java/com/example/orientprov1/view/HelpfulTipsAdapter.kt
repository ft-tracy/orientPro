package com.example.orientprov1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.HelpfulTip

class HelpfulTipAdapter(private val tips: List<HelpfulTip>) :
    RecyclerView.Adapter<HelpfulTipAdapter.HelpfulTipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpfulTipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_helpful_tips, parent, false)
        return HelpfulTipViewHolder(view)
    }

    override fun onBindViewHolder(holder: HelpfulTipViewHolder, position: Int) {
        holder.bind(tips[position])
    }

    override fun getItemCount(): Int = tips.size

    class HelpfulTipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTipText: TextView = itemView.findViewById(R.id.tvHelpfulTipText)

        fun bind(tip: HelpfulTip) {
            tvTipText.text = tip.text
        }
    }
}