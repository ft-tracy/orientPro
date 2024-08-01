package com.example.orientprov1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.orientprov1.model.error.ErrorType
import androidx.fragment.app.Fragment
import com.example.orientprov1.databinding.FragmentErrorBinding


class ErrorFragment : Fragment() {
    private lateinit var binding: FragmentErrorBinding
    private lateinit var errorType: ErrorType

    companion object {
        private const val ARG_ERROR_TYPE = "error_type"

        fun newInstance(errorType: ErrorType): ErrorFragment {
            return ErrorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ERROR_TYPE, errorType)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            errorType = it.getSerializable(ARG_ERROR_TYPE) as ErrorType
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupErrorView()
    }

    private fun setupErrorView() {
        when (errorType) {
            ErrorType.NETWORK -> {
                binding.errorTitle.text = "Network Error"
                binding.errorMessage.text = "Please check your internet connection and try again."
            }
            ErrorType.SERVER -> {
                binding.errorTitle.text = "Server Error"
                binding.errorMessage.text = "We're experiencing server issues. Please try again later."
            }
            ErrorType.DATA -> {
                binding.errorTitle.text = "Data Error"
                binding.errorMessage.text = "There was an issue with the data. Please refresh or try again."
            }
        }

        binding.btnRetry.setOnClickListener {
            // Implement retry logic
        }

    }

    override fun onStart() {
        super.onStart()
        // Called when the Fragment is visible to the user
    }

    override fun onResume() {
        super.onResume()
        // Called when the Fragment is visible and actively running
    }

    override fun onPause() {
        super.onPause()
        // Called when the system is about to temporarily pause the Fragment
    }

    override fun onStop() {
        super.onStop()
        // Called when the Fragment is no longer visible to the user
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Called when the view hierarchy associated with the Fragment is being removed
    }

    override fun onDestroy() {
        super.onDestroy()
        // Called when the Fragment is being destroyed
    }

    override fun onDetach() {
        super.onDetach()
        // Called when the Fragment is being disassociated from its Activity
    }


}