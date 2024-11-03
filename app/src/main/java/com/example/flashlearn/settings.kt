// Settings.kt
package com.example.flashlearn

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class Settings : AppCompatActivity() {

    private var isMusicPlaying = false
    private lateinit var anibtn: LottieAnimationView
    private var musicService: MusicService? = null
    private var isBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder // Cast to MusicBinder
            musicService = binder.getService() // Get the MusicService instance
            isMusicPlaying = musicService?.isPlaying() ?: false // Check if music is playing
            updateVolumeIcon(findViewById(R.id.vlm))
            updateLottieAnimation()
            Log.d("Settings", "Service connected: Music is ${if (isMusicPlaying) "playing" else "stopped"}")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupNavigationButtons()

        // Bind to the MusicService
        bindService(Intent(this, MusicService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
        isBound = true // Set isBound to true

        val vlmButton = findViewById<ImageButton>(R.id.vlm)
        anibtn = findViewById(R.id.ani_btn)

        // Music control button logic
        vlmButton.setOnClickListener {
            if (isMusicPlaying) {
                startMusicService("STOP_MUSIC")
            } else {
                startMusicService("START_MUSIC")
            }
            isMusicPlaying = !isMusicPlaying // Toggle the state
            updateVolumeIcon(vlmButton)
            updateLottieAnimation()
        }
    }

    private fun startMusicService(action: String) {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        startService(intent) // Start the service with the action
        Log.d("Settings", "Service action sent: $action")
    }

    private fun updateVolumeIcon(vlmButton: ImageButton) {
        if (isMusicPlaying) {
            vlmButton.setImageResource(R.drawable.volume) // Show unmuted icon
        } else {
            vlmButton.setImageResource(R.drawable.mute) // Show muted icon
        }
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

    private fun setupNavigationButtons() {
        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection) // Unbind from the service
            isBound = false
        }
    }
}
