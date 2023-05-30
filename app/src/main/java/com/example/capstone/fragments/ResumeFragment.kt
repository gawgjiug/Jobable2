package com.example.capstone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.capstone.R
import com.example.capstone.databinding.FragmentResumeBinding


class ResumeFragment : Fragment() {

    private lateinit var binding : FragmentResumeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_resume,container,false)
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_resumeFragment_to_homeFragment)
        }


        return binding.root
    }


}