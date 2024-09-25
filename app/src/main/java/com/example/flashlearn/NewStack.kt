package com.example.flashlearn

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

class NewStack : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_stack)

        editText = findViewById(R.id.crt_stk) // Your EditText ID
        submitButton = findViewById(R.id.submit) // Your Button ID

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener {
            finish() // Close this activity and return to the previous one
        }

        val home = findViewById<ImageButton>(R.id.imageButton7)
        home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close current activity to avoid returning to it
        }

        submitButton.setOnClickListener {
            val cardName = editText.text.toString()

            if (cardName.isNotBlank()) {
                // Return the card name to MainActivity
                val resultIntent = Intent()
                resultIntent.putExtra("CARD_NAME", cardName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close this activity and return to MainActivity
            } else {
                Toast.makeText(this, "Card name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}