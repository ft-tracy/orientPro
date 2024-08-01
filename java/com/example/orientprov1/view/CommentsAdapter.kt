package com.example.orientprov1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orientprov1.R
import com.example.orientprov1.model.Comment
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class CommentsAdapter(
    private val onLikeClick: (String, Int) -> Unit
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private val comments = mutableListOf<Comment>()

    fun submitList(commentList: List<Comment>) {
        comments.clear()
        comments.addAll(commentList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount() = comments.size

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val commentDate: TextView = itemView.findViewById(R.id.comment_date)
        private val commentText: TextView = itemView.findViewById(R.id.comment_text)
        private val likeButton: ImageButton = itemView.findViewById(R.id.like_button)
        private val likeCount: TextView = itemView.findViewById(R.id.like_count)
        private val replyButton: ImageButton = itemView.findViewById(R.id.reply_button)

        fun bind(comment: Comment) {
            profileImage.setImageResource(R.drawable.ic_profile)
            userName.text = "${comment.userFirstName} ${comment.userLastName}"
            commentDate.text = getTimeAgo(comment.date)
            commentText.text = comment.text
            likeCount.text = comment.likes.toString()

            likeButton.setOnClickListener {
                comment.likes++
                likeCount.text = comment.likes.toString()
                onLikeClick(comment.id, comment.likes)
            }

            replyButton.setOnClickListener {
                // Handle reply action
            }
        }

        private fun getTimeAgo(date: Date): String {
            val diff = System.currentTimeMillis() - date.time
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            val days = TimeUnit.MILLISECONDS.toDays(diff)

            return when {
                minutes < 60 -> "$minutes m"
                hours < 24 -> "$hours h"
                else -> "$days d"
            }
        }
    }
}

