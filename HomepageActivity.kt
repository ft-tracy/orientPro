package com.example.orientpro

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.orientpro.databinding.ActivityHomepageBinding

class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI elements
        val btnSettings = binding.btnSettings
        val drawerLayout = binding.drawerLayout
        val blurOverlay = binding.blurOverlay
        val navView = binding.navView

        // Set the drawer width to 50% of the screen
        val layoutParams = navView.layoutParams
        layoutParams.width = resources.displayMetrics.widthPixels / 2
        navView.layoutParams = layoutParams

        // Add click listeners
        btnSettings.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
            blurOverlay.visibility = View.VISIBLE
        }

        blurOverlay.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            blurOverlay.visibility = View.GONE
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Handle drawer slide if needed
            }

            override fun onDrawerOpened(drawerView: View) {
                blurOverlay.visibility = View.VISIBLE
            }

            override fun onDrawerClosed(drawerView: View) {
                blurOverlay.visibility = View.GONE
            }

            override fun onDrawerStateChanged(newState: Int) {
                // Handle drawer state change if needed
            }
        })

        // Access the back arrow button in the navigation view header
        val headerView = navView.getHeaderView(0)
        val btnArrowBackSettings: ImageButton = headerView.findViewById(R.id.btn_arrowback_settings)

        btnArrowBackSettings.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
            blurOverlay.visibility = View.GONE
        }

        // Additional initialization or testing code can be added here
    }
}
