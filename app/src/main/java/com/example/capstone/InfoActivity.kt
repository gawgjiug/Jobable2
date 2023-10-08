package com.example.capstone

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R
import com.example.capstone.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    // 이미지뷰 ID 목록
    private val seoulImageViewIds = listOf(
        R.id.seoul_1, R.id.seoul_2, R.id.seoul_3,
        R.id.seoul_4, R.id.seoul_5, R.id.seoul_6,
        R.id.seoul_7, R.id.seoul_8, R.id.seoul_9
    )
    private val gyeongiImageViewIds = listOf(
        R.id.gyeongi_1, R.id.gyeongi_2, R.id.gyeongi_3,
        R.id.gyeongi_4, R.id.gyeongi_5, R.id.gyeongi_6,
        R.id.gyeongi_7, R.id.gyeongi_8, R.id.gyeongi_9
    )

    private lateinit var infoGyeongi: ImageView
    private lateinit var infoSeoul: ImageView
    private lateinit var infoEtc: ImageView
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        infoGyeongi = findViewById(R.id.info_gyeongi)
        infoSeoul = findViewById(R.id.info_seoul)
        infoEtc = findViewById(R.id.info_etc)

        // 모든 서울 이미지뷰 초기화
        val seoulImageViews = seoulImageViewIds.map { findViewById<ImageView>(it) }
        // 모든 경기 이미지뷰 초기화
        val gyeongiImageViews = gyeongiImageViewIds.map { findViewById<ImageView>(it) }

        val calculator = findViewById<ImageView>(R.id.info_calculator)

        calculator.setOnClickListener {
            val intent = Intent(this,Info_Calculator_Activity::class.java)
            startActivity(intent)
        }


        val seoulUrl = mapOf(
            binding.seoul1 to "http://www.sbcil.org/child/sub/activity/",
            binding.seoul2 to "https://www.gwanak.go.kr/site/gwanak/04/10404050100002016051205.jsp",
            binding.seoul3 to "https://www.guro.go.kr/www/contents.do?key=2255&",
            binding.seoul4 to "http://selfhelp.or.kr/bbs/board.php?bo_table=b03_03",
            binding.seoul5 to "https://www.bokjiro.go.kr/ssis-tbu/twataa/wlfareInfo/moveTWAT52011M.do?wlfareInfoId=WLF00002474",
            binding.seoul6 to "https://www.mapo.go.kr/site/main/content/mapo05010604",
            binding.seoul7 to "https://www.gangnam.go.kr/etc/information/disability_support/index1.html",
            binding.seoul8 to "http://www.gangbukrc.or.kr/menu/?menu_str=2020",
            binding.seoul9 to "http://www.yongsanedu.or.kr/sub2_1.php",
            )

        val gyeongiUrl = mapOf(
            binding.gyeongi1 to "https://www.suwonrehab.or.kr/bbs/content.php?co_id=bussiness9",
            binding.gyeongi2 to "https://www.happyseed.or.kr/utilize/?act=sub8",
            binding.gyeongi3 to "http://gpil.co.kr/as_intro",
            binding.gyeongi4 to "http://www.hanamrehab.or.kr/m/business/intearated.php",
            binding.gyeongi5 to "https://www.a-sak.or.kr/business/service_activity.php",
            binding.gyeongi6 to "https://www.nyj.go.kr/main/976",
            binding.gyeongi7 to "https://www.seongnam.go.kr/city/1000234/10137/contents.do",
            binding.gyeongi8 to "http://bcmedcoop.org/bbs/content.php?co_id=support1",
            binding.gyeongi9 to "https://jangbokmoa.incheon.go.kr/contents.do?menuno=37"
        )

        seoulUrl.forEach{
            (imageView,url) -> imageView.setOnClickListener {
                showWebView(url)
        }
        }


        gyeongiUrl.forEach{
                (imageView,url) -> imageView.setOnClickListener {
            showWebView(url)
        }
        }


        infoGyeongi.setOnClickListener {
            updateImageVisibility(gyeongiImageViews, true)
            updateImageVisibility(seoulImageViews, false)
            updateInfoButtons(
                R.drawable.info_click_gyeongi,
                R.drawable.info_seoul,
                R.drawable.info_etc
            )
        }

        infoSeoul.setOnClickListener {

            updateImageVisibility(gyeongiImageViews,false)
            updateImageVisibility(seoulImageViews,true)

            updateInfoButtons(
                R.drawable.info_gyeongi,            //경기자리
                R.drawable.info_click_seoul,         //서울자리
                R.drawable.info_etc                 //지방 자리
            )
        }

        infoEtc.setOnClickListener {
            updateInfoButtons(
                R.drawable.info_gyeongi,
                R.drawable.info_seoul,
                R.drawable.info_click_etc
            )
        }
    }

    private fun updateInfoButtons(
        gyeongiImageResId: Int,
        seoulImageResId: Int,
        etcImageResId: Int
    ) {
        infoGyeongi.setImageResource(gyeongiImageResId)
        infoSeoul.setImageResource(seoulImageResId)
        infoEtc.setImageResource(etcImageResId)
    }

    private fun updateImageVisibility(imageViews: List<ImageView>, isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        imageViews.forEach { it.visibility = visibility }
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
