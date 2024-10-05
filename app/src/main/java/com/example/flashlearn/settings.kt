package com.example.flashlearn

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings : AppCompatActivity() {

    private lateinit var musicServiceIntent: Intent
    private var isMusicPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        musicServiceIntent = Intent(this, MusicService::class.java)

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener {
            finish()
        }

        val home = findViewById<ImageButton>(R.id.imageButton7)
        home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Button to toggle music
        val toggle = findViewById<ImageButton>(R.id.vlm)
        toggle.setOnClickListener {
            if (isMusicPlaying) {
                stopService(musicServiceIntent)
                toggle.setImageResource(R.drawable.mute) // Change to a stop icon
            } else {
                startService(musicServiceIntent)
                toggle.setImageResource(R.drawable.volume) // Change to a play icon
            }
            isMusicPlaying = !isMusicPlaying
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // MusicService class
    class MusicService : Service() {

        private lateinit var mediaPlayer: MediaPlayer

        override fun onCreate() {
            super.onCreate()
            mediaPlayer = MediaPlayer.create(this, R.raw.cooking) // Replace with your music file
            mediaPlayer.isLooping = true // Loop the music
            mediaPlayer.start() // Start playing music
        }

        override fun onDestroy() {
            super.onDestroy()
            mediaPlayer.stop()
            mediaPlayer.release()
        }

        override fun onBind(intent: Intent?): IBinder? {
            return null // Not using binding in this case
        }
    }
}
