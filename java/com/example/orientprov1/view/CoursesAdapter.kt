package com.example.orientprov1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.Course

class CourseAdapter(private val courses: List<Course>, private val onCourseClick: (Course) -> Unit) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_available_courses, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bind(courses[position])
    }

    override fun getItemCount(): Int = courses.size.coerceAtMost(5)

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCourse: ImageView = itemView.findViewById(R.id.iv_course_image)

        fun bind(course: Course) {
            // Load image using Glide or Picasso
            // Glide.with(itemView).load(course.imageUrl).into(ivCourse)
            itemView.setOnClickListener { onCourseClick(course) }
        }
    }
}