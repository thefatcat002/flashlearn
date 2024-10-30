package com.example.flashlearn

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class Settings : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var isMusicPlaying = false
    private lateinit var anibtn: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize MediaPlayer with the music resource
        mediaPlayer = MediaPlayer.create(this, R.raw.cooking).apply {
            isLooping = true // Set looping if you want continuous play
        }

        setupNavigationButtons()

        val vlmButton = findViewById<ImageButton>(R.id.vlm)
        anibtn = findViewById(R.id.ani_btn)

        // Music control button logic
        vlmButton.setOnClickListener {
            if (isMusicPlaying) {
                stopMusic(vlmButton)
            } else {
                startMusic(vlmButton)
            }
            updateLottieAnimation()
        }
    }

    private fun startMusic(vlmButton: ImageButton) {
        mediaPlayer?.start()
        isMusicPlaying = true
        vlmButton.setImageResource(R.drawable.volume) // Show unmuted icon
        Log.d("Settings", "Music started")
    }

    private fun stopMusic(vlmButton: ImageButton) {
        mediaPlayer?.pause()
        isMusicPlaying = false
        vlmButton.setImageResource(R.drawable.mute) // Show muted icon
        Log.d("Settings", "Music stopped")
    }

    private fun updateLottieAnimation() {
        if (isMusicPlaying) {
            anibtn.setMinAndMaxProgress(0.0f, 0.5f) // Green animation progress range
        } else {
            anibtn.setMinAndMaxProgress(0.5f, 1.0f) // Red animation progress range
        }
        anibtn.speed = 3f // Set animation speed
        anibtn.playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("Settings", "MediaPlayer released")
    }

    private fun setupNavigationButtons() {

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener { finish() }
        }

    }

