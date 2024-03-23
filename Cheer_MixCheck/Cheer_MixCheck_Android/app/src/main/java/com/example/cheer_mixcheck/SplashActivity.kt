package com.example.cheer_mixcheck

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            try {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Log.d("SplashActivity", "Successfully transitioned from SplashActivity to MainActivity.")
            } catch (e: Exception) {
                Log.e("SplashActivity", "Error transitioning from SplashActivity to MainActivity", e)
            }
        }, SPLASH_TIME_OUT)
    }
}