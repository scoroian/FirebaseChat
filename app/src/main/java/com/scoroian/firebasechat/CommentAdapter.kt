package com.scoroian.firebasechat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scoroian.firebasechat.databinding.ItemCommentBinding
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(
    private val comments: List<CityComment>,
    private val currentUser: String,
    private val onLongClick: (View, Int) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.binding.userNameTextView.text = comment.user
        holder.binding.commentTextView.text = comment.comment

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val formattedDate = sdf.format(Date(comment.timestamp ?: 0))
        holder.binding.timestampTextView.text = formattedDate

        if (comment.user == currentUser) {
            holder.binding.root.setOnLongClickListener {
                onLongClick(it, position)
                true
            }
        } else {
            holder.binding.root.setOnLongClickListener(null)
        }
    }

    override fun getItemCount(): Int = comments.size

    class CommentViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)
}
