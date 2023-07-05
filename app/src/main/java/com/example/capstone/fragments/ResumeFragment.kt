package com.example.capstone.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.capstone.R
import com.example.capstone.databinding.FragmentResumeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResumeFragment : Fragment() {

    private lateinit var binding: FragmentResumeBinding
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth

    private var resumewrite = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = requireActivity().getSharedPreferences("ResumeData", 0)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_resume, container, false)

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_resumeFragment_to_homeFragment)
        }

        binding.resumeWrite.setOnClickListener {
            val name = binding.resumeName.text.toString()
            val address = binding.resumeAddress.text.toString()
            val detail = binding.resumeDetail.text.toString()
            val birth = binding.resumeBirth.text.toString()
            val introduce = binding.resumeIntroduce.text.toString()
            val sex = binding.resumeSex.text.toString()
            val type = binding.resumeType.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                resumewrite = false
            }
            if (address.isEmpty()) {
                Toast.makeText(requireContext(), "주소를 입력해주세요", Toast.LENGTH_SHORT).show()
                resumewrite = false
            }
            if (detail.isEmpty()) {
                Toast.makeText(requireContext(), "상세주소를 입력해주세요", Toast.LENGTH_SHORT).show()
                resumewrite = false
            }
            if (sex.isEmpty()) {
                Toast.makeText(requireContext(), "성별을 입력해주세요", Toast.LENGTH_SHORT).show()
                resumewrite = false
            }
            if (birth.isEmpty()) {
                Toast.makeText(requireContext(), "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show()
                resumewrite = false
            }
            if (type.isEmpty()) {
                Toast.makeText(requireContext(), "장애 유형을 입력해주세요", Toast.LENGTH_SHORT).show()
                resumewrite = false
            }
            if (introduce.isEmpty()) {
                Toast.makeText(requireContext(), "소개를 입력해주세요", Toast.LENGTH_SHORT).show()
                resumewrite = false
            }

            if (resumewrite) {
                val resume = Resume(name, address, detail, birth, introduce, sex, type)
                val resumeRef = database.child("resume").child(auth.currentUser?.uid ?: "")
                resumeRef.setValue(resume)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "이력서가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                        // 이력서가 성공적으로 저장되면 입력한 데이터를 프래그먼트에 표시
                        binding.resumeName.setText(name)
                        binding.resumeBirth.setText(birth)
                        binding.resumeAddress.setText(address)
                        binding.resumeDetail.setText(detail)
                        binding.resumeSex.setText(sex)
                        binding.resumeType.setText(type)
                        binding.resumeIntroduce.setText(introduce)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // 저장된 이력서 정보 불러오기
        val resumeRef = database.child("resume").child(auth.currentUser?.uid ?: "")
        resumeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val resume = snapshot.getValue(Resume::class.java)
                    resume?.let {
                        binding.resumeName.setText(it.name)
                        binding.resumeBirth.setText(it.birth)
                        binding.resumeAddress.setText(it.address)
                        binding.resumeDetail.setText(it.detail)
                        binding.resumeSex.setText(it.sex)
                        binding.resumeType.setText(it.type)
                        binding.resumeIntroduce.setText(it.introduce)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return binding.root
    }

    data class Resume(
        val name: String = "",
        val address: String = "",
        val detail: String = "",
        val birth: String = "",
        val introduce: String = "",
        val sex: String = "",
        val type: String = ""
    )
}
