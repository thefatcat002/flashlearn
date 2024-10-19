package com.example.flashlearn

//data class MyDataItem(
//    val deck: Deck?,  // Change deck to String type
//    val token: String
//)

data class MyDataItem(
    val id: Int, // ID of the deck
    val deck: String, // Name of the deck
    val token: String, // Token associated with the deck
    val token_expires_at: String, // Expiration time for the token
    val created_at: String, // Creation time of the deck
    val updated_at: String // Last update time of the deck
)

