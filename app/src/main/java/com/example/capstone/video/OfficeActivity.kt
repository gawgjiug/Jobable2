package com.example.capstone.video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.capstone.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class OfficeActivity : AppCompatActivity() {

    private var currentVideoIndex = 0
    private var videoIds = listOf("MmbYe7bsu5A","-wXl7g1xlmU","qEd4n8h5tX8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_office)


        val youTubePlayerView: YouTubePlayerView = findViewById(R.id.youtube_player_view)
        youTubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                val nextButton: Button = findViewById(R.id.video_nextbtn)
                nextButton.setOnClickListener {
                    currentVideoIndex++
                    if (currentVideoIndex >= videoIds.size) {
                        currentVideoIndex = 0
                    }
                    youTubePlayer.loadVideo(videoIds[currentVideoIndex], 0F)
                }
            }
        })
    }
}