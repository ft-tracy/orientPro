package com.example.orientpro

sealed class HomepageItem {
    data class RecentCourse(val courseName: String, val courseImageUrl: String, val progress: Int) : HomepageItem()
    data class AvailableCourses(val courseName: String, val courseImageUrl: String) : HomepageItem()
    data class WeeklyProgress(val progress: Int) : HomepageItem()
    data class HelpfulTip(val tip: String) : HomepageItem()
}
