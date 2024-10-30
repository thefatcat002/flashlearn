package com.example.flashlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var buttonContainer: LinearLayout
    private lateinit var musicServiceIntent: Intent

    companion object {
        const val BASE_URL = "https://probable-bat-dashing.ngrok-free.app/"
        const val REQUEST_CODE_NEW_STACK = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        findViewById<ImageButton>(R.id.settings).setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        buttonContainer = findViewById(R.id.stacks)
        getDecksData()

        Handler(Looper.getMainLooper()).postDelayed({
            initializeViews()
        }, 3000)
    }

    private fun getDecksData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(APIDecksService::class.java)

        retrofitBuilder.getDecks().enqueue(object : Callback<List<MyDataItem>?> {
            override fun onResponse(call: Call<List<MyDataItem>?>, response: Response<List<MyDataItem>?>) {
                val responseBody = response.body() ?: run {
                    Log.e("MainActivity", "Response body is null")
                    return
                }

                for (item in responseBody) { // Renamed 'deck' to 'item' for clarity
                    val id = item.id
                    val token = item.token
                    Log.d("Token", "Token: $token")

                    val deckButton = Button(this@MainActivity).apply {
                        text = item.deck // Accessing the deck name from the Deck object

                        background = ContextCompat.getDrawable(this@MainActivity, R.drawable.rounded_button)
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))

                    }
                    deckButton.setOnClickListener {
                        val intent = Intent(this@MainActivity, Stack::class.java).apply {
                            putExtra("token", token)
                            putExtra("id", id) // Accessing the ID from the Deck object
                            //
                        }
                        startActivity(intent)
                    }
                    buttonContainer.addView(deckButton) // Add the button to the container
                }
            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                Log.e("MainActivity", "Error fetching decks: ${t.message}", t)
                Toast.makeText(this@MainActivity, "Failed to load decks. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializeViews() {
        findViewById<ImageButton>(R.id.add_btn).setOnClickListener {
            startActivityForResult(Intent(this, NewStack::class.java), REQUEST_CODE_NEW_STACK)
        }

        ViewCompat.setOnApplyWindowInsetsListener(buttonContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_NEW_STACK && resultCode == Activity.RESULT_OK) {
            val deckId = data?.getIntExtra("DECK_ID", -1)
            if (deckId != null && deckId != -1) {
                // Fetch the new deck or add it to the UI directly
                getDecksData() // Optionally refresh the list
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(musicServiceIntent)
    }
}
