package com.example.capstone

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.capstone.databinding.ActivityResumcheckBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResumecheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumcheckBinding
    private val usersList = mutableListOf<User>()
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumcheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Get the selected user's ID from the previous activity
        val selectedUserId = intent.getStringExtra("selectedUserId")

        if (selectedUserId != null) {
            // Get the selected user's information from the database
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        // Add the selected user's information to the list
                        usersList.add(user)
                        // Update the ListView with user information
                        updateListView()
                        // Show the selected user's profile image, name, and age
                        showProfileInfo(user)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            }

            // Get the reference to the selected user's information in the database
            val userRef = database.child("users").child(selectedUserId)
            // Register the listener to get the selected user's information
            userRef.addListenerForSingleValueEvent(userListener)
        }

        // Set an onClickListener for the ListView items
        binding.listViewUsers.setOnItemClickListener { _, _, position, _ ->
            // Get the selected user's information
            val selectedUser = usersList[position]
            // Start the DetailedResumeActivity and pass the selected user's ID
            val intent = Intent(this, DetailedResumeActivity::class.java)
            intent.putExtra("selectedUserId", selectedUser.userId)
            startActivity(intent)
        }
    }

    private fun updateListView() {
        // Update the ListView with the user information
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usersList)
        binding.listViewUsers.adapter = adapter
    }

    private fun showProfileInfo(user: User) {
        // Show the selected user's profile image, name, and age
        Glide.with(this)
            .load(user.profileImageUrl)
            .into(binding.imageViewProfile)

        binding.textViewName.text = "이름: ${user.name}"
        binding.textViewAge.text = "나이: ${user.age}"
    }

    // User data class
    data class User(val userId: String, val name: String, val age: Int, val profileImageUrl: String) {
        override fun toString(): String {
            return name
        }
    }
}
