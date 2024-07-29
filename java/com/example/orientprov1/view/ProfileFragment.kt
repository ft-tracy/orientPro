package com.example.orientprov1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orientprov1.R
import com.example.orientprov1.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var btnBack: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        btnBack = view.findViewById(R.id.back_button)

        val userNameTextView = view.findViewById<TextView>(R.id.user_name)
        val firstNameEditText = view.findViewById<EditText>(R.id.first_name)
        val lastNameEditText = view.findViewById<EditText>(R.id.last_name)
        val emailEditText = view.findViewById<EditText>(R.id.email)
        val companyRoleEditText = view.findViewById<EditText>(R.id.company_role)

        // Make EditText fields read-only
        firstNameEditText.isEnabled = false
        lastNameEditText.isEnabled = false
        emailEditText.isEnabled = false
        companyRoleEditText.isEnabled = false

        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.userName.observe(viewLifecycleOwner, Observer { userName ->
            userNameTextView.text = userName
        })

        viewModel.firstName.observe(viewLifecycleOwner, Observer { firstName ->
            firstNameEditText.setText(firstName)
        })

        viewModel.lastName.observe(viewLifecycleOwner, Observer { lastName ->
            lastNameEditText.setText(lastName)
        })

        viewModel.email.observe(viewLifecycleOwner, Observer { email ->
            emailEditText.setText(email)
        })

        viewModel.companyRole.observe(viewLifecycleOwner, Observer { companyRole ->
            companyRoleEditText.setText(companyRole)
        })

        // Fetch data using the token from arguments
        arguments?.getString("token")?.let { token ->
            viewModel.fetchData(token)
        }
    }
}