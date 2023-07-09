package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.capstone.databinding.ActivityWelfareBinding

class WelfareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelfareBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelfareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val welfareMap = mapOf(
            binding.WelfareSuwon to "https://www.suwonrehab.or.kr/bbs/content.php?co_id=bussiness5",
            binding.WelfareSeoul to "https://wis.seoul.go.kr/handicap/findWelfareService.do",
            binding.WelfareSiheung to "https://www.siheung.go.kr/portal/contents.do?mId=0204070000",
            binding.WelfareAnsan to "https://www.ansan.go.kr/www/common/cntnts/selectContents.do?cntnts_id=C0001245",
            binding.WelfareDaegu to "https://www.daegu.go.kr/welf/index.do?menu_id=00000503&servletPath=%2Fwelf",
            binding.WelfareWonju to "https://www.wonju.go.kr/welfare/contents.do?key=2252&",
            binding.WelfareGwangju to "https://gjdsc.or.kr/kor/menu?menuId=71_110",
            binding.WelfareIncheon to "https://www.incheon.go.kr/welfare/WE010201",
            binding.elseIcon to "https://www.ablejob.co.kr/eduEvent/edu-event.php"
        )

        welfareMap.forEach { (imageView, url) ->
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
