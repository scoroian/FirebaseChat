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
                val nickname = view.nicknameEditText.text.toString()
                if (nickname.isNotEmpty()) {
                    // Se crea un usuario anonimo en Firebase Authentication.
                    Log.d("NicknameActivity", "AQUI LLEGO 111.")
                    auth.signInAnonymously().addOnCompleteListener {
                        Log.d("NicknameActivity", "AQUI LLEGO 222.")
                        Log.d("NicknameActivity", it.toString())
                        if (it.isSuccessful) { //Si se crea en FA
                            Log.d("NicknameActivity", "AQUI LLEGO 333.")
                            val currentUser = auth.currentUser //Obtengo el nombre que en FA es vacio/null
                            //Creoo un perfil en local.
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(nickname)
                                .build()
                            //Updateo el perfil en FA con el nuevo perfil.
                            currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { updateProfileTask ->
                                if (updateProfileTask.isSuccessful) {
                                    Log.d("NicknameActivity", "User profile updated.")
                                    startActivity(Intent(this, CitySelectionActivity::class.java))
                                    finish()
                                } else {
                                    Log.e("NicknameActivity", "Failed to update profile: ${updateProfileTask.exception}")
                                }
                            }
                        } else {
                            Log.e("NicknameActivity", "Anonymous sign-in failed: ${it.exception}")
                        }
                    }
                }
            }
        }
        /*view.enterButton.setOnClickListener {
            val nickname = view.nicknameEditText.text.toString()
            if (nickname.isNotEmpty()) {
                // Guardar el nickname en SharedPreferences
                val sharedPref = getSharedPreferences("prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString("nickname", nickname)
                    putBoolean("isFirstTime", false)
                    apply()
                }
                startActivity(Intent(this, CitySelectionActivity::class.java))
            }
        }*/

    }
}