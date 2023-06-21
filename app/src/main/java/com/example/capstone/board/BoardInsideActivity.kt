package com.example.capstone.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.ActivityBoardInsideBinding
import com.example.capstone.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class BoardInsideActivity : AppCompatActivity() {

    private val TAG = BoardInsideActivity::class.java.simpleName

    private lateinit var binding : ActivityBoardInsideBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_inside)

//        val title = intent.getStringExtra("title").toString()
//        val content = intent.getStringExtra("content").toString()
//        val time = intent.getStringExtra("time").toString()
//
//
//        binding.titleArea.text = title
//        binding.textAread.text = content
//        binding.timeArea.text = time



        val key = intent.getStringExtra("key")

        getBoardData(key.toString())
        getImageData(key.toString())



    }
    private fun getBoardData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {



               val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                Log.d(TAG,dataModel!!.title)

                binding.titleArea.text = dataModel!!.title
                binding.textAread.text = dataModel!!.content
                binding.timeArea.text = dataModel!!.time


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG,"loadPost:onCancellde",databaseError.toException())

            }

        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }
    private fun getImageData(key :String){
        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".jpg")

        // ImageView in your Activity
        val imageViewFromFB = binding.getImageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            }else{

            }
        })
    }
}