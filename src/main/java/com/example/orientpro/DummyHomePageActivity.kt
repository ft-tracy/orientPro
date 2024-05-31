package com.example.orientpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class DummyHomePageActivity : AppCompatActivity(){

private lateinit var btnBack: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy_home_page)

        btnBack.setOnClickListener {
            finish()
        }
    }
}