package com.example.capstone.fragments

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore.Video
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.capstone.R
import com.example.capstone.UserBoardActivity
import com.example.capstone.VideoActivity
import com.example.capstone.WelfareActivity
import com.example.capstone.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var binding : FragmentHomeBinding
    private var mediaplayer : MediaPlayer?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mediaplayer = MediaPlayer.create(requireContext(),R.raw.search_mp3)

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
        binding.searchmp3.setOnClickListener {
            mediaplayer?.start()
        }
        binding.quizmp3.setOnClickListener {
            mediaplayer?.start()
        }
        binding.welfaremp3.setOnClickListener {
            mediaplayer?.start()
        }


        return binding.root
        //
    }


}