package com.example.capstone.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.capstone.R
import com.example.capstone.databinding.FragmentResumeBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.IOException

class ResumeFragment : Fragment() {

    private lateinit var binding: FragmentResumeBinding
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private val PICK_IMAGE_REQUEST = 1

    private var resumewrite = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        sharedPreferences = requireActivity().getSharedPreferences("ResumeData", 0)
        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
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
                val user = auth.currentUser
                val userId = user?.uid
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

        binding.resumeProfile.setOnClickListener {
            openGallery()
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

        // Load and display the profile image if it exists
        val profileImageRef = storageReference.child("images/${auth.currentUser?.uid}")
        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            // Use Glide to load the image from the URI and set it to the ImageView
            Glide.with(requireContext())
                .load(uri)
                .into(binding.resumeProfile)
        }.addOnFailureListener {
            // Handle the failure to load the profile image
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "사진 선택"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri = data.data!!
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, imageUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                }
                uploadImage(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val imageRef = storageReference.child("images/${auth.currentUser?.uid}")
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnSuccessListener(OnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageURL = uri.toString()
                // Save the imageURL in the database
                database.child("resume").child(auth.currentUser?.uid ?: "").child("profileImageURL")
                    .setValue(imageURL)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "프로필 사진이 업로드되었습니다.", Toast.LENGTH_SHORT).show()
                        // Load and display the uploaded profile image
                        Glide.with(requireContext())
                            .load(imageURL)
                            .into(binding.resumeProfile)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "프로필 사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
            }
        }).addOnFailureListener(OnFailureListener {
            Toast.makeText(requireContext(), "프로필 사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show()
        })
    }

    data class Resume(
        val name: String = "",
        val address: String = "",
        val detail: String = "",
        val birth: String = "",
        val introduce: String = "",
        val sex: String = "",
        val type: String = "",
        val profileImageURL : String = " "

    )
}
