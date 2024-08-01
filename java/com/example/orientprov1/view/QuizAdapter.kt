package com.example.orientprov1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.QuizWithProgress
//import kotlinx.android.synthetic.main.item_quiz.view.*

class QuizAdapter(private val quizzes: List<QuizWithProgress>) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizzes[position])
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(quiz: QuizWithProgress) {
            itemView.findViewById<TextView>(R.id.quizTitle).text = "${quiz.moduleName} Quiz"
            itemView.findViewById<TextView>(R.id.quizStatus).text = quiz.status

        }
    }
}
