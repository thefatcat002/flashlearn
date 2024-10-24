package com.example.flashlearn

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardCreateActivity : AppCompatActivity() {
    private lateinit var questionEditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button // Updated delete button
    private var cardId: Int? = null // For updating existing cards
    private var id: Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_create)

        val intent = intent

        id = intent.getIntExtra("id", 0)
        val question = intent.getStringExtra("question")
        val answer = intent.getStringExtra("answer")

        questionEditText = findViewById(R.id.et_qst)
        answerEditText = findViewById(R.id.et_ans)
        saveButton = findViewById(R.id.save_btn)
        deleteButton = findViewById(R.id.dlt_btn) // Updated to dlt_btn

        setupNavigationButtons()

        if (question != null && answer != null) {
            questionEditText.setText(question)
            answerEditText.setText(answer)
        }

        saveButton.setOnClickListener {
            if (questionEditText.text.isNotEmpty() && answerEditText.text.isNotEmpty()) {
                saveCard(id)
            }
        }

        deleteButton.setOnClickListener {
            deleteCard(id)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupNavigationButtons() {
        val sett = findViewById<ImageButton>(R.id.settings)
        sett.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener {
            finish()
        }

        val homeButton = findViewById<ImageButton>(R.id.imageButton7)
        homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun saveCard(id: Int) {
        val intent = intent
        val updatedQuestion = questionEditText.text.toString()
        val updatedAnswer = answerEditText.text.toString()
        val IS_UPDATING = intent.getBooleanExtra("IS_UPDATING", false)
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

        if (IS_UPDATING) {
            retrofitBuilder.updateCard("Bearer $token", id, updatedQuestion, updatedAnswer).enqueue(object : Callback<Questions> {
                override fun onResponse(call: Call<Questions>, response: Response<Questions>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CardCreateActivity, "Card updated!", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this@CardCreateActivity, Stack::class.java).apply {
                            putExtra("token", token)
                        }
                        startActivity(intent2)
                        finish()
                    } else {
                        Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Questions>, t: Throwable) {
                    Log.e("Network Error", "Error: ${t.message}", t)
                }
            })
        } else {
            retrofitBuilder.createCard("Bearer $token", updatedQuestion, updatedAnswer).enqueue(object : Callback<Questions> {
                override fun onResponse(call: Call<Questions>, response: Response<Questions>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CardCreateActivity, "Card created!", Toast.LENGTH_SHORT).show()
                        val intent2 = Intent(this@CardCreateActivity, Stack::class.java).apply {
                            putExtra("token", token)
                        }
                        startActivity(intent2)
                        finish()
                    } else {
                        Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Questions>, t: Throwable) {
                    Log.e("Network Error", "Error: ${t.message}", t)
                }
            })
        }
    }

    private fun deleteCard(id: Int) {
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

        retrofitBuilder.deleteCard("Bearer $token", id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CardCreateActivity, "Card deleted!", Toast.LENGTH_SHORT).show()
                    val intent2 = Intent(this@CardCreateActivity, Stack::class.java).apply {
                        putExtra("token", token)
                    }
                    startActivity(intent2)
                    finish()
                } else {
                    Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Network Error", "Error: ${t.message}", t)
            }
        })
    }
}
