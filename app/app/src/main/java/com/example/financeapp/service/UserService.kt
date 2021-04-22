package com.example.financeapp.service

import com.example.financeapp.model.*
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @Headers("Content-Type: application/json")
    @POST("users/register")
    fun createUser(@Body user: UserRequest): Call<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("users/login")
    fun loginUser(@Body login: LoginRequest): Call<LoginResponse>
}
