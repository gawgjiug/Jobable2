package com.example.capstone.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

        //day spinner
        val dayList = listOf(
            " 주 5일제"," 주 4일제", " 주 3일제"," 주 2일제", " 주 1일제", " 만나서 협의"
        )
        val dayadapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,dayList)
        dayadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.editDaySpinner.adapter = dayadapter

        binding.editDaySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        val selectedItem = dayList[position]
                        Toast.makeText(
                            this@BoardEditActivity, selectedItem, Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        //day spinner



        //Spinner 부분 코드

        val categoryList = listOf(
            "직무를 선택하세요", "사무보조", "편의점", "도서관 사서", "의류 매장 보조", "빵집 보조"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.boardEditSpinner.adapter = adapter

        binding.boardEditSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    if (position != 0) {
                        val selectedItem = categoryList[position]
                        Toast.makeText(
                            this@BoardEditActivity, selectedItem, Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        //Spinner 부분 코드






    }
    private fun editBoardData(key:String){
        FBRef.boardRef
            .child(key)
            .setValue(BoardModel(binding.boardTitle.text.toString(),
                binding.boardContents.text.toString(),
                writerUid,FBAuth.getTime(),
                binding.boardEditSpinner.selectedItem as String,
                binding.boardEditPay.text.toString(),
                binding.editDaySpinner.selectedItem as String,
                binding.boardEditTime.text.toString()
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
                binding.boardEditPay.setText(dataModel?.pay)
                binding.boardEditTime.setText(dataModel?.worktime)

            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG,"loadPost:onCancellde",databaseError.toException())

            }

        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
        }
    }


