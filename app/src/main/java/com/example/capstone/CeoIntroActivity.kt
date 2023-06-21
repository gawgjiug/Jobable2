package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.capstone.board.BoardInsideActivity
import com.example.capstone.board.BoardListLVAdapter
import com.example.capstone.board.BoardModel
import com.example.capstone.board.BoardWriteActivity
import com.example.capstone.databinding.ActivityCeoIntroBinding
import com.example.capstone.setting.SettingActivity
import com.example.capstone.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class CeoIntroActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCeoIntroBinding
    private val TAG = CeoIntroActivity::class.java.simpleName
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var boardRVAdapter: BoardListLVAdapter
    private val boardKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCeoIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //첫번째 방법은 listView에 있는 데이터 title content time 다 다른 액티비티로 전달해줘서 만들기

        //두번째 방법은 Firebase에 있는 board에 대한 데이터의 id를 기반으로 다시 데이터를 받아오는 방법.




        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardListView.adapter = boardRVAdapter
        binding.boardListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BoardInsideActivity::class.java)
            intent.putExtra("key",boardKeyList[position])
            startActivity(intent)



        }

        auth = Firebase.auth

        val setting_btn = binding.settingBtn
        setting_btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        val ceowrite_btn = binding.CeoWritebtn
        ceowrite_btn.setOnClickListener {
            val intent = Intent(this, BoardWriteActivity::class.java)
            startActivity(intent)
        }
        getFBBoardData()

    }

    //데이터 받아오기
    private fun getFBBoardData(){
        val postListener = object : ValueEventListener{
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
