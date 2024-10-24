package com.example.flashlearn

import retrofit2.Call
import retrofit2.http.*

interface APICardsService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/questions")
    fun createCard(
        @Header("Authorization") token: String,
        @Field("question") title: String,
        @Field("answer") answer: String
    ): Call<Questions>

    @GET("/api/questions")
    fun getQuestions(
        @Header("Authorization") token: String,
    ): Call<List<Questions>>

    @FormUrlEncoded
    @PUT("/api/questions/{id}")
    fun updateCard(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Field("question") title: String,
        @Field("answer") answer: String
    ): Call<Questions>

    @DELETE("/api/questions/{id}")
    fun deleteCard(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<Void>
}
