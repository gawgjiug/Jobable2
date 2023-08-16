package com.example.capstone.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.applyjob.ResumecheckActivity
import com.example.capstone.databinding.ActivitySettingBinding
import com.example.capstone.utils.FBAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var ceouserRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myUid = FBAuth.getUid()

        auth = Firebase.auth
        ceouserRef = FirebaseDatabase.getInstance().reference.child("Ceousers")

        val logoutBtn: Button = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val resumechk: ImageView = findViewById(R.id.resume_search)
        resumechk.setOnClickListener {
            val intent = Intent(this, ResumecheckActivity::class.java)
            startActivity(intent)
        }

        // Check if the user's data exists in the "ceouser" table
        ceouserRef.child(myUid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val isUserExists = dataSnapshot.exists()
                binding.resumeSearch.isVisible = isUserExists
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur during the database operation
                // You may want to show an error message or take appropriate action
            }
        })
    }
}

