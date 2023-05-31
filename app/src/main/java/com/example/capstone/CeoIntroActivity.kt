package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.capstone.board.BoardWriteActivity
import com.example.capstone.databinding.ActivityCeoIntroBinding
import com.example.capstone.setting.SettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CeoIntroActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding:ActivityCeoIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ceo_intro)

        auth = Firebase.auth

        val setting_btn = findViewById<ImageView>(R.id.setting_btn)
        setting_btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

        }
        val ceowrite_btn = findViewById<ImageView>(R.id.Ceo_writebtn)
        ceowrite_btn.setOnClickListener {
            val intent = Intent(this,BoardWriteActivity::class.java)
            startActivity(intent)
        }



    }
}