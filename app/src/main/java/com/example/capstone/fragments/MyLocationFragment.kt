package com.example.capstone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.capstone.R
import com.example.capstone.databinding.FragmentMyLocationBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyLocationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding : FragmentMyLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_location,container,false)

        binding.resumeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myLocationFragment_to_resumeFragment)

        }
        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_myLocationFragment_to_homeFragment)
        }



        return binding.root
    }


}