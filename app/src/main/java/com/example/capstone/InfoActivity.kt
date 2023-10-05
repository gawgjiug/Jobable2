package com.example.capstone;

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R

class InfoActivity : AppCompatActivity() {

    private lateinit var infoGyeongi: ImageView
    private lateinit var infoSeoul: ImageView
    private lateinit var infoEtc: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        infoGyeongi = findViewById(R.id.info_gyeongi)
        infoSeoul = findViewById(R.id.info_seoul)
        infoEtc = findViewById(R.id.info_etc)

        infoGyeongi.setOnClickListener {
            // 클릭 시 이미지 변경
            infoEtc.setImageResource(R.drawable.info_etc)
            infoGyeongi.setImageResource(R.drawable.info_click_gyeongi)
            infoSeoul.setImageResource(R.drawable.info_seoul)
        }

        infoSeoul.setOnClickListener{

            infoEtc.setImageResource(R.drawable.info_etc)
            infoGyeongi.setImageResource(R.drawable.info_gyeongi)
            infoSeoul.setImageResource(R.drawable.info_click_seoul)

        }
        infoEtc.setOnClickListener {
            infoEtc.setImageResource(R.drawable.info_click_etc)
            infoGyeongi.setImageResource(R.drawable.info_gyeongi)
            infoSeoul.setImageResource(R.drawable.info_seoul)
        }




    }
}
