package com.example.flashlearn

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.cooking) // Ensure cooking.mp3 is in res/raw
            mediaPlayer.isLooping = true // Loop the music
            Log.d("MusicService", "MediaPlayer created and set to loop")
        } catch (e: Exception) {
            Log.e("MusicService", "Error initializing MediaPlayer: ${e.message}")
        }
    }

    fun startMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            Log.d("MusicService", "Music started")
        } else {
            Log.d("MusicService", "Music is already playing")
        }
    }

    fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepare() // Prepare the media player for future use
            Log.d("MusicService", "Music stopped")
        }
    }

    fun setVolume(volume: Float) {
        mediaPlayer.setVolume(volume, volume)
        Log.d("MusicService", "Volume set to: $volume")
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
        mediaPlayer.release() // Release resources when the service is destroyed
        Log.d("MusicService", "MediaPlayer released and service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}
