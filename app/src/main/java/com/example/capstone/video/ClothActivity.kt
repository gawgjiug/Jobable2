package com.example.capstone.video
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ClothActivity : AppCompatActivity() {
    private var currentVideoIndex = 0
    private val videoIds = listOf("cMwR1gJXWnI", "WSiOMLlByew", "cxO_j7r0mA8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloth)

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
