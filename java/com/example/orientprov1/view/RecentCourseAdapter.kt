package com.example.orientprov1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.Course

class RecentCoursesAdapter(
    private val courses: List<Course>,
    private val onContinueClick: (Course) -> Unit
) : RecyclerView.Adapter<RecentCoursesAdapter.RecentCourseViewHolder>() {

    class RecentCourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCourseName: TextView = view.findViewById(R.id.tv_course_name)
        val ivCourseImage: ImageView = view.findViewById(R.id.iv_course_image)
        val pbCourseProgress: ProgressBar = view.findViewById(R.id.pb_course_progress)
        val btnContinue: Button = view.findViewById(R.id.btnContinuercs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentCourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_course, parent, false)
        return RecentCourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentCourseViewHolder, position: Int) {
        val course = courses[position]
        holder.tvCourseName.text = course.name
        holder.ivCourseImage.setImageResource(course.imageResId) // Update with actual image loading logic
        holder.pbCourseProgress.progress = course.progress
        holder.btnContinue.setOnClickListener { onContinueClick(course) }
    }

    override fun getItemCount(): Int = courses.size
}