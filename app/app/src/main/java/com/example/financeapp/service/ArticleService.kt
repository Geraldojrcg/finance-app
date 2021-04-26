package com.example.financeapp.service

import com.example.financeapp.model.Article
import retrofit2.Call
import retrofit2.http.*

interface ArticleService {
    @Headers("Content-Type: application/json")
    @GET("users/{userId}/articles")
    fun listArticles(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Call<List<Article>>

    @Headers("Content-Type: application/json")
    @POST("users/{userId}/articles/{articleId}/read")
    fun readArticle(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int,
        @Path("articleId") articleId: Int
    ): Call<Any>
}