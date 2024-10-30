package com.example.flashlearn

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
import android.widget.TextView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CardQuiz : AppCompatActivity() {
    private val questionsList: MutableList<Questions> = mutableListOf()
    private lateinit var questionTv: TextView
    private lateinit var answerEt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_quiz)

        questionTv = findViewById(R.id.questionTv)
        answerEt = findViewById(R.id.editText)
        val token = intent.getStringExtra("token")

        Log.d("Token", "$token")
        getQuestions()

        val back = findViewById<ImageButton>(R.id.back_btn)
        back.setOnClickListener {
            finish()
        }

        val sett = findViewById<ImageButton>(R.id.settings);
        sett.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }

        val home = findViewById<ImageButton>(R.id.imageButton7);
        home.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getRandomQuestion(): Questions? {
        return if (questionsList.isNotEmpty()) {
            val randomIndex = (questionsList.indices).random()
            questionsList[randomIndex]
        } else {
            null
        }
    }

    private fun displayRandomQuestion() {
        val randomQuestion = getRandomQuestion()
        val reveal = findViewById<Button>(R.id.rvl);
        val token = intent.getStringExtra("token")

        randomQuestion?.let {
            questionTv.text = randomQuestion.question
            Log.d("Question", "Random Question: ${randomQuestion.question}")

            reveal.setOnClickListener {
                val intent = Intent(this,CardReveal::class.java)
                intent.putExtra("answer", randomQuestion.answer)
                intent.putExtra("token", token)
                startActivity(intent)
                finish()
            }
        }
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
                    displayRandomQuestion()
                }
            }

            override fun onFailure(p0: Call<List<Questions>>, p1: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}