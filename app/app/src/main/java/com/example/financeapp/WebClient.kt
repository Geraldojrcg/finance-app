package com.example.financeapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.financeapp.service.UserService

class WebClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://finance-app-back.herokuapp.com/api")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun UserService() = retrofit.create(UserService::class.java)
}