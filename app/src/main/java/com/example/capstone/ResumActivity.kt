package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ResumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Region이 US일 경우
        //val database = Firebase.database
        //val myRef = database.getReference("message")  예시1
        //myRef.setValue("Hello, World!")

        //구조체 선언
        data class Resume(var userId: String,  //회원의id
                          var name: String,    //회원 이름
                          var phoneNum: String,  //회원 전화번호
                          var residentRegistrationNum: String, //주민번호
                          var gender: String,  //성별
                          var adress: String,  //주소
                          var disabilityType: String ,  //장애유형
                          var education: String,    //교육여부
                          var whatEducation: String)  //무슨교육인지

        fun writeUserData(user :Resume) {
            val databaseReference: DatabaseReference = Firebase.database.reference
            val userRef = databaseReference.child("id_resume").child(user.userId)
            val userMap = mapOf(
                "username" to user.name,
                "phoneNum" to user.phoneNum,
                "residentRegistrationNum" to user.residentRegistrationNum,
                "gender" to user.gender,
                "adress" to user.adress,
                "disabilityType" to user.disabilityType,
                "education" to user.education,
                "whatEducation" to user.whatEducation
            )
            userRef.setValue(userMap)
        }

        /////////////////////이부분 나중에 주석풀고edit박스 변수명에 맞춰서 넣으면 됨///////////////////////////////////////////////////
        /*
         fun saveResumeData() {
         val userId = edId.text.toString()
         val name = edName.text.toString()
         val phoneNum = edPhoneNum.text.toString()
         val residentRegistrationNum = edRegistrationNum.text.toString()
         val gender = edGender.text.toString()
         val address = edAddress.text.toString()
         val disabilityType = edDisabilityType.text.toString()
         val education = edEducation.text.toString()
         val whatEducation = edWhatEducation.text.toString()
         val user = Resume(
            userId,
            name,
            phoneNum,
            residentRegistrationNum,
            gender,
            address,
            disabilityType,
            education,
            whatEducation)
            writeUserData(user
            )
         }
         saveResumeData()
        */
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        val user1 = Resume("kim","김민수","01087654321","010101-4444444","여","경기도 수원시 어디","발달장애","없음","")
        val user2 = Resume("Lee","이철수","01023456789","990505-2222222","여","경기도 화성시","발달장애","있음","제빵")
        val user3 = Resume("Park","박영희","01011112222","030303-3333333","남","경기도 오산시","발달장애","있음","목공품")
        val user4 = Resume("ba","바둑이","01044445555","981111-1111111","남","경기도 오산시","발달장애","없음","")


        //4명의 유저의 이력서추가
         writeUserData(user1)
         writeUserData(user2)
         writeUserData(user3)
         writeUserData(user4)

        fun readUserData(userId: String) {
            val databaseReference2: DatabaseReference = Firebase.database.reference
            val userRef2 = databaseReference2.child("users").child(userId)
            userRef2.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue()
                        println("------------------------------------------------------------------------------------------------")
                        println(userData)
                        println("------------------------------------------------------------------------------------------------")
                    } else {
                        println("No data available")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.toException())
                }
            })
        }
        readUserData("test")
    }
}
//////////dddd//////