package com.scoroian.firebasechat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.scoroian.firebasechat.databinding.ActivityNicknameBinding

class NicknameActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val view by lazy { ActivityNicknameBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        if (user == null) {
            auth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    // Usuario autenticado
                }
            }
        }

        view.enterButton.setOnClickListener {
            val nickname = view.nicknameEditText.text.toString()
            if (nickname.isNotEmpty()) {
                // Guardar el nickname en SharedPreferences
                val sharedPref = getSharedPreferences("prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("nickname", nickname)
                    apply()
                }
                startActivity(Intent(this, ChatActivity::class.java))
            }
        }
    }
}