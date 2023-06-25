package com.example.capstone.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_inside)
        binding.boardSettingicon.setOnClickListener {
            showDialog()
        }






        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)



    }
    private fun showDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("게시글 수정/삭제")

        val alertDialog = mBuilder.show()
        alertDialog.findViewById<Button>(R.id.editBtn)?.setOnClickListener {
            Toast.makeText(this,"수정 버튼을 눌렀습니다",Toast.LENGTH_LONG).show()
            val intent = Intent(this,BoardEditActivity::class.java)
            intent.putExtra("key",key) //edit으로 intent를 넘겨줄 때 key값을 같이 넘겨줄수 있도록 함
            //수정 페이지이기 때문에 수정버튼 을 눌렀을때 key값을 바탕으로 기존의 제목과 내용을 포함하고 있어야 함.
            startActivity(intent)
        }
        alertDialog.findViewById<Button>(R.id.removeBtn)?.setOnClickListener {

            FBRef.boardRef.child(key).removeValue() //board테이블의 대한 정보를 가져와서 key값을 찾아 remove해줌
            Toast.makeText(this,"삭제완료",Toast.LENGTH_SHORT).show()
            finish() //삭제하고 activity가 닫히도록 finish선언
        }
    }
    private fun getBoardData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try{
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)
                    Log.d(TAG,dataModel!!.title)

                    binding.titleArea.text = dataModel!!.title
                    binding.textAread.text = dataModel!!.content
                    binding.timeArea.text = dataModel!!.time
                    //try에서 에러가 나면 catch를 실행하라 예외처리

                }catch (e:java.lang.Exception){
                    Log.d(TAG,"삭제완료")
                }




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