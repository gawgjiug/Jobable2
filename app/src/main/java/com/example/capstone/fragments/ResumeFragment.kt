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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class ResumeFragment : Fragment() {

    private lateinit var binding : FragmentResumeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_resume,container,false)
        auth = Firebase.auth
        val user = auth.currentUser
        val userId = user?.uid

        val databaseReference3: DatabaseReference = Firebase.database.reference
        val list = listOf("username","phoneNum","residentRegistrationNum","gender","address","disabilityType","education","whatEducation")
        for(i in list){
            val userRef3 = databaseReference3.child("id_resume").child(userId.toString()).child(i)
            userRef3.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val text = snapshot.getValue(String::class.java)

                            ///////////username setText////////////////////////////////////
                        if(i==list[0]) {
                            if (text != null) {
                                println("Username: $text")
                                binding.rsName.setText("$text")
                            } else {
                                println("Username not found")
                                binding.rsName.setText("")
                            }
                        }
                            ///////////phoneNum setText/////////////////////////////////////
                        if(i==list[1]) {
                            if (text != null && i == list[1]) {
                                println("phoneNum: $text")
                                binding.rsPhoneNum.setText("$text")
                            } else {
                                println("rsPhoneNum not found")
                                binding.rsPhoneNum.setText("")
                            }
                        }
                            ///////////residentRegistrationNum setText/////////////////////////////////////
                        if(i==list[2]) {
                            if (text != null && i == list[2]) {
                                println("residentRegistrationNum: $text")
                                binding.rsRegistrationNum.setText("$text")
                            } else {
                                println("residentRegistrationNum not found")
                                binding.rsRegistrationNum.setText("")
                            }
                        }
                            ///////////gender setText/////////////////////////////////////
                        if(i==list[3]) {
                            if (text != null && i == list[3]) {
                                println("gender: $text")
                                binding.rsGender.setText("$text")
                            } else {
                                println("gender not found")
                                binding.rsGender.setText("")
                            }
                        }
                            ///////////address setText/////////////////////////////////////
                        if(i==list[4]) {
                            if (text != null && i == list[4]) {
                                println("address: $text")
                                binding.rsAddress.setText("$text")
                            } else {
                                println("address not found")
                                binding.rsAddress.setText("")
                            }
                        }
                            ///////////disabilityType setText/////////////////////////////////////
                        if(i==list[5]) {
                            if (text != null && i == list[5]) {
                                println("disabilityType: $text")
                                binding.rsDisabilityType.setText("$text")
                            } else {
                                println("disabilityType not found")
                                binding.rsDisabilityType.setText("")
                            }
                        }
                            ///////////education setText/////////////////////////////////////
                        if(i==list[6]) {
                            if (text != null && i == list[6]) {
                                println("education: $text")
                                binding.rsEducation.setText("$text")
                            } else {
                                println("education not found")
                                binding.rsEducation.setText("")
                            }
                        }
                            ///////////whatEducation setText/////////////////////////////////////
                        if(i==list[7]) {
                            if (text != null && i == list[7]) {
                                println("whatEducation: $text")
                                binding.rsWhatEducation.setText("$text")
                            } else {
                                println("whatEducation not found")
                                binding.rsWhatEducation.setText("")
                            }
                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.toException())
                }
            })
        }



        ///////////////////////////////////////////////////////////////////////////////////////
        binding.homeTap.setOnClickListener{
            it.findNavController().navigate(R.id.action_resumeFragment_to_homeFragment)
        }

        binding.rsSubmit.setOnClickListener{


            val name = binding.rsName.text.toString()
            val phoneNum = binding.rsPhoneNum.text.toString()
            val registrationNum = binding.rsRegistrationNum.text.toString()
            val gender = binding.rsGender.text.toString()
            val address = binding.rsAddress.text.toString()
            val disabilityType = binding.rsDisabilityType.text.toString()
            val education = binding.rsEducation.text.toString()
            val whatEducation = binding.rsWhatEducation.text.toString()


            val users = Resume(
                userId = userId.toString(),
                name = name,
                phoneNum = phoneNum,
                residentRegistrationNum = registrationNum,
                gender = gender,
                adress = address,
                disabilityType = disabilityType,
                education = education,
                whatEducation = whatEducation
            )

            writeUserData(users)
            //readUserData(userId.toString())
        }



        return binding.root

    }
    data class Resume(
        var userId: String,
        var name: String,
        var phoneNum: String,
        var residentRegistrationNum: String,
        var gender: String,
        var adress: String,
        var disabilityType: String,
        var education: String,
        var whatEducation: String
    )

    fun writeUserData(user: Resume) {
        val databaseReference: DatabaseReference = Firebase.database.reference
        val userRef = databaseReference.child("id_resume").child(user.userId)
        val userMap = mapOf(
            "username" to user.name,
            "phoneNum" to user.phoneNum,
            "residentRegistrationNum" to user.residentRegistrationNum,
            "gender" to user.gender,
            "address" to user.adress,
            "disabilityType" to user.disabilityType,
            "education" to user.education,
            "whatEducation" to user.whatEducation
        )
        userRef.setValue(userMap) { error, _ ->
            if (error != null) {
                // 데이터 쓰기 중에 오류가 발생한 경우 처리
                println("Error writing data to Firebase: $error")
            } else {
                // 데이터 쓰기가 완료된 후에 실행되는 부분
                println("Data written to Firebase successfully")

                //readUserData(user.userId)
            }
        }
    }
    /*
    fun readUserData(userId: String) {
        val databaseReference2: DatabaseReference = Firebase.database.reference
        val userRef2 = databaseReference2.child("id_resume").child(userId).child("username")

        userRef2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.getValue(String::class.java)
                    if (username != null) {
                        println("Username: $username")
                    } else {
                        println("Username not found")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.toException())
            }
        })

    }*/


}
