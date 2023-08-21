package com.example.capstone.video_quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Video
import com.example.capstone.IntroActivity
import com.example.capstone.R
import com.example.capstone.VideoActivity
import com.example.capstone.databinding.ActivityQuizResultBinding

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding:ActivityQuizResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val score = intent.getIntExtra("score",0)
        val totalSize = intent.getIntExtra("totalSize",0)

        //점수 보여주기
        binding.scoreText.text =getString(R.string.count_label,score,totalSize)

        //다시하기 버튼
        binding.resetBtn.setOnClickListener{
            val intent = Intent(this@QuizResultActivity,VideoActivity::class.java)
            startActivity(intent)
        }
        //처음으로 돌아가기 버튼
        binding.homeBtn.setOnClickListener{
            val intent = Intent(this@QuizResultActivity,IntroActivity::class.java)
            startActivity(intent)
        }
    }
}