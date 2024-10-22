package com.example.flashlearn

import android.annotation.SuppressLint
import android.app.Activity
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val CARD_URL = "https://probable-bat-dashing.ngrok-free.app/"

class CardCreateActivity : AppCompatActivity() {
    private lateinit var questionEditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button // Updated delete button
    private var cardId: Int? = null // For updating existing cards

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_create)

        questionEditText = findViewById(R.id.et_qst)
        answerEditText = findViewById(R.id.et_ans)
        saveButton = findViewById(R.id.save_btn)
        deleteButton = findViewById(R.id.dlt_btn) // Updated to dlt_btn

        setupNavigationButtons()

        // Check if we're editing an existing card
        cardId = intent.getIntExtra("CARD_ID", -1).takeIf { it != -1 }
        cardId?.let { getCard(it) }

        saveButton.setOnClickListener {
            saveCard()
        }

        deleteButton.setOnClickListener {
            cardId?.let { id -> deleteCard(id) } // Call deleteCard if editing
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

    private fun saveCard() {
        val question = questionEditText.text.toString()
        val answer = answerEditText.text.toString()

        if (question.isNotBlank() && answer.isNotBlank()) {
            val card = CardsDataItem( question = question, answer = answer)
            if (cardId == null) {
                submitData(card)
            } else {
                updateCard(card)
            }
        } else {
            Toast.makeText(this, "Both fields must be filled!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitData(card: CardsDataItem) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CARD_URL)
            .build()
            .create(APICardsService::class.java)

        retrofitBuilder.createCard(card.question, card.answer).enqueue(object : Callback<CardsDataItem> {
            override fun onResponse(call: Call<CardsDataItem>, response: Response<CardsDataItem>) {
                if (response.isSuccessful && response.body() != null) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("NEW_CARD", response.body()!!.question)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                    Toast.makeText(this@CardCreateActivity, "Card saved!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                    Toast.makeText(this@CardCreateActivity, "Failed to save card. Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CardsDataItem>, t: Throwable) {
                Log.e("Network Error", "Error: ${t.message}", t)
                Toast.makeText(this@CardCreateActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCard(card: CardsDataItem) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CARD_URL)
            .build()
            .create(APICardsService::class.java)

        cardId?.let { id ->
            retrofitBuilder.updateCard(id, card.question, card.answer).enqueue(object : Callback<CardsDataItem> {
                override fun onResponse(call: Call<CardsDataItem>, response: Response<CardsDataItem>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CardCreateActivity, "Card updated!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CardsDataItem>, t: Throwable) {
                    Log.e("Network Error", "Error: ${t.message}", t)
                }
            })
        }
    }

    private fun getCard(id: Int) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CARD_URL)
            .build()
            .create(APICardsService::class.java)

        retrofitBuilder.getCard(id).enqueue(object : Callback<CardsDataItem> {
            override fun onResponse(call: Call<CardsDataItem>, response: Response<CardsDataItem>) {
                if (response.isSuccessful && response.body() != null) {
                    val card = response.body()!!
                    questionEditText.setText(card.question)
                    answerEditText.setText(card.answer)
                } else {
                    Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CardsDataItem>, t: Throwable) {
                Log.e("Network Error", "Error: ${t.message}", t)
            }
        })
    }

    private fun deleteCard(id: Int) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CARD_URL)
            .build()
            .create(APICardsService::class.java)

        retrofitBuilder.deleteCard(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CardCreateActivity, "Card deleted!", Toast.LENGTH_SHORT).show()
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
