package com.example.flashlearn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://jsonplaceholder.typicode.com/"

class MainActivity : AppCompatActivity() {
    private lateinit var buttonContainer: LinearLayout
    private lateinit var txtId: TextView
    private lateinit var musicServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add the splash screen
        installSplashScreen()
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        // Start music service immediately
        musicServiceIntent = Intent(this, Settings.MusicService::class.java)
        startService(musicServiceIntent)

        val sett = findViewById<ImageButton>(R.id.settings)
        sett.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        txtId = findViewById(R.id.txtId)
        buttonContainer = findViewById(R.id.stacks)

        getMyData()

        // Initialize views after a delay to simulate splash screen duration
        Handler(Looper.getMainLooper()).postDelayed({
            initializeViews()
        }, 3000)
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
            override fun onResponse(call: Call<List<MyDataItem>?>, response: Response<List<MyDataItem>?>) {
                val responseBody = response.body() ?: return // Handle null response gracefully

                for (myData in responseBody) {
                    val cardButton = Button(this@MainActivity).apply {
                        text = myData.id.toString() // Set button text to ID or another relevant field
                        setOnClickListener {
                            val intent = Intent(this@MainActivity, Stack::class.java).apply {
                                putExtra("ITEM_ID", myData.id) // Pass the ID or other relevant data
                            }
                            startActivity(intent) // Start the Stack activity
                        }
                    }
                    buttonContainer.addView(cardButton) // Add the button to the LinearLayout
                }
            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: ${t.message}") // Use the correct variable 't'
            }
        })
    }

    private fun initializeViews() {
        val addButton = findViewById<ImageButton>(R.id.add_btn)
        addButton.setOnClickListener {
            val intent = Intent(this, NewStack::class.java)
            startActivityForResult(intent, REQUEST_CODE_NEW_STACK)
        }

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
