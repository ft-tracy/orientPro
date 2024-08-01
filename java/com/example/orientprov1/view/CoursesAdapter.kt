package com.example.orientprov1.view

import com.bumptech.glide.Glide
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.Course

class CoursesAdapter(
    private val onCourseClick: (Course) -> Unit
) :
    ListAdapter<Course, CoursesAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        Log.d("CoursesAdapter", "onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_available_courses, parent, false)
        Log.d("CoursesAdapter", "view set as: $view")
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        Log.d("CoursesAdapter", "onBindViewHolder called")
        holder.bind(getItem(position))
    }

    inner class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivCourse: ImageView = itemView.findViewById(R.id.iv_available_image)

        fun bind(course: Course) {
            Glide.with(itemView).load(course.courseImageUrl).into(ivCourse)

            itemView.setOnClickListener {
                Log.d("CoursesAdapter", "Course clicked: ${course.courseTitle}")
                Log.d("CoursesAdapter", "onClickListener set")
                onCourseClick(course)
            }
        }
    }

    class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}