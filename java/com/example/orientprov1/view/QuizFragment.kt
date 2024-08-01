package com.example.orientprov1.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.Progress
import com.example.orientprov1.model.QuizDetails
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.repository.QuizRepository
import com.example.orientprov1.viewmodel.MainViewModel
import com.example.orientprov1.viewmodel.QuizViewModel
import com.example.orientprov1.viewmodel.QuizViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel
    private lateinit var quizRecyclerView: RecyclerView
    private lateinit var adapter: QuizAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("QuizFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("QuizFragment", "onViewCreated called")

        val token = arguments?.getString("TOKEN_KEY") ?: return

        quizRecyclerView = view.findViewById(R.id.QuizRecyclerView)
        quizRecyclerView.layoutManager = LinearLayoutManager(context)

        val apiService = ApiClient.createService(ApiService::class.java)
        val repository = QuizRepository(apiService)
        val factory = QuizViewModelFactory(repository, apiService)
        viewModel = ViewModelProvider(this, factory).get(QuizViewModel::class.java)

        viewModel.quizDetailsWithProgress.observe(viewLifecycleOwner, Observer { quizzes ->
            adapter = QuizAdapter(quizzes)
            quizRecyclerView.adapter = adapter
        })

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

//        mainViewModel.userDetails.observe(viewLifecycleOwner) {
//        }

        loadData()
    }

    private fun loadData() {
        // Assuming you have a way to get the user token
        Log.d("QuizFragment", "loadData called")
        val userToken = mainViewModel.token
        userToken?.let {
            viewModel.fetchAllQuizzesWithProgress(it)
        }
    }
}
