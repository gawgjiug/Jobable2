package com.example.capstone.applyjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.ActivityResumeDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResumeDetailActivity : AppCompatActivity() {
    // ... (other existing code)
    private lateinit var binding : ActivityResumeDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the selectedUserId from the intent extras
        val selectedUserId = intent.getStringExtra("selectedUserId")

        // Query the Firebase Realtime Database to get the user data using the selectedUserId
        val myRef = selectedUserId?.let {
            FirebaseDatabase.getInstance().getReference("applyusers").child(
                it
            )
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the user data from dataSnapshot
                val name = dataSnapshot.child("name").getValue(String::class.java)
                val sex = dataSnapshot.child("sex").getValue(String::class.java)
                val type = dataSnapshot.child("type").getValue(String::class.java)
                val profileImageURL = dataSnapshot.child("profilePhotoURL").getValue(String::class.java)
                val address = dataSnapshot.child("address").getValue(String::class.java)
                val introduce = dataSnapshot.child("introduce").getValue(String::class.java)
                val birth = dataSnapshot.child("birth").getValue(String::class.java)
                val detail = dataSnapshot.child("detail").getValue(String::class.java)
                val tel = dataSnapshot.child("tel").getValue(String::class.java)

                // Populate the UI components with the retrieved data
                binding.Detailsex.text = sex
                binding.Detailname.text = name
                binding.Detailaddress.text = address
                binding.Detailbirth.text=birth
                binding.Detailaddress2.text = detail
                binding.Detailintroduce.text = introduce
                binding.Detailtype.text = type
                binding.Detailtel.text = tel

                // ... (populate other UI components similarly)

                // Load profile image using Glide or any other image loading library
                Glide.with(this@ResumeDetailActivity)
                    .load(profileImageURL)
                    .into(binding.DetailProfile)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ResumeDetailActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef?.addListenerForSingleValueEvent(postListener)
    }
}