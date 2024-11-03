// MusicService.kt
package com.example.flashlearn

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    // Define the Binder class
    inner class MusicBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService // Return this instance of MusicService
        }
    }

    private val binder = MusicBinder()

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicService", "Service created")
        mediaPlayer = MediaPlayer.create(this, R.raw.cooking).apply {
            isLooping = true // Set to loop the music
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder // Return the binder instance
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        Log.d("MusicService", "Received action: $action")

        when (action) {
            "START_MUSIC" -> startMusic()
            "STOP_MUSIC" -> stopMusic()
        }
        return START_STICKY
    }

    private fun startMusic() {
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            Log.d("MusicService", "Music started")
        }
    }

    private fun stopMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            Log.d("MusicService", "Music paused")
        }
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("MusicService", "MediaPlayer released")
    }
}
