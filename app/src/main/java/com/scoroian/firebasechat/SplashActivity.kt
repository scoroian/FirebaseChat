package com.scoroian.firebasechat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.scoroian.firebasechat.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val view by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val sharedPref = getSharedPreferences("prefs", MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean("isFirstTime", true)

        val imageView: ImageView = findViewById(R.id.imageView)
        Glide.with(this)
            .asGif()
            .load(R.drawable.cat_spinning)
            .into(imageView)

        Handler(Looper.getMainLooper()).postDelayed({
            if (isFirstTime) {
                startActivity(Intent(this, NicknameActivity::class.java))
            } else {
                startActivity(Intent(this, CitySelectionActivity::class.java))
            }
            finish()
        }, 2000) // Delay de 2 segundos
    }
}
