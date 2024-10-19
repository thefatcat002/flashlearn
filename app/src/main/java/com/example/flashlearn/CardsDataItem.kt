package com.example.flashlearn

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.w3c.dom.Text

data class CardsDataItem(
//    val id: Int? = null,
//    @SerializedName("question")
    val question: String, // Question
    val answer: String // Answer
)
//) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readValue(Int::class.java.classLoader) as? Int,
//        parcel.readString() ?: "",
//        parcel.readString() ?: ""
//    )

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeValue(id)
//        parcel.writeString(title)
//        parcel.writeString(answer)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<CardsDataItem> {
//        override fun createFromParcel(parcel: Parcel): CardsDataItem {
//            return CardsDataItem(parcel)
//        }
//
//        override fun newArray(size: Int): Array<CardsDataItem?> {
//            return arrayOfNulls(size)
//        }
//    }

