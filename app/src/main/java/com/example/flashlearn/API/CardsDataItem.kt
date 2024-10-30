package com.example.flashlearn

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.w3c.dom.Text

data class CardsDataItem(
//    val id: Int? = null,
//    @SerializedName("question")
    val question: String, // Question
    val answer: String, // Answer
)


