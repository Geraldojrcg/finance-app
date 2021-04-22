package com.example.financeapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class LoginResponse (
    @SerializedName("token")
    val token: String,

    @SerializedName("user")
    val user: User
)