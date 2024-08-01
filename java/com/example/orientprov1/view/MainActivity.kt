package com.example.orientprov1.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.orientprov1.R
import com.example.orientprov1.databinding.ActivityMainBinding
import com.example.orientprov1.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var viewModel: MainViewModel
    private var settingsFragment: SettingsView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("MainActivity", "onCreate called")

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Set up the NavController
        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            Log.d("MainActivity", "NavController initialized")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing NavController: ${e.message}", e)
            return
        }

        // Set up the bottom navigation
        setupBottomNavigation()

        // Set up the settings drawer
        setupSettingsDrawer()

// Retrieve token from intent or SharedPreferences
        val token = intent.getStringExtra("TOKEN_KEY") ?: getTokenFromPreferences()
        Log.d("MainActivity", "Token is $token")
        token?.let {
            viewModel.setToken(it)
            Log.d("MainActivity", "setting token")
            viewModel.fetchUserDetails()
            Log.d("MainActivity", "fetching user details")
            setTokenForHomeFragment(it) // Set token for HomeFragment without navigating
        }


        // Observe user details and update UI accordingly
        viewModel.userDetails.observe(this, Observer { userResponse ->
            // Update UI based on user details
        })

    }

    private fun setTokenForHomeFragment(token: String) {
        val bundle = Bundle().apply {
            putString("TOKEN_KEY", token)
        }
        Log.d("MainActivity", "Home Fragment Bundle contains: $bundle")
        navController.currentBackStackEntry?.savedStateHandle?.set("TOKEN_KEY", token)
        Log.d("MainActivity", "Token set for Home Fragment")
    }

    private fun getTokenFromPreferences(): String? {
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("TOKEN_KEY", null)
    }

    private fun setupSettingsDrawer() {
        try {
            binding.main.addDrawerListener(object : DrawerLayout.DrawerListener {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

                override fun onDrawerOpened(drawerView: View) {
                    if (settingsFragment == null) {
                        settingsFragment = SettingsView()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.settings_container, settingsFragment!!)
                            .commit()
                    }
                    binding.blurOverlay.visibility = View.VISIBLE
                    Log.d("MainActivity", "Settings drawer opened and SettingsView inflated")
                }

                override fun onDrawerClosed(drawerView: View) {
                    binding.blurOverlay.visibility = View.GONE
                    Log.d("MainActivity", "Settings drawer closed")
                }

                override fun onDrawerStateChanged(newState: Int) {}
            })

            // Your existing close button setup
            binding.navView.findViewById<View>(R.id.closeButton)?.setOnClickListener {
                binding.main.closeDrawer(GravityCompat.START)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting up SettingsDrawer: ${e.message}", e)
        }
    }

    private fun setupBottomNavigation() {
        try {
            binding.bottomNavigation.setupWithNavController(navController)
            Log.d("MainActivity", "BottomNavigation setup with NavController")

            binding.bottomNavigation.setOnItemSelectedListener { item ->
                val bundle = Bundle().apply {
                    putString("TOKEN_KEY", viewModel.token)
                }
                when (item.itemId) {
                    R.id.navigation_home -> {
                        navController.navigate(R.id.navigation_home, bundle)
                        Log.d("MainActivity", "Navigated to home")
                    }
                    R.id.navigation_quiz -> {
                        navController.navigate(R.id.navigation_quiz, bundle)
                        Log.d("MainActivity", "Navigated to quiz")
                    }
                    R.id.navigation_course -> {
                        navController.navigate(R.id.navigation_course, bundle)
                        Log.d("MainActivity", "Navigated to course")
                    }
                    R.id.navigation_certificate -> {
                        navController.navigate(R.id.navigation_certificate, bundle)
                        Log.d("MainActivity", "Navigated to certificate")
                    }
                    R.id.navigation_settings -> {
                        // Open the settings drawer
                        Log.d("MainActivity", "Attempting to open SettingsView")
                        binding.main.openDrawer(GravityCompat.START)
                        binding.blurOverlay.visibility = View.VISIBLE
                        Log.d("MainActivity", "Settings drawer opened")
                        return@setOnItemSelectedListener false
                    }
                }
                true
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting up BottomNavigation: ${e.message}", e)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when {
            binding.main.isDrawerOpen(GravityCompat.START) -> {
                binding.main.closeDrawer(GravityCompat.START)
                binding.blurOverlay.visibility = View.GONE
                Log.d("MainActivity", "Drawer closed via back press")
            }
            supportFragmentManager.backStackEntryCount > 0 -> {
                supportFragmentManager.popBackStack()
                binding.blurOverlay.visibility = View.GONE
                Log.d("MainActivity", "Fragment popped from back stack")
            }
            else -> {
                super.onBackPressed()
                Log.d("MainActivity", "Back pressed")
            }
        }
    }
}