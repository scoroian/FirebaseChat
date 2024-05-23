package com.scoroian.firebasechat

import android.os.Bundle
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
        commentAdapter = CommentAdapter(commentList)

        view.commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        view.commentsRecyclerView.adapter = commentAdapter

        view.addCommentButton.setOnClickListener {
            val comment = view.commentEditText.text.toString()
            if (comment.isNotEmpty()) {
                val cityComment = CityComment(auth.currentUser?.displayName ?: "Anonymous", comment, System.currentTimeMillis())
                commentsRef.push().setValue(cityComment)
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
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
