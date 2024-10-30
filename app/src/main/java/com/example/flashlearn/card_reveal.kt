package com.example.flashlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CardReveal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_reveal)

        val token = intent.getStringExtra("token")

        val next = findViewById<Button>(R.id.next);
        next.setOnClickListener {
            next.isEnabled=false
            next.text="Loading"
            val intent = Intent(this,CardQuiz::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
            finish()
        }

        val sett = findViewById<ImageButton>(R.id.settings);
        sett.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
            finish()
        }

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener {
            finish()
        }

        val homeButton = findViewById<ImageButton>(R.id.imageButton7)
        homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close this activity to avoid returning to it
        }

        val answer = findViewById<TextView>(R.id.answerTv)
        answer.text = intent.getStringExtra("answer")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}