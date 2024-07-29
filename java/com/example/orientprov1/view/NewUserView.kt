/*
package com.example.orientprov1.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.orientprov1.viewmodel.NewUserViewModel

class NewUserView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: ViewNewUserBinding
    private lateinit var viewModel: NewUserViewModel
    private lateinit var courseAdapter: CoursesAdapter
    private lateinit var helpfulTipsAdapter: HelpfulTipsAdapter

    init {
        initView()
    }

    private fun initView() {
        binding = ViewNewUserBinding.inflate(LayoutInflater.from(context), this, true)
        viewModel = NewUserViewModel()

        setupRecyclerViews()
        setupObservers()
        loadData()
    }

    private fun loadData() {
        TODO("Not yet implemented")
    }

    private fun setupObservers() {
        TODO("Not yet implemented")
    }

    private fun setupRecyclerViews() {
        courseAdapter = CourseAdapter(emptyList()) { course ->
            // Handle course click
        }
        binding.rvCourses.adapter

    }
}    */
