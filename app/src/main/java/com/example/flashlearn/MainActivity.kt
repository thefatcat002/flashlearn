package com.example.flashlearn

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var buttonContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val add = findViewById<ImageButton>(R.id.add_btn)
        add.setOnClickListener {
            val context = Intent(this, NewStack::class.java)
            startActivityForResult(context, REQUEST_CODE_NEW_STACK)
        }

        buttonContainer = findViewById(R.id.stacks)

        ViewCompat.setOnApplyWindowInsetsListener(buttonContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NEW_STACK && resultCode == RESULT_OK) {
            data?.getStringExtra("CARD_NAME")?.let { cardName ->
                createCardButton(cardName)
            }
        }
    }

    private fun createCardButton(cardName: String) {
        if (cardName.isNotBlank()) {
            val cardButton = Button(this).apply {
                text = cardName
                setOnClickListener {
                    // Start the CardCreateActivity when this button is clicked
                    val intent = Intent(this@MainActivity, CardCreateActivity::class.java)
                    startActivity(intent)
                }
            }

            // Add the button to the LinearLayout
            buttonContainer.addView(cardButton)
        } else {
            Toast.makeText(this, "Card name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_NEW_STACK = 1
    }
}
