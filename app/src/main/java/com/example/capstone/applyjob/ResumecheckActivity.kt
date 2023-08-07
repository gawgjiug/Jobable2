package com.example.capstone.applyjob

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.capstone.R
import com.example.capstone.databinding.ActivityResumcheckBinding
import com.example.capstone.utils.FBAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResumecheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResumcheckBinding
    private lateinit var selectedUserId : String
    private lateinit var boardKey : String

    private lateinit var applyUsersAdapter: ResumeListAdapter
    private val applyUserList = ArrayList<ResumeData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumcheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // applyUsersAdapter 초기화
        applyUsersAdapter = ResumeListAdapter(this, R.layout.list_item_resume, applyUserList)

        // 리스트뷰에 applyUsersAdapter 할당
        binding.applyUserListView.adapter = applyUsersAdapter

        // 클릭 이벤트 처리
        binding.applyUserListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val selectedResumeData = applyUserList[i]
            val selectedUserId = selectedResumeData?.userid

            Log.d("ResumecheckActivity", "selectedUserId: $selectedUserId")

            val intent = Intent(this@ResumecheckActivity, ResumeDetailActivity::class.java)
            intent.putExtra("selectedUserId", selectedUserId)
            startActivity(intent)
        }



        selectedUserId = intent.getStringExtra("selectedUserId2").toString()
        boardKey = intent.getStringExtra("boardKey").toString()


        Log.d("ResumecheckActivity", "selectedUserId: $selectedUserId, boardKey: $boardKey")



        val myRef = FirebaseDatabase.getInstance().getReference("applyusers")
        val myUid = FBAuth.getUid()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val resumeDataList = mutableListOf<ResumeData>()
                for (dataModel in dataSnapshot.children) {
                    val uid = dataModel.key
                    val name = dataModel.child("name").getValue(String::class.java)
                    val sex = dataModel.child("sex").getValue(String::class.java)
                    val type = dataModel.child("type").getValue(String::class.java)
                    val profileImageURL = dataModel.child("profilePhotoURL").getValue(String::class.java)
                    val boardid = dataModel.child("boardid").getValue(String::class.java)
                    val introduce = dataModel.child("introduce").getValue(String::class.java)
                    val address = dataModel.child("address").getValue(String::class.java)
                    val birth = dataModel.child("birth").getValue(String::class.java)
                    val detail = dataModel.child("detail").getValue(String::class.java)
                    val userid = dataModel.child("userid").getValue(String::class.java)
                    val tel = dataModel.child("tel").getValue(String::class.java)





                    if (boardid == myUid && uid != null && name != null && sex != null && type != null && profileImageURL != null ) {
                        val resumeData = ResumeData(name, sex, type, profileImageURL,introduce,address,birth,detail,userid,tel)
                        resumeDataList.add(resumeData)
                        Log.d("ResumecheckActivity", "selectedUserId: $selectedUserId, uid: $uid")

                    }
                    else{
                        Log.d("ResumecheckActivity", "selectedUserId: $selectedUserId, uid: $uid")

                    }
                }

                val adapter = ResumeListAdapter(this@ResumecheckActivity,
                    R.layout.list_item_resume, resumeDataList)
                binding.applyUserListView.adapter = adapter

                applyUserList.clear()
                applyUserList.addAll(resumeDataList)

                // Notify the adapter that the data has changed
                applyUsersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ResumecheckActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)
    }

    override fun onResume() {
        super.onResume()

        // 데이터가 변경되었을 때 어댑터에 알려주기
        applyUsersAdapter.notifyDataSetChanged()
    }









}