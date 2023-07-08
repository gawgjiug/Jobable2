package com.example.capstone.welfarewebview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.capstone.R
import android.webkit.WebViewClient

class WelfareSeoulActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welfare_seoul)

        val webView = findViewById<WebView>(R.id.seoulWebview)//웹뷰 셋팅
        webView.apply {
            webViewClient = WebViewClient() // 페이지 컨트롤을 위한 기본적인 함수 , 다양한 요청 ,알림을 수신하는기능
            webChromeClient = WebChromeClient()
            //크롬 환경에 맞는 세팅을 해줌 , 특히, 알람등을 받기 위해서는 꼭 선언해주어야 함
            settings.javaScriptEnabled = true // 자바스크립트 허용
        }
        webView.loadUrl("https://wis.seoul.go.kr/handicap/findWelfareService.do")




    }

    override fun onBackPressed() {
        //뒤로 가기 버튼을 눌렀을때 웹 페이지 내에서 뒤로 가기가 있으면 해주고 아니면 , 앱 뒤로가기 실행
        val webView = findViewById<WebView>(R.id.seoulWebview)
        if (webView.canGoBack())
        {
            webView.goBack()
        }
        else{
            finish()
        }
    }



}