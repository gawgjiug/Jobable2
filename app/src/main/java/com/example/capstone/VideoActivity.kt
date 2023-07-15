package com.example.capstone


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import com.example.capstone.databinding.ActivityVideoBinding
import com.example.capstone.video.ClothActivity
import com.example.capstone.video.OfficeActivity


class VideoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityVideoBinding
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoMap = mapOf(
            binding.videoSeoul to " https://www.broso.or.kr/seoul/mainPage.do",
            binding.videoDamoa to " https://www.damoa.or.kr/main/inner.php?sMenu=A0000&category=%EC%83%9D%ED%99%9C%EC%A0%95%EB%B3%B4",
            binding.videoBodacenter to " https://xn--2j1bv4q8pej3e.xn--3e0b707e/?menucode=10000&tmenu=wonder"
        )
        videoMap.forEach { (imageView, url) ->
            imageView.setOnClickListener {
                showWebView(url)
            }
        }

        val video_cloth = findViewById<ImageView>(R.id.video_cloth)
        video_cloth.setOnClickListener {
            val intent = Intent(this, ClothActivity::class.java)
            startActivity(intent)
        }
        val video_office = findViewById<ImageView>(R.id.video_office)
        video_office.setOnClickListener {
            val intent = Intent(this,OfficeActivity::class.java)
            startActivity(intent)
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



