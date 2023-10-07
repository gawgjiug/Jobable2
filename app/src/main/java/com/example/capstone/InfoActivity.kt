package com.example.capstone;

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R
import com.example.capstone.databinding.ActivityInfoBinding
import com.example.capstone.databinding.ActivityWelfareBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var infoGyeongi: ImageView
    private lateinit var infoSeoul: ImageView
    private lateinit var infoEtc: ImageView
    private lateinit var seoul1 : ImageView
    private lateinit var seoul2 : ImageView
    private lateinit var seoul3 : ImageView
    private lateinit var seoul4 : ImageView
    private lateinit var seoul5 : ImageView
    private lateinit var seoul6 : ImageView
    private lateinit var seoul7 : ImageView
    private lateinit var seoul8 : ImageView
    private lateinit var seoul9 : ImageView
    private lateinit var gyeongi_1 : ImageView
    private lateinit var gyeongi_2 : ImageView
    private lateinit var gyeongi_3 : ImageView
    private lateinit var gyeongi_4 : ImageView
    private lateinit var gyeongi_5 : ImageView
    private lateinit var gyeongi_6 : ImageView
    private lateinit var gyeongi_7 : ImageView
    private lateinit var gyeongi_8 : ImageView
    private lateinit var gyeongi_9 : ImageView
    private lateinit var binding: ActivityInfoBinding
    private lateinit var webView: WebView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        infoGyeongi = findViewById(R.id.info_gyeongi)
        infoSeoul = findViewById(R.id.info_seoul)
        infoEtc = findViewById(R.id.info_etc)

        seoul1 = findViewById(R.id.seoul_1)
        seoul2 = findViewById(R.id.seoul_2)
        seoul3 = findViewById(R.id.seoul_3)
        seoul4 = findViewById(R.id.seoul_4)
        seoul5 = findViewById(R.id.seoul_5)
        seoul6 = findViewById(R.id.seoul_6)
        seoul7 = findViewById(R.id.seoul_7)
        seoul8 = findViewById(R.id.seoul_8)
        seoul9 = findViewById(R.id.seoul_9)






        infoGyeongi.setOnClickListener {
            // 클릭 시 이미지 변경
            infoEtc.setImageResource(R.drawable.info_etc)
            infoGyeongi.setImageResource(R.drawable.info_click_gyeongi)
            infoSeoul.setImageResource(R.drawable.info_seoul)
            seoul1.setImageResource(R.drawable.gyeongi_1)
            seoul2.setImageResource(R.drawable.gyeongi_2)
            seoul3.setImageResource(R.drawable.gyeongi_3)
            seoul4.setImageResource(R.drawable.gyeongi_4)
            seoul5.setImageResource(R.drawable.gyeongi_5)
            seoul6.setImageResource(R.drawable.gyeongi_6)
            seoul7.setImageResource(R.drawable.gyeongi_7)
            seoul8.setImageResource(R.drawable.gyeongi_8)
            seoul9.setImageResource(R.drawable.gyeongi_9)
        }

        infoSeoul.setOnClickListener{

            infoEtc.setImageResource(R.drawable.info_etc)
            infoGyeongi.setImageResource(R.drawable.info_gyeongi)
            infoSeoul.setImageResource(R.drawable.info_click_seoul)
            seoul1.setImageResource(R.drawable.seoul_1)
            seoul2.setImageResource(R.drawable.seoul_1)
            seoul3.setImageResource(R.drawable.seoul_3)
            seoul4.setImageResource(R.drawable.seoul_4)
            seoul5.setImageResource(R.drawable.seoul_5)
            seoul6.setImageResource(R.drawable.seoul_6)
            seoul7.setImageResource(R.drawable.seoul_7)
            seoul8.setImageResource(R.drawable.seoul_8)
            seoul9.setImageResource(R.drawable.seoul_9)

        }
        infoEtc.setOnClickListener {
            infoEtc.setImageResource(R.drawable.info_click_etc)
            infoGyeongi.setImageResource(R.drawable.info_gyeongi)
            infoSeoul.setImageResource(R.drawable.info_seoul)
            seoul1.setImageResource(R.drawable.etc_1)
            seoul2.setImageResource(R.drawable.etc_2)
            seoul3.setImageResource(R.drawable.etc_3)
        }



        val gyeongi_1 = R.drawable.gyeongi_1
        val infoMap = mapOf(
            binding.seoul1 to "http://www.sbcil.org/child/sub/activity/",
//            gyeongi_1 to "https://www.suwonrehab.or.kr/bbs/content.php?co_id=bussiness9"
        )

        infoMap.forEach{(imageView,url) ->
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
        if (!::webView.isInitialized) {
            // webView가 초기화되지 않은 경우 초기화
            webView = WebView(this)
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = WebViewClient()
            setContentView(webView)
        }

        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }
}
