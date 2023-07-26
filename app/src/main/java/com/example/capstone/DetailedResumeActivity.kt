package com.example.capstone
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.capstone.databinding.ActivityDetailedResumeBinding
import com.google.firebase.database.*

class DetailedResumeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailedResumeBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        // Get the selected user's ID from the previous activity
        val selectedUserId = intent.getStringExtra("selectedUserId")

        if (selectedUserId != null) {
            // Get the selected user's information from the database
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {
                        // Show the selected user's detailed resume information
                        showResumeInfo(user)
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
    }

    private fun showResumeInfo(user: User) {
        // Show the selected user's detailed resume information
        Glide.with(this)
            .load(user.profileImageUrl)
            .into(binding.imageViewProfile)

        binding.textViewName.text = "이름: ${user.name}"
        binding.textViewAge.text = "나이: ${user.age}"
        binding.textViewEducation.text = "학력: ${user.education}"
        binding.textViewExperience.text = "경력: ${user.experience}"
        // Add other resume information to the corresponding TextViews
        // ...
    }

    data class User(
        val userId: String,
        val name: String,
        val age: Int,
        val profileImageUrl: String,
        val education: String,
        val experience: String
        // Add other resume fields here based on your actual data structure
        // ...
    )
}
