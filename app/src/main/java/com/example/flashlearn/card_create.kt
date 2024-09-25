package com.example.flashlearn

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toolbar

class CardCreateActivity : AppCompatActivity() {
    private lateinit var questionEditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_create) // Ensure this matches your layout file

        // Initialize views
        questionEditText = findViewById(R.id.et_qst)
        answerEditText = findViewById(R.id.et_ans)
        saveButton = findViewById(R.id.save_btn) // Ensure the ID matches

        // Set up toolbar and home button
        val homeButton = findViewById<ImageButton>(R.id.imageButton7)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close this activity to avoid returning to it
        }

        // Set up save button click listener
        saveButton.setOnClickListener {
            saveCard()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveCard() {
        val question = questionEditText.text.toString()
        val answer = answerEditText.text.toString()

        if (question.isNotBlank() && answer.isNotBlank()) {
            // Save card logic goes here
            Toast.makeText(this, "Card saved!", Toast.LENGTH_SHORT).show()
            // Optionally finish this activity or clear fields
        } else {
            Toast.makeText(this, "Both fields must be filled!", Toast.LENGTH_SHORT).show()
        }
    }
}
