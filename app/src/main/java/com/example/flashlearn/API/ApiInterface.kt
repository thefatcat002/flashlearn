package com.example.flashlearn

import retrofit2.Call
import retrofit2.http.*

interface APIDecksService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/decks")
    fun createDeck(
        @Field("deck") deck: String
    ): Call<PostCreateStack>

    @GET("/decks/{id}")
    fun getDeck(
        @Path("id") id: Int
    ): Call<MyDataItem>

    @GET("/api/decks") // Endpoint to get all decks
    fun getDecks(): Call<List<MyDataItem>>

    @DELETE("/api/decks/{id}")
    fun deleteDeck(
        @Path("id") id: Int
    ): Call<Void>
}
