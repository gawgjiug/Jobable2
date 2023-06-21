package com.example.capstone.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.capstone.R
import com.example.capstone.databinding.ActivityBoardWriteBinding
import com.example.capstone.utils.FBAuth
import com.example.capstone.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardWriteBinding
    private var TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_write)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_board_write)


        binding.boardWritebtn.setOnClickListener {

            //제목과 내용 받아오기
            val title = binding.boardTitle.text.toString()
            val content = binding.boardContents.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()

            Log.d(TAG,title)
            Log.d(TAG,content)

            //이미지 이름에 대한 정보를 모르기 때문에
            //이미지 이름을 문서의 key값으로 설정하고 이미지에 대한 정보를 찾기 쉽게 해놓음.

            val key = FBRef.boardRef.push().key.toString()
            //게시글의 key값을 받아옴

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title,content,uid,time))

            Toast.makeText(this,"게시글 입력 완료",Toast.LENGTH_LONG).show()

            if(isImageUpload==true) {
                imageUpload(key)
            }



            finish()//게시글 입력이 완료되면 화면이 이전화면으로 돌아감

        }

        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,100)
            isImageUpload = true
        }

    }

    private fun imageUpload(key : String){

        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child(key + ".jpg")

        val imageView = binding.imageArea

        // Get the data from an ImageView as bytes
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 100 ){
            binding.imageArea.setImageURI(data?.data) //이미지를 선택하면 이미지를 가져와서 보여줌
        }
    }
}