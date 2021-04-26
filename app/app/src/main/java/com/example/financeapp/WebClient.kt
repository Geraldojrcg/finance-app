package com.example.financeapp

import com.example.financeapp.service.ArticleService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.financeapp.service.UserService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class WebClient {

    private val BASE_URL = "https://finance-app-back.herokuapp.com/api/";

    private val client = OkHttpClient()
        .newBuilder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun UserService() = retrofit.create(UserService::class.java)

    fun ArticleService() = retrofit.create(ArticleService::class.java)
}