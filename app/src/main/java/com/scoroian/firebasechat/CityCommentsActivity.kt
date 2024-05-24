package com.scoroian.firebasechat

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.scoroian.firebasechat.databinding.ActivityCityCommentsBinding

class CityCommentsActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var commentsRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var commentList: MutableList<CityComment>
    private lateinit var commentAdapter: CommentAdapter
    private val view by lazy { ActivityCityCommentsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val selectedCity = intent.getStringExtra("selectedCity") ?: "Unknown City"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://weatherfirebasechat-default-rtdb.firebaseio.com/")
        commentsRef = database.getReference("city_comments").child(selectedCity)

        commentList = mutableListOf()
        commentAdapter = CommentAdapter(commentList, auth.currentUser?.displayName ?: "") { view, position ->
            showPopupMenu(view, position)
        }

        view.commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        view.commentsRecyclerView.adapter = commentAdapter

        view.addCommentButton.setOnClickListener {
            val comment = view.commentEditText.text.toString()
            if (comment.isNotEmpty()) {
                val commentId = commentsRef.push().key ?: ""
                val cityComment = CityComment(commentId, auth.currentUser?.displayName ?: "Anonymous", comment, System.currentTimeMillis(), selectedCity)
                commentsRef.child(commentId).setValue(cityComment)
                view.commentEditText.text.clear()
            }
        }

        commentsRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val comment = snapshot.getValue(CityComment::class.java)
                comment?.let {
                    commentList.add(it)
                    commentAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val comment = snapshot.getValue(CityComment::class.java)
                comment?.let {
                    val index = commentList.indexOfFirst { it.id == comment.id }
                    if (index != -1) {
                        commentList.removeAt(index)
                        commentAdapter.notifyItemRemoved(index)
                    }
                }
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_comment_options)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_delete -> {
                    deleteComment(position)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun deleteComment(position: Int) {
        val comment = commentList[position]
        commentsRef.child(comment.id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                commentList.removeAt(position)
                commentAdapter.notifyItemRemoved(position)
            }
        }
    }
}
