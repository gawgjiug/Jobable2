package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.capstone.databinding.ActivityVideoBinding

class VideoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVideoBinding
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoMap = mapOf(
            binding.videoSeoul to " https://www.broso.or.kr/seoul/mainPage.do"
        )
        videoMap.forEach { (imageView, url) ->
            imageView.setOnClickListener {
                showWebView(url)
            }
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
            finish()
        }
    }

}



