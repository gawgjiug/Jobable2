package com.example.capstone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.applyjob.ResumeData
import com.example.capstone.applyjob.ResumeListAdapter
import com.example.capstone.databinding.ActivityResumcheckBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ResumecheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumcheckBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumcheckBinding.inflate(layoutInflater)
        setContentView(binding.root)







        val selectedUserId1 = intent.getStringExtra("selectedUserId2")
        val boardKey = intent.getStringExtra("boardKey")


        Log.d("ResumecheckActivity", "selectedUserId: $selectedUserId1, boardKey: $boardKey")



        val myRef = FirebaseDatabase.getInstance().getReference("resume")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val resumeDataList = mutableListOf<ResumeData>()
                for (dataModel in dataSnapshot.children) {
                    val uid = dataModel.key
                    val name = dataModel.child("name").getValue(String::class.java)
                    val sex = dataModel.child("sex").getValue(String::class.java)
                    val type = dataModel.child("type").getValue(String::class.java)
                    val profileImageURL = dataModel.child("profileImageURL").getValue(String::class.java)



                    if (selectedUserId1 == uid && uid != null && name != null && sex != null && type != null && profileImageURL != null ) {
                        val resumeData = ResumeData(name, sex, type, profileImageURL)
                        resumeDataList.add(resumeData)
                        Log.d("ResumecheckActivity", "selectedUserId: $selectedUserId1, uid: $uid")

                    }
                    else{
                        Log.d("ResumecheckActivity", "selectedUserId: $selectedUserId1, uid: $uid")

                    }
                }

                val adapter = ResumeListAdapter(this@ResumecheckActivity, R.layout.list_item_resume, resumeDataList)
                binding.applyUserListView.adapter = adapter

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ResumecheckActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)
    }
}