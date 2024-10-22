package com.example.flashlearn

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class Settings : AppCompatActivity() {

    private lateinit var musicServiceIntent: Intent
    private var musicService: MusicService? = null
    private var isBound = false
    private var isMusicPlaying = false
    private lateinit var anibtn: LottieAnimationView
    private var volumeLevel = 50 // Default volume
    private var isMuted = false

    // Service connection to bind with MusicService
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
            musicService?.setVolume(volumeLevel / 100f) // Set initial volume
            Log.d("Settings", "Service connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
            Log.d("Settings", "Service disconnected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        musicServiceIntent = Intent(this, MusicService::class.java)
        bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE)

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener { finish() }

        val vlmButton = findViewById<ImageButton>(R.id.vlm)
        anibtn = findViewById(R.id.ani_btn)
        val volumeValue = findViewById<TextView>(R.id.volumeValue)

        val increase = findViewById<Button>(R.id.increase)
        val decrease = findViewById<Button>(R.id.decrease)

        // Increase volume button logic
        increase.setOnClickListener {
            if (volumeLevel < 100) {
                // Unmute if currently muted
                if (isMuted) {
                    vlmButton.setImageResource(R.drawable.volume) // Change to unmuted icon
                    isMuted = false
                }
                volumeLevel += 10
                volumeValue.text = "Volume: $volumeLevel"
                if (isBound) {
                    musicService?.setVolume(volumeLevel / 100f)
                }
            }
        }

        // Decrease volume button logic
        decrease.setOnClickListener {
            if (volumeLevel > 0) {
                // Unmute if currently muted
                if (isMuted) {
                    vlmButton.setImageResource(R.drawable.volume) // Change to unmuted icon
                    isMuted = false
                }
                volumeLevel -= 10
                volumeValue.text = "Volume: $volumeLevel"
                if (isBound) {
                    musicService?.setVolume(volumeLevel / 100f)
                }
            }
        }

        // Mute/Unmute and Start/Stop music button logic
        vlmButton.setOnClickListener {
            if (isMusicPlaying) {
                // Mute/unmute if music is already playing
                if (isMuted) {
                    vlmButton.setImageResource(R.drawable.volume) // Show volume icon
                    if (isBound) {
                        musicService?.setVolume(volumeLevel / 100f) // Unmute
                    }
                    isMuted = false
                } else {
                    vlmButton.setImageResource(R.drawable.mute) // Show mute icon
                    if (isBound) {
                        musicService?.setVolume(0f) // Mute
                    }
                    isMuted = true
                }
            } else {
                // Start the music service if it's not playing
                startService(musicServiceIntent)
                bindService(musicServiceIntent, serviceConnection, BIND_AUTO_CREATE) // Bind again if needed
                vlmButton.setImageResource(R.drawable.volume) // Set to unmuted icon
                isMusicPlaying = true
                isMuted = false // Ensure it's unmuted when starting

                // Add log to confirm method call
                Log.d("Settings", "Starting music")
                musicService?.startMusic() // Start playing music
            }

            // Control Lottie animation here
            anibtn.speed = 3f
            anibtn.setMinAndMaxProgress(0.0f, 0.5f)
            anibtn.playAnimation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            unbindService(serviceConnection)
            isBound = false
            Log.d("Settings", "Service unbound")
        }
    }
}
