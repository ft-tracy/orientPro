package com.example.orientprov1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.DayProgress

class WeeklyProgressAdapter(private val progress: List<DayProgress>) : RecyclerView.Adapter<WeeklyProgressAdapter.WeeklyProgressViewHolder>() {

    class WeeklyProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tv_monday) // Update IDs as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyProgressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weekly_progress, parent, false)
        return WeeklyProgressViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeeklyProgressViewHolder, position: Int) {
        val dayProgress = progress[position]
        holder.tvDay.text = dayProgress.day
        holder.tvDay.setBackgroundResource(if (dayProgress.completed) R.drawable.circle_blue else R.drawable.circle_grey) // Update with actual drawables
    }

    override fun getItemCount(): Int = progress.size
}