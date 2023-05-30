package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SplashActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val currentUserEmail = auth.currentUser?.email

            val ceoUsersQuery = database.child("Ceousers")
                .orderByChild("ceoemail")
                .equalTo(currentUserEmail)

            val usersQuery = database.child("users")
                .orderByChild("email")
                .equalTo(currentUserEmail)

            ceoUsersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 로그인한 사용자가 Ceousers 테이블에 존재하는 경우
                        Log.d("SplashActivity", "CeoIntroActivity")
                        startActivity(Intent(this@SplashActivity, CeoIntroActivity::class.java))
                    } else {
                        // 로그인한 사용자가 Ceousers 테이블에 존재하지 않는 경우
                        usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(usersSnapshot: DataSnapshot) {
                                if (usersSnapshot.exists()) {
                                    // 로그인한 사용자가 users 테이블에 존재하는 경우
                                    Log.d("SplashActivity", "IntroActivity")
                                    startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                                } else {
                                    // 로그인한 사용자가 users 테이블에 존재하지 않는 경우
                                    Log.d("SplashActivity", "MainActivity")
                                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                }
                                finish()
                            }

                            override fun onCancelled(usersDatabaseError: DatabaseError) {
                                // users 테이블에서의 에러 처리
                                Log.d("SplashActivity", "Users Error: ${usersDatabaseError.message}")
                                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                finish()
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Ceousers 테이블에서의 에러 처리
                    Log.d("SplashActivity", "Ceousers Error: ${databaseError.message}")
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            })
        } else {
            // 사용자가 로그인하지 않은 경우
            Log.d("SplashActivity", "MainActivity")
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}
