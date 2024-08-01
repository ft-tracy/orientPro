package com.example.orientprov1.model

import java.io.Serializable

data class Course(
    val id: String,
    val courseTitle: String,
    val courseImageUrl: String,
    val imageResId: Int,
    val progress: Int,
    val modules: List<Module>,
//    val courseModules: List<Module>,
    val courseDescription: String,
    val courseTags: List<String>,
    val createdOn: String,
    val exclusiveToCompanyEmployees: String
) : Serializable
