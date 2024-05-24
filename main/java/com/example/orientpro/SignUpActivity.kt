package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up click listener for the back button
        binding.btnBack.setOnClickListener {
            finish() // Closes the current activity and returns to the previous one
        }

        // Set up click listener for the proceed button
        binding.btnProceed.setOnClickListener {
            // Here you can add any validation or additional processing before proceeding
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val email = binding.etEmail.text.toString()

            if (validateInput(firstName, lastName, email)) {
                // If validation is successful, proceed to the dummy homepage
                val intent = Intent(this, DummyHomePageActivity::class.java)
                startActivity(intent)
            } else {
                // Show error message if validation fails (you can implement this method)
                showValidationError()
            }
        }
    }

    private fun validateInput(firstName: String, lastName: String, email: String): Boolean {
        // Simple validation logic (you can expand this as needed)
        return firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showValidationError() {
        // Show a simple error message (you can customize this as needed)
        android.widget.Toast.makeText(this, "Please fill out all fields correctly", android.widget.Toast.LENGTH_SHORT).show()
    }
}
