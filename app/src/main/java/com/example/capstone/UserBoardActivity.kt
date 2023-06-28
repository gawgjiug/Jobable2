package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.example.capstone.board.BoardInsideActivity
import com.example.capstone.board.BoardListLVAdapter
import com.example.capstone.board.BoardModel
import com.example.capstone.databinding.ActivityUserBoardBinding
import com.example.capstone.setting.SettingActivity
import com.example.capstone.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.*

class UserBoardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityUserBoardBinding
    private val TAG = UserBoardActivity::class.java.simpleName
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var boardRVAdapter: BoardListLVAdapter
    private val boardKeyList = mutableListOf<String>()
    private lateinit var searchView: SearchView
    private val filteredBoardDataList = mutableListOf<BoardModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boardRVAdapter = BoardListLVAdapter(filteredBoardDataList)
        binding.userboardListView.adapter = boardRVAdapter

        searchView = findViewById(R.id.serachView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterBoardData(newText)
                return true
            }
        })

        auth = Firebase.auth

        val usersetting_btn = binding.UsersettingBtn
        usersetting_btn.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        binding.userboardListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, BoardInsideActivity::class.java)
            intent.putExtra("key", boardKeyList[position])
            startActivity(intent)
        }

        getFBBoardData()
    }

    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear()

                for (dataModel in dataSnapshot.children) {
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())
                }

                boardKeyList.reverse()
                boardDataList.reverse()
                filterBoardData(searchView.query.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }

        FBRef.boardRef.addValueEventListener(postListener)
    }

    private fun filterBoardData(query: String?) {
        filteredBoardDataList.clear()

        if (query.isNullOrEmpty()) {
            filteredBoardDataList.addAll(boardDataList)
        } else {
            val searchQuery = query.toLowerCase(Locale.getDefault())

            for (item in boardDataList) {
                if (item.title.toLowerCase(Locale.getDefault()).contains(searchQuery)) {
                    filteredBoardDataList.add(item)
                }
            }
        }

        boardRVAdapter.notifyDataSetChanged()
    }
}
