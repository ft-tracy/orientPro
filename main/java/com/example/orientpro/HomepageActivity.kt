package com.example.orientpro

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.R

class HomepageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize UI elements
        val btnSettings: ImageButton = findViewById(R.id.btn_settings)
        val btnAddCourse: ImageButton = findViewById(R.id.btn_add_course)
        val tvEnrolledCourses: TextView = findViewById(R.id.tv_enrolled_courses)
        val imgEnrolledCourse1: ImageView = findViewById(R.id.img_enrolled_course_1)
        val imgEnrolledCourse2: ImageView = findViewById(R.id.img_enrolled_course_2)
        val progressBar1: ProgressBar = findViewById(R.id.progressBar1)
        val progressBar2: ProgressBar = findViewById(R.id.progressBar2)
        val tvAvailableCourses: TextView = findViewById(R.id.tv_available_courses)
        val tvHelpfulTips: TextView = findViewById(R.id.tv_helpful_tips)


        // Add click listeners
        btnSettings.setOnClickListener {
            // Code to handle settings button click
        }

        btnAddCourse.setOnClickListener {
            // Code to handle add course button click
        }

        // Additional initialization or testing code can be added here
    }
}