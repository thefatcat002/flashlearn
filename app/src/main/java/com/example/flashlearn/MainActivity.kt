package com.example.flashlearn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var buttonContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add the splash screen
        installSplashScreen()
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        // Initialize views after a delay to simulate splash screen duration
        Handler(Looper.getMainLooper()).postDelayed({
            initializeViews()
        }, 3000)
    }

    private fun initializeViews() {
        val addButton = findViewById<ImageButton>(R.id.add_btn)
        addButton.setOnClickListener {
            val intent = Intent(this, NewStack::class.java)
            startActivityForResult(intent, REQUEST_CODE_NEW_STACK)
        }

        buttonContainer = findViewById(R.id.stacks)

        // Set window insets for button container
        ViewCompat.setOnApplyWindowInsetsListener(buttonContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NEW_STACK && resultCode == RESULT_OK) {
            val cardName = data?.getStringExtra("CARD_NAME") ?: run {
                Toast.makeText(this, "No card name returned", Toast.LENGTH_SHORT).show()
                return
            }
            createCardButton(cardName)
        }
    }

    private fun createCardButton(cardName: String) {
        if (cardName.isNotBlank()) {
            val cardButton = Button(this).apply {
                text = cardName
                setOnClickListener {
                    val intent = Intent(this@MainActivity, Stack::class.java)
                    startActivity(intent)
                }
            }
            buttonContainer.addView(cardButton)
        } else {
            Toast.makeText(this, "Card name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_CODE_NEW_STACK = 1
    }
}
