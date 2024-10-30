package com.example.flashlearn

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mediaPlayer == null) {
            Log.d("MusicService", "Initializing MediaPlayer")
            mediaPlayer = MediaPlayer.create(this, R.raw.cooking)
            mediaPlayer?.isLooping = true
        }
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
            Log.d("MusicService", "Music started")
        } else {
            Log.d("MusicService", "MediaPlayer already playing")
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            Log.d("MusicService", "Music stopped")
        }
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("MusicService", "MediaPlayer released")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
