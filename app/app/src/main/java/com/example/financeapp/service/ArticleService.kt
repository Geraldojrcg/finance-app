package com.example.financeapp.service

import com.example.financeapp.model.Article
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ArticleService {
    @Headers("Content-Type: application/json")
    @GET("articles")
    fun listArticles(@Header("Authorization") token: String): Call<List<Article>>
}