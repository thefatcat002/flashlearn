package com.example.flashlearn

import retrofit2.Call
import retrofit2.http.*

interface APICardsService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("/api/questions")
    fun createCard(
        @Field("question") title: String,
        @Field("answer") answer: String
    ): Call<CardsDataItem>

    @GET("/api/questions/{id}")
    fun getCard(
        @Path("id") id: Int
    ): Call<CardsDataItem>

    @GET("/api/questions")
    fun getQuestions(): Call<List<Questions>>

    @FormUrlEncoded
    @PUT("/api/questions/{id}")
    fun updateCard(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("answer") answer: String
    ): Call<CardsDataItem>

    @DELETE("/api/questions/{id}")
    fun deleteCard(
        @Path("id") id: Int
    ): Call<Void>
}
