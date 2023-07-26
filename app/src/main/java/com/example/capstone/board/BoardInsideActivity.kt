package com.example.capstone.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.RegisterActivity
import com.example.capstone.ResumecheckActivity
import com.example.capstone.databinding.ActivityBoardInsideBinding
import com.example.capstone.databinding.CustomDialogBinding
import com.example.capstone.utils.FBAuth
import com.example.capstone.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding
    private lateinit var key: String
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_inside)
        binding.boardSettingicon.setOnClickListener {
            showDialog()
        }

        key = intent.getStringExtra("key").toString()
        database = FirebaseDatabase.getInstance().reference

        getBoardData(key)
        getImageData(key)
        // Check if the current user has a resume and show/hide the apply job button accordingly
        checkUserHasResume()

        // Set an onClickListener to apply job button
        binding.applyjobBtn.setOnClickListener {
            // Get the selected user's data from the database and pass it to ResumecheckActivity
            val selectedUserId = key // This key is used as the user's unique ID
            val intent = Intent(this, ResumecheckActivity::class.java)
            intent.putExtra("selectedUserId", selectedUserId)
            startActivity(intent)
        }
    }

    private fun showDialog() {
        // Create a custom dialog using custom_dialog.xml
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val dialogBinding = CustomDialogBinding.bind(mDialogView)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()

        dialogBinding.editBtn.setOnClickListener {
            alertDialog.dismiss()
            Toast.makeText(this, "수정 버튼을 눌렀습니다", Toast.LENGTH_LONG).show()
            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("key", key)
            startActivity(intent)
        }

        dialogBinding.removeBtn.setOnClickListener {
            alertDialog.dismiss()
            FBRef.boardRef.child(key).removeValue()
            Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    val userdataModel = dataSnapshot.getValue(RegisterActivity.User::class.java)
                    Log.d(TAG, dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textAread.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time

                    val myUid = FBAuth.getUid()
                    val writerUid = dataModel.uid

                    val userQuery: Query = database.child("users").orderByChild("uid").equalTo(myUid)
                    if (myUid.equals(writerUid)) {
                        binding.boardSettingicon.isVisible = true
                    } else {
                        // Handle the case where the current user is not the author of the post
                    }

                    // Try to check if the user has a resume and show/hide the apply job button
                    checkUserHasResume()

                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "삭제완료")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancellde", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun checkUserHasResume() {
        val myUid = FBAuth.getUid()
        if (myUid != null) {
            val resumeReference = FirebaseDatabase.getInstance().reference.child("resume").child(myUid)
            resumeReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // If the user has a resume, show the apply job button
                        binding.applyjobBtn.isVisible = true
                        // Set an onClickListener to apply job button
                        binding.applyjobBtn.setOnClickListener {
                            // Get the selected user's data from the database and pass it to ResumecheckActivity
                            val selectedUserId = key // This key is used as the user's unique ID

                            intent.putExtra("selectedUserId", selectedUserId)

                        }
                    } else {
                        // If the user does not have a resume, hide the apply job button
                        binding.applyjobBtn.isVisible = false
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "checkUserHasResume:onCancelled", databaseError.toException())
                }
            })
        } else {
            // Handle the case where the user is not logged in
            // You may want to hide the apply job button or redirect the user to login page, etc.
            binding.applyjobBtn.isVisible = false
        }
    }

    private fun getImageData(key :String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child("$key.jpg")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{
                // Handle the failure to load the image
            }
        })
    }
}