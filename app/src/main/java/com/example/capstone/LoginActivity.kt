package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.capstone.databinding.ActivityLoginBinding
import com.example.capstone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference

        binding.accessLoginBtn.setOnClickListener {
            val email = binding.edId.text.toString()
            val password = binding.edPw.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                checkCredentials(email, password)
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
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this@LoginActivity) { task ->
                                    if (task.isSuccessful) {
                                        val uid = auth.currentUser?.uid
                                        saveUserUid(userSnapshot.ref, uid)
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "로그인 성공",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent =
                                            Intent(this@LoginActivity, IntroActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "로그인 실패",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            return
                        }
                    }
                } else {
                    // Ceousers 테이블에서 이메일과 비밀번호 확인
                    checkCeoCredentials(email, password)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
                Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun checkCeoCredentials(email: String, password: String) {
        val ceoQuery: Query = database.child("Ceousers").orderByChild("ceoemail").equalTo(email)
        ceoQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (ceoSnapshot in dataSnapshot.children) {
                        val ceousers =
                            ceoSnapshot.getValue(CeoregisterActivity.Ceouser::class.java)
                        if (ceousers?.ceopw == password) {
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this@LoginActivity) { task ->
                                    if (task.isSuccessful) {
                                        val uid = auth.currentUser?.uid
                                        saveUserUid(ceoSnapshot.ref, uid)
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "로그인 성공",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        val intent =
                                            Intent(this@LoginActivity, CeoIntroActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "로그인 실패",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            return
                        }
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "아이디와 비밀번호가 존재하지 않습니다",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 에러 처리
                Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveUserUid(ref: DatabaseReference, uid: String?) {
        ref.child("uid").setValue(uid)
    }


















}