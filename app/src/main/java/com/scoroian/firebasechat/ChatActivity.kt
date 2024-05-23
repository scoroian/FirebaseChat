package com.scoroian.firebasechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.scoroian.firebasechat.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var chatRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var messageList: MutableList<ChatMessage>
    private lateinit var chatAdapter: ChatAdapter
    private val view by lazy { ActivityChatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val selectedCity = intent.getStringExtra("selectedCity") ?: "Unknown City"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://weatherfirebasechat-default-rtdb.firebaseio.com/")
        chatRef = database.getReference("chats").child(selectedCity)

        messageList = mutableListOf()
        chatAdapter = ChatAdapter(messageList)

        view.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        view.chatRecyclerView.adapter = chatAdapter

        view.sendButton.setOnClickListener {
            val message = view.messageEditText.text.toString()
            if (message.isNotEmpty()) {
                val chatMessage = ChatMessage(auth.currentUser?.displayName ?: "Anonymous", message, System.currentTimeMillis())
                chatRef.push().setValue(chatMessage)
                view.messageEditText.text.clear()  // Limpiar el campo de texto después de enviar el mensaje
            }
        }

        chatRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(ChatMessage::class.java)
                message?.let {
                    messageList.add(it)
                    chatAdapter.notifyDataSetChanged()
                    view.chatRecyclerView.scrollToPosition(messageList.size - 1)  // Desplazarse al último mensaje
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
