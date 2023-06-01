package com.example.capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.capstone.databinding.ActivityCeoregisterBinding
import com.example.capstone.databinding.ActivityRegisterBinding
import com.example.capstone.utils.FBAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CeoregisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ceoregister)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference
        //제발되라

        val binding = ActivityCeoregisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCeojoin.setOnClickListener {
            var CeoJoin = true
            val Ceo_email = binding.edCeoid.text.toString()
            val Ceo_pw1 = binding.edCeopw1.text.toString()
            val Ceo_pw2 = binding.edCeopw2.text.toString()
            val Ceo_name = binding.edCeoname.text.toString()
            val Ceo_phone = binding.edCeophone.text.toString()

            if(Ceo_name.isEmpty()){
                Toast.makeText(this@CeoregisterActivity,"기업명을 입력해주세요",Toast.LENGTH_SHORT).show()
                CeoJoin = false
            }
            if(Ceo_email.isEmpty()){
                Toast.makeText(this@CeoregisterActivity,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show()
                CeoJoin = false
            }
            if(Ceo_pw1.isEmpty()){
                Toast.makeText(this@CeoregisterActivity,"비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                CeoJoin = false
            }
            if(Ceo_pw2.isEmpty()){
                Toast.makeText(this@CeoregisterActivity,"비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
                CeoJoin = false
            }
            if(!Ceo_pw1.equals(Ceo_pw2)){
                Toast.makeText(this@CeoregisterActivity,"비밀번호가 일치하지 않습니다 ",Toast.LENGTH_SHORT).show()
                CeoJoin = false
            }
            if(Ceo_pw1.length<6){
                Toast.makeText(this@CeoregisterActivity,"비밀번호를 6자 이상 입력해주세요",Toast.LENGTH_SHORT).show()
                CeoJoin = false
            }
            if(CeoJoin){
                auth.createUserWithEmailAndPassword(Ceo_email,Ceo_pw1)
                    .addOnCompleteListener(this){task->
                        if(task.isSuccessful){
                            val Ceouser = auth.currentUser
                            val Ceouserid = Ceouser?.uid
                            Ceouserid?.let {
                                val CeouserInfo = Ceouser(Ceo_name,Ceo_email,Ceo_phone,Ceo_pw1)
                                database.child("Ceousers").child(Ceouserid).setValue(CeouserInfo)
                            }
                            Toast.makeText(this,"성공",Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,CeoIntroActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this,"실패",Toast.LENGTH_SHORT).show()
                        }
                    }
            }



        }

    }
    data class Ceouser(
        val ceoname : String = " ",
        val ceoemail : String = " ",
        val ceophone : String = " ",
        val ceopw : String = " "

    )


}