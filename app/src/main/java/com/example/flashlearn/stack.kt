package com.example.flashlearn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Stack : AppCompatActivity() {
    private lateinit var buttonContainer: LinearLayout // Container for dynamically created buttons
    private val questionsList: MutableList<Questions> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stack)

        val intent = intent
        val id = intent.getIntExtra("id", 0)
        val token = intent.getStringExtra("token")
        Log.e("Token", "Token: $token")
        buttonContainer = findViewById(R.id.stacks) // Ensure this matches your layout
        getQuestions()
        // Button to start CardCreateActivity
        val add = findViewById<Button>(R.id.crt_crd)
        add.setOnClickListener {
            val intent = Intent(this, CardCreateActivity::class.java).apply {
                putExtra("token", token)
            }
            startActivityForResult(intent, CREATE_CARD_REQUEST_CODE) // Start for result
        }

        val delete = findViewById<Button>(R.id.dlt_btn)
        delete.setOnClickListener {
            deleteDeck(id)
        }

        // Navigation buttons setup
        setupNavigationButtons()

        // Handle window insets for edge-to-edge support
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    private fun setupNavigationButtons() {
        val start = findViewById<Button>(R.id.start_btn)
        start.setOnClickListener {
            startActivity(Intent(this, CardQuiz::class.java))
        }

        val sett = findViewById<ImageButton>(R.id.settings)
        sett.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener {
            finish() // Close the current activity
        }

        val home = findViewById<ImageButton>(R.id.imageButton7)
        home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Optional: close this activity
        }
    }

    private fun addButtonToStack(id: Int, question: String, answer: String, token: String) {
        val button = Button(this).apply {
            text = question// Display question and answer
            background = resources.getDrawable(R.drawable.rounded_button, null) // For API 21+
            setTextColor(Color.WHITE) // Set text color as needed
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Full width
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 8, 8, 8) // Optional margins for spacing between buttons
            }
        }

        button.setOnClickListener {
            val intent = Intent(this@Stack, CardCreateActivity::class.java).apply {
                putExtra("question", question) // Accessing the ID from the Deck object
                putExtra("id", id)
                putExtra("answer", answer)
                putExtra("token", token)
                putExtra("IS_UPDATING", true)
            }
            startActivity(intent)
        }
        buttonContainer.addView(button) // Add button to the container
    }

    private fun deleteDeck(id: Int) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://probable-bat-dashing.ngrok-free.app/")
            .build()
            .create(APIDecksService::class.java)

        retrofitBuilder.deleteDeck(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Stack, "Deck deleted successfully", Toast.LENGTH_SHORT).show()
                    val intent2 = Intent(this@Stack, MainActivity::class.java)
                    startActivity(intent2)
                    finish()
                } else {
                    Toast.makeText(this@Stack, "Failed to delete deck", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@Stack, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getQuestions(){
        val token = intent.getStringExtra("token")

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token") // Add token to the header
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://probable-bat-dashing.ngrok-free.app/") // Replace with your actual base URL
            .build()
            .create(APICardsService::class.java)

        retrofitBuilder.getQuestions("Bearer $token").enqueue(object : Callback<List<Questions>> {
            override fun onResponse(p0: Call<List<Questions>>, p1: Response<List<Questions>>) {
                if (p1.isSuccessful){
                    questionsList.clear()
                    questionsList.addAll(p1.body()!!)

                    for (question in questionsList){
                        if (token != null) {
                            addButtonToStack(question.id, question.question, question.answer, token)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<Questions>>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    companion object {
        private const val CREATE_CARD_REQUEST_CODE = 1 // Request code for starting CardCreateActivity
    }
}
