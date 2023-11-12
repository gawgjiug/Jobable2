package com.example.capstone.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private lateinit var binding: ActivityBoardWriteBinding
    private var TAG = BoardWriteActivity::class.java.simpleName

    private var isImageUpload = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_write)

        val dayList = listOf(
            " 주 5일제"," 주 4일제", " 주 3일제"," 주 2일제", " 주 1일제", " 만나서 협의"
        )
        val dayadapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,dayList)
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.writeDaySpinner.adapter = dayadapter

        binding.writeDaySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        val selectedItem = dayList[position]
                        Toast.makeText(
                            this@BoardWriteActivity, selectedItem, Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        val categoryList = listOf(
            "직무를 선택하세요", "사무보조", "편의점 직원", "도서관 사서", "의류 매장 보조", "빵집 보조"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.boardWriteSpinner.adapter = adapter

        binding.boardWriteSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        val selectedItem = categoryList[position]
                        Toast.makeText(
                            this@BoardWriteActivity, selectedItem, Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        binding.boardWritebtn.setOnClickListener {
            val title = binding.boardTitle.text.toString()
            val content = binding.boardContents.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            val job = binding.boardWriteSpinner.selectedItem as String // 사용자가 선택한 직무
            val pay = binding.boardWritePay.text.toString()
            val day = binding.writeDaySpinner.selectedItem as String
            val worktime = binding.boardWriteTime.text.toString()


            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(title, content, uid, time, job,pay,day,worktime)) // 'job' 필드 추가

            Toast.makeText(this, "게시글 입력 완료", Toast.LENGTH_LONG).show()

            if (isImageUpload == true) {
                imageUpload(key)
            }

            finish()
        }


        binding.imageArea.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
            isImageUpload = true
        }
    }

    private fun imageUpload(key: String) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val mountainsRef = storageRef.child("$key.jpg")

        val imageView = binding.imageArea

        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            binding.imageArea.setImageURI(data?.data)
        }
    }
}
