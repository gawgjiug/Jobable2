package com.example.capstone.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore.Video
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.capstone.CeoIntroActivity
import com.example.capstone.R
import com.example.capstone.UserBoardActivity
import com.example.capstone.VideoActivity
import com.example.capstone.WelfareActivity
import com.example.capstone.board.BoardListLVAdapter
import com.example.capstone.board.BoardModel
import com.example.capstone.databinding.FragmentHomeBinding
import com.example.capstone.utils.FBRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : FragmentHomeBinding
    private var mediaplayer : MediaPlayer?= null
    private val TAG = CeoIntroActivity::class.java.simpleName
    private val boardDataList = mutableListOf<BoardModel>()
    private lateinit var boardRVAdapter: BoardListLVAdapter
    private val boardKeyList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        boardRVAdapter = BoardListLVAdapter(boardDataList)
        binding.boardList.adapter = boardRVAdapter


        mediaplayer = MediaPlayer.create(requireContext(),R.raw.search_mp3)

        binding.locationTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_myLocationFragment)
        }

        binding.resumeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_resumeFragment)
        }
        binding.homeVideo.setOnClickListener {
            val intent = Intent(binding.root.context, VideoActivity::class.java)
            startActivity(intent)
        }
        binding.homeInfo.setOnClickListener {
            val intent = Intent(binding.root.context, WelfareActivity::class.java)
            startActivity(intent)
        }
        binding.homeSearch.setOnClickListener {
            val intent = Intent(binding.root.context,UserBoardActivity::class.java)
            startActivity(intent)
        }
        binding.homeSearchMp3.setOnClickListener {

            val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.search_mp3)
            mediaPlayer.start()
        }
        binding.homeVideoMp3.setOnClickListener {

            val mediaPlayer = MediaPlayer.create(requireContext(), R.raw.quiz_mp3)
            mediaPlayer.start()
        }
        binding.homeInfoMp3.setOnClickListener {

            val mediaPlayer = MediaPlayer.create(requireContext(),R.raw.welfare_mp3)
            mediaPlayer.start()
        }

        getFBBoardData()


        return binding.root
        //
    }


    private fun getFBBoardData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                boardDataList.clear() //기존 데이터 초기화 하고 다시 받아옴

                for (dataModel in dataSnapshot.children){
                    Log.d(TAG,dataModel.toString())
                    val item = dataModel.getValue(BoardModel::class.java)
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString())

                }
                boardKeyList.reverse()
                boardDataList.reverse()
                boardRVAdapter.notifyDataSetChanged() // adapter 업데이트

                Log.d(TAG,boardDataList.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG,"loadPost:onCancelled",databaseError.toException())
            }
        }
        FBRef.boardRef.addValueEventListener(postListener)
    }
}