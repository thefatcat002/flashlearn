package com.example.flashlearn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewStack : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var submitButton: Button

    companion object {
        const val BASE_URL = "https://probable-bat-dashing.ngrok-free.app/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_stack)

        editText = findViewById(R.id.crt_stk)
        submitButton = findViewById(R.id.submit)

        findViewById<ImageButton>(R.id.back_btn).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.settings).setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
        findViewById<ImageButton>(R.id.imageButton7).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        submitButton.setOnClickListener {
            submitButton.isEnabled=false
            submitButton.text="Loading"
            val deckTitle = editText.text.toString().trim()
            if (deckTitle.isNotBlank()) {
                createDeck(deckTitle)
            } else {
                submitButton.isEnabled=true
                submitButton.text="Submit"
                Toast.makeText(this, "Deck title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        val mainLayout = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createDeck(title: String) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(APIDecksService::class.java)

        retrofitBuilder.createDeck(title).enqueue(object : Callback<PostCreateStack> {
            override fun onResponse(call: Call<PostCreateStack>, response: Response<PostCreateStack>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@NewStack, "Deck created!", Toast.LENGTH_SHORT).show()
                    val intent2 = Intent(this@NewStack, MainActivity::class.java)
                    startActivity(intent2)
                    finish()
                } else {
                    submitButton.isEnabled=true
                    submitButton.text="Submit"
                    Log.e("NewStack", "Response Body: ${response.errorBody()?.string()}")
                    Toast.makeText(this@NewStack, "Failed to create deck.", Toast.LENGTH_SHORT).show()
                }
            }

            // Change Call<MyDataItem> to Call<PostCreateStack>
            override fun onFailure(call: Call<PostCreateStack>, t: Throwable) {
                submitButton.isEnabled=true
                submitButton.text="Submit"
                Log.e("NewStack", "Error: ${t.message}", t)
                Toast.makeText(this@NewStack, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
