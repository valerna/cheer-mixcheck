package com.example.cheer_mixcheck

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContentView(webView)

        val url = intent.getStringExtra("url")
        if (url != null) {
            webView.loadUrl(url)
            Log.d("WebViewActivity", "Loading URL: $url")

            // Auto-close the webview after 4 seconds
            Handler().postDelayed({
                finish()
                Log.d("WebViewActivity", "WebView closed after 4 seconds")
            }, 4000)
        } else {
            Log.e("WebViewActivity", "Error: URL is null")
            finish() // Close the activity if the URL is null
        }
    }
}