package com.example.capstone.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.ActivityBoardEditBinding
import com.example.capstone.utils.FBAuth
import com.example.capstone.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardEditActivity : AppCompatActivity() {
    private lateinit var key: String
    private lateinit var binding : ActivityBoardEditBinding
    private val TAG = BoardEditActivity::class.java.simpleName
    private lateinit var writerUid :String


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_edit)
        key = intent.getStringExtra("key").toString()  //edit 페이지에서도 마찬가지로 key값을 받아줘야 함.
        getBoardData(key)
        getImageData(key)
        binding.editWritebtn.setOnClickListener {
            editBoardData(key)
        }



    }
    private fun editBoardData(key:String){
        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(binding.boardTitle.text.toString(), binding.boardContents.text.toString(),writerUid,FBAuth.getTime()
            ))
        Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show()
        finish()
    }



    private fun getImageData(key :String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".jpg")

        // ImageView in your Activity
        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{

            }
        })
    }

    private fun getBoardData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                binding.boardTitle.setText(dataModel?.title)
                binding.boardContents.setText(dataModel?.content)
                writerUid = dataModel!!.uid

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG,"loadPost:onCancellde",databaseError.toException())

            }

        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
        }
    }


