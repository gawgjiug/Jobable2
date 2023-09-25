package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.capstone.databinding.ActivityMainBinding
import com.example.capstone.utils.FBAuth
import com.example.capstone.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        binding.btLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btRegister.setOnClickListener {
            if(binding.checkCeoregi.isChecked){
                val intent =Intent(this,CeoregisterActivity::class.java)
                startActivity(intent)
            }
            else{
                val intent = Intent(this,RegisterActivity::class.java)
                startActivity(intent)
            }

        }



    }
}
