package com.example.flashlearn

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast

class CardQuiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_quiz)

        val reveal = findViewById<Button>(R.id.rvl);
        reveal.setOnClickListener {
            val intent = Intent(this,CardReveal::class.java)
            startActivity(intent)
        }

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener {
            finish()
        }

        val sett = findViewById<ImageButton>(R.id.settings);
        sett.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }

        val home = findViewById<ImageButton>(R.id.imageButton7);
        home.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}