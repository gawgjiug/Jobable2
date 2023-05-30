package com.example.capstone

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.capstone.databinding.ActivityIntroBinding
import com.example.capstone.databinding.ActivityMainBinding
import com.example.capstone.setting.SettingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        auth = Firebase.auth

        val setting_btn = findViewById<ImageView>(R.id.setting_btn)
        setting_btn.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            startActivity(intent)

        }





    }
}