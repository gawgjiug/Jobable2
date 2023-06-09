package com.example.capstone.board

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.capstone.R
import com.example.capstone.databinding.ActivityBoardWriteBinding
import com.example.capstone.utils.FBAuth
import com.example.capstone.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardWriteBinding
    private var TAG = BoardWriteActivity::class.java.simpleName
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

            //board
            // -key
            //   -boardModel(title,content,uid,time)
            // 푸시 연습

            FBRef.boardRef
                .push() //랜덤한 key값 생성
                .setValue(BoardModel(title,content,uid,time))

            Toast.makeText(this,"게시글 입력 완료",Toast.LENGTH_LONG).show()

            finish()//게시글 입력이 완료되면 화면이 이전화면으로 돌아감

        }

    }
}