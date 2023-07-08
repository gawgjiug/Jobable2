package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import com.example.capstone.databinding.ActivityWelfareBinding
import com.example.capstone.welfarewebview.WelfareSeoulActivity

class WelfareActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWelfareBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelfareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val welFareSuwonImageView = findViewById<ImageView>(R.id.WelfareSuwon)

        binding.WelfareSeoul.setOnClickListener {
            val intent = Intent(this,WelfareSeoulActivity::class.java)
            startActivity(intent)
        }

        welFareSuwonImageView.setOnClickListener {
            showWebView("https://www.suwonrehab.or.kr/bbs/content.php?co_id=bussiness5")
        }
    }

    private fun showWebView(url: String) {
        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        setContentView(webView)
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
