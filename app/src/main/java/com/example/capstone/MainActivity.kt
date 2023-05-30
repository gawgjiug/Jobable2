package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.capstone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.*



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        binding.btLogin.setOnClickListener {
            val email = binding.edId.text.toString()
            val password = binding.edPw.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                checkCredentials(email, password)
            }
        }

        binding.btRegister.setOnClickListener {
            if (binding.checkCeoregi.isChecked) {
                val intent = Intent(this, CeoregisterActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkCredentials(email: String, password: String) {
        val userQuery: Query = database.child("users").orderByChild("email").equalTo(email)
        val ceoQuery: Query = database.child("Ceousers").orderByChild("ceoemail").equalTo(email)

        userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(RegisterActivity.User::class.java)
                        if (user?.pw == password) {
                            Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MainActivity, IntroActivity::class.java)
                            startActivity(intent)
                            finish()
                            return
                        }
                    }
                    // 로그인 실패 처리
//                    Toast.makeText(this@MainActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                } else {
                    // Ceousers 테이블에서 이메일과 비밀번호 확인
                    checkCeoCredentials(email, password)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
                Toast.makeText(this@MainActivity, "로그인 실패", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun checkCeoCredentials(email: String, password: String) {
        val ceoquery: Query = database.child("Ceousers").orderByChild("ceoemail").equalTo(email)
        ceoquery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val ceousers = userSnapshot.getValue(CeoregisterActivity.Ceouser::class.java)
                        if (ceousers?.ceopw == password) {
                            Toast.makeText(this@MainActivity, "로그인 성공", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MainActivity, CeoIntroActivity::class.java)
                            startActivity(intent)
                            finish()
                            return
                        }
                    }
                }
                else{
                    Toast.makeText(this@MainActivity, "아이디와 비밀번호가 존재하지 않습니다", Toast.LENGTH_LONG).show()
                }
// 로그인 실패 처리

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
                Toast.makeText(this@MainActivity, "로그인 실패", Toast.LENGTH_LONG).show()
            }
        })
    }
}
