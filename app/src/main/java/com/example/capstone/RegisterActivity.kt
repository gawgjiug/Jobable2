package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.capstone.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btJoin.setOnClickListener {
            var isGoToJoin = true
            val email = binding.regiId.text.toString()
            val password1 = binding.regiPw1.text.toString();
            val password2 = binding.regiPwcheck.text.toString();
            val name = binding.regiName.text.toString();
            if(name.isEmpty()){
                Toast.makeText(this@RegisterActivity,"이름을 입력해주세요",Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            //값이 비어있는지 확인
            if (email.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            if(password1.isEmpty()){
                Toast.makeText(this@RegisterActivity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            if(password2.isEmpty()){
                Toast.makeText(this@RegisterActivity, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }
            //비밀번호 2개가 같은지 확인
            if(!password1.equals(password2)){
                Toast.makeText(this@RegisterActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            } //비밀번호가 6자 이상인지
            if(password1.length <6){
                Toast.makeText(this@RegisterActivity, "비밀번호를 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                isGoToJoin = false
            }

            if(isGoToJoin){
                auth.createUserWithEmailAndPassword(email,password1)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // 사용자 정보를 Realtime Database에 저장
                            val user = auth.currentUser
                            val userId = user?.uid
                            userId?.let {

                                val userInfo = User(name, email, password1 )
                                database.child("users").child(userId).setValue(userInfo)
                            }

                            Toast.makeText(this,"성공",Toast.LENGTH_LONG).show()
                            val intent = Intent(this,IntroActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this,"실패",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    // 사용자 정보를 저장할 데이터 클래스
    data class User(
        val name: String = "",
        val email: String = "",
        val pw: String = ""
    )
}
