package com.example.flashlearn

import android.annotation.SuppressLint
import android.app.Activity
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

class CardCreateActivity : AppCompatActivity() {
    private lateinit var questionEditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var saveButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_create)

        val sett = findViewById<ImageButton>(R.id.settings);
        sett.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }

        // Initialize views
        questionEditText = findViewById(R.id.et_qst)  // EditText for question
        answerEditText = findViewById(R.id.et_ans)    // EditText for answer
        saveButton = findViewById(R.id.save_btn)      // Button to save the card

        val back = findViewById<ImageButton>(R.id.back_btn);
        back.setOnClickListener {
            val intent = Intent(this,Stack::class.java)
            startActivity(intent)
        }

        // Home button setup
        val homeButton = findViewById<ImageButton>(R.id.imageButton7)
        homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Close this activity to avoid returning to it
        }

        // Set up save button click listener
        saveButton.setOnClickListener {
            saveCard()
        }

        // Handle window insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun saveCard() {
        val question = questionEditText.text.toString()
        val answer = answerEditText.text.toString()

        // Check if both fields are filled
        if (question.isNotBlank() && answer.isNotBlank()) {
            // Combine question and answer into a single string
            val cardName = "$question - $answer"

            // Return the combined string to MainActivity
            val resultIntent = Intent()
            resultIntent.putExtra("CARD_NAME", cardName)
            setResult(Activity.RESULT_OK, resultIntent)
            finish() // Close this activity and return to MainActivity

            Toast.makeText(this, "Card saved!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Both fields must be filled!", Toast.LENGTH_SHORT).show()
        }
    }
}
