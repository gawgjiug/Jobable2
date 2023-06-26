package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.capstone.board.BoardInsideActivity
import com.example.capstone.board.BoardListLVAdapter
import com.example.capstone.board.BoardModel
import com.example.capstone.databinding.ActivityCeoIntroBinding
import com.example.capstone.databinding.ActivityUserBoardBinding
import com.example.capstone.setting.SettingActivity
import com.example.capstone.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class UserBoardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUserBoardBinding
    private val TAG = UserBoardActivity::class.java.simpleName
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var boardRVAdapter: BoardListLVAdapter
    private val boardKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityUserBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.userboardListView.adapter = boardRVAdapter
        binding.userboardListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this,BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)
        }
        auth = Firebase.auth

        val usersetting_btn = binding.UsersettingBtn
        usersetting_btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }



        getFBBoardData()

    }

    private fun getFBBoardData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                boardDataList.clear() //기존 데이터 초기화 하고 다시 받아옴

                for (dataModel in dataSnapshot.children){
                    Log.d(TAG,dataModel.toString())
//                    dataModel.key
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                }
                boardKeyList.reverse()
                boardDataList.reverse() //글을 최신순으로 보여주기 위해 list를 뒤집어줌
                boardRVAdapter.notifyDataSetChanged()
                Log.d(TAG,boardDataList.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG,"loadPost:onCancellde",databaseError.toException())

            }

        }
        FBRef.boardRef.addValueEventListener(postListener)
    }



}