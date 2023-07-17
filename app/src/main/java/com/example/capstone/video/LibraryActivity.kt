package com.example.capstone.video

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.capstone.R
import com.example.capstone.video_quiz.Cloth_QuizActivity
import com.example.capstone.video_quiz.Library_QuizActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class LibraryActivity : AppCompatActivity() {

    private var currentVideoIndex = 0
    private var videoIds = listOf("BRE28hTnokM","CmcbrwHOWB4","_rpV15tgAfc","-IeXaqb9JEs")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                val nextButton: Button = findViewById(R.id.video_nextbtn)
                nextButton.setOnClickListener {
                    currentVideoIndex++
                    if (currentVideoIndex >= videoIds.size) {
                        // 전환할 액티비티로 화면 전환
                        val intent = Intent(this@LibraryActivity, Library_QuizActivity::class.java)
                        startActivity(intent)
                        return@setOnClickListener
                    }
                    youTubePlayer.loadVideo(videoIds[currentVideoIndex], 0F)
                }
            }
        })


    }
}