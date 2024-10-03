package com.example.flashlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class NewStack : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_stack)

        editText = findViewById(R.id.crt_stk) // Your EditText ID
        submitButton = findViewById(R.id.submit) // Your Button ID

        val backButton = findViewById<ImageButton>(R.id.back_btn)
        backButton.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }

        val homeButton = findViewById<ImageButton>(R.id.imageButton7)
        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close current activity to avoid returning to it
        }

        submitButton.setOnClickListener {
            val cardName = editText.text.toString().trim() // Trim whitespace

            if (cardName.isNotBlank()) {
                // Return the card name to MainActivity
                val resultIntent = Intent().apply {
                    putExtra("CARD_NAME", cardName)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close this activity and return to MainActivity
            } else {
                Toast.makeText(this, "Card name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Ensure the main layout is defined correctly
        val mainLayout = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
