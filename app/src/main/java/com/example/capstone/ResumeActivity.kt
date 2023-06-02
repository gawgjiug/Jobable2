package com.example.capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import android.widget.Toast
import com.example.capstone.databinding.ActivityResumeBinding


class ResumeActivity : AppCompatActivity() {

    private lateinit var bindingRS: ActivityResumeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        println("------------1-------------")
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "이동 완료.", Toast.LENGTH_SHORT).show()
        bindingRS = ActivityResumeBinding.inflate(layoutInflater)
        setContentView(bindingRS.root)


    
        bindingRS.rsSubmit.setOnClickListener(View.OnClickListener{


            println("------------4-------------")
            Toast.makeText(this, "입력 완료.", Toast.LENGTH_SHORT).show()

            val name = bindingRS.rsName.text.toString()
            val phoneNum = bindingRS.rsPhoneNum.text.toString()
            val registrationNum = bindingRS.rsRegistrationNum.text.toString()
            val gender = bindingRS.rsGender.text.toString()
            val address = bindingRS.rsAddress.text.toString()
            val disabilityType = bindingRS.rsDisabilityType.text.toString()
            val education = bindingRS.rsEducation.text.toString()
            val whatEducation = bindingRS.rsWhatEducation.text.toString()

            val user = Resume(
                userId = "test11",
                name = name,
                phoneNum = phoneNum,
                residentRegistrationNum = registrationNum,
                gender = gender,
                adress = address,
                disabilityType = disabilityType,
                education = education,
                whatEducation = whatEducation
            )

            println("------------2-------------")
            println(user)
            println("------------3-------------")
            writeUserData(user)

        })

        bindingRS.rsEdit.setOnClickListener(View.OnClickListener{
            println("------------5-------------")
            Toast.makeText(this, "수정 완료.", Toast.LENGTH_SHORT).show()
        })
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
                Toast.makeText(this, "입력완료.", Toast.LENGTH_SHORT).show()
                //readUserData(user.userId)
            }
        }
    }



    /*
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
    readUserData("test")*/

}
