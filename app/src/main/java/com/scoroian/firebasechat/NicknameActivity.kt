package com.scoroian.firebasechat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
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
            view.enterButton.setOnClickListener {
                val email = view.emailEditText.text.toString()
                val password = view.passwordEditText.text.toString()
                val nickname = view.nicknameEditText.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && nickname.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { createUserTask ->
                            if (createUserTask.isSuccessful) {
                                Log.d("NicknameActivity", "User created successfully.")
                                val currentUser = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(nickname)
                                    .build()
                                currentUser?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { updateProfileTask ->
                                        if (updateProfileTask.isSuccessful) {
                                            Log.d("NicknameActivity", "User profile updated.")
                                            startActivity(Intent(this, CitySelectionActivity::class.java))
                                            finish()
                                        } else {
                                            Log.e("NicknameActivity", "Failed to update profile: ${updateProfileTask.exception}")
                                        }
                                    }
                            } else {
                                Log.e("NicknameActivity", "User creation failed: ${createUserTask.exception}")
                            }
                        }
                }
            }
        } else {
            startActivity(Intent(this, CitySelectionActivity::class.java))
            finish()
        }
    }
}
