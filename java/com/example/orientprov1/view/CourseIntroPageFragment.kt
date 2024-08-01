package com.example.orientprov1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.example.orientprov1.R
import com.example.orientprov1.viewmodel.CourseIntroPageViewModel
import com.example.orientprov1.viewmodel.CourseIntroPageViewModelFactory
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.Lifecycle
import com.example.orientprov1.databinding.FragmentCourseIntroPageBinding
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.repository.CourseRepository
import androidx.appcompat.app.AppCompatActivity
import com.example.orientprov1.model.Module

class CourseIntroPageFragment : Fragment() {
    private var _binding: FragmentCourseIntroPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CourseIntroPageViewModel by viewModels {
        CourseIntroPageViewModelFactory(courseRepository)
    }
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    val apiService = ApiClient.createService(ApiService::class.java)
    private val courseRepository = CourseRepository(apiService)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseIntroPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseId = arguments?.getString("courseId") ?: ""

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        // Set up toolbar
        val toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        setupDrawer(toolbar)
        observeViewModel()

        // Load initial course data
        viewModel.loadCourseData(courseId)
    }

    private fun setupDrawer(toolbar: androidx.appcompat.widget.Toolbar) {
        // Set up drawer toggle
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up navigation item click listener
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // Handle specific item selection
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    updateDrawerContent(state.modules)
//                    navigateToContent(state.currentContent)
                }
            }
        }
    }

    private fun updateDrawerContent(modules: List<Module>) {
        val menu = navView.menu
        menu.clear()

        // Inflate course item
        val courseItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Course Name")
        courseItem.setActionView(R.layout.item_sidebar_course)

        // Inflate module items
        modules.forEachIndexed { index, module ->
            // Assume you only have one ModuleDetails per ModuleWithContents for this example
            val moduleDetails = module.moduleDetails.firstOrNull()
            moduleDetails?.let { moduleDetail ->
                val moduleItem = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, moduleDetail.moduleName)
                moduleItem.setActionView(R.layout.item_sidebar_module)

                // Inflate content items
                module.contents.forEach { content ->
                    val contentItem = moduleItem.subMenu?.add(Menu.NONE, content.id.hashCode(), Menu.NONE, content.title)
                    contentItem?.setActionView(R.layout.item_sidebar_content)
                    contentItem?.setOnMenuItemClickListener {
                        viewModel.navigateToContent(content.id)
                        drawerLayout.closeDrawers()
                        true
                    }
                }
            }
        }
    }

//    private fun navigateToContent(content: Content?) {
//        content?.let {
//            val action = when (it.type) {
//                ContentType.READING -> R.id.action_courseIntro_to_reading
//                ContentType.VIDEO -> R.id.action_courseIntro_to_video
//                ContentType.QUIZ -> R.id.action_courseIntro_to_quiz
//            }
//            val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//            navHostFragment.navController.navigate(action, Bundle().apply {
//                putString("contentId", it.id)
//            })
//        }
//    }
}
