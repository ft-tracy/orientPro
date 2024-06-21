package com.example.orientpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SimilarCoursesAdapter(private val courses: List<Course>) : RecyclerView.Adapter<SimilarCoursesAdapter.SimilarCourseViewHolder>() {

    class SimilarCourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCourseImage: ImageView = view.findViewById(R.id.iv_course_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarCourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_available_courses, parent, false)
        return SimilarCourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimilarCourseViewHolder, position: Int) {
        val course = courses[position]
        holder.ivCourseImage.setImageResource(course.imageResId) // Update with actual image loading logic
    }

    override fun getItemCount(): Int = courses.size
}
