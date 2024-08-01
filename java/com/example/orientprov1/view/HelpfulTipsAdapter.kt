package com.example.orientprov1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.HelpfulTip

class HelpfulTipsAdapter :
    ListAdapter<HelpfulTip, HelpfulTipsAdapter.HelpfulTipViewHolder>(HelpfulTipDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpfulTipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_helpful_tips, parent, false)
        return HelpfulTipViewHolder(view)
    }

    override fun onBindViewHolder(holder: HelpfulTipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HelpfulTipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTipUsername: TextView = itemView.findViewById(R.id.tipUsername)
        private val tvTipText: TextView = itemView.findViewById(R.id.tvHelpfulTipText)

        fun bind(tip: HelpfulTip) {
            // Assuming HelpfulTip contains firstName and lastName
            val username = "${tip.firstName} ${tip.lastName}"
            tvTipUsername.text = username
            tvTipText.text = tip.tipContent
        }
    }


    class HelpfulTipDiffCallback : DiffUtil.ItemCallback<HelpfulTip>() {
        override fun areItemsTheSame(oldItem: HelpfulTip, newItem: HelpfulTip): Boolean {
            return oldItem.tipId == newItem.tipId
        }

        override fun areContentsTheSame(oldItem: HelpfulTip, newItem: HelpfulTip): Boolean {
            return oldItem == newItem
        }
    }
}