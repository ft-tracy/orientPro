package com.example.orientpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HomepageAdapter(private val items: List<HomepageItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_RECENT_COURSE = 1
        private const val VIEW_TYPE_AVAILABLE_COURSES = 2
        private const val VIEW_TYPE_WEEKLY_PROGRESS = 3
        private const val VIEW_TYPE_HELPFUL_TIP = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomepageItem.RecentCourse -> VIEW_TYPE_RECENT_COURSE
            is HomepageItem.AvailableCourses -> VIEW_TYPE_AVAILABLE_COURSES
            is HomepageItem.WeeklyProgress -> VIEW_TYPE_WEEKLY_PROGRESS
            is HomepageItem.HelpfulTip -> VIEW_TYPE_HELPFUL_TIP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_RECENT_COURSE -> RecentCourseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recent_course, parent, false))
            VIEW_TYPE_AVAILABLE_COURSES -> AvailableCoursesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_available_courses, parent, false))
            VIEW_TYPE_WEEKLY_PROGRESS -> WeeklyProgressViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weekly_progress, parent, false))
            VIEW_TYPE_HELPFUL_TIP -> HelpfulTipViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_helpful_tips, parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecentCourseViewHolder -> holder.bind(items[position] as HomepageItem.RecentCourse)
            is AvailableCoursesViewHolder -> holder.bind(items[position] as HomepageItem.AvailableCourses)
            is WeeklyProgressViewHolder -> holder.bind(items[position] as HomepageItem.WeeklyProgress)
            is HelpfulTipViewHolder -> holder.bind(items[position] as HomepageItem.HelpfulTip)
        }
    }

    override fun getItemCount(): Int = items.size

    class RecentCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: HomepageItem.RecentCourse) {
            // Bind data to views
        }
    }

    class AvailableCoursesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: HomepageItem.AvailableCourses) {
            // Bind data to views
        }
    }

    class WeeklyProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: HomepageItem.WeeklyProgress) {
            // Bind data to views
        }
    }

    class HelpfulTipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: HomepageItem.HelpfulTip) {
            // Bind data to views
        }
    }
}
