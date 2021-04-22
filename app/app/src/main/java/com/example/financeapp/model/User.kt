package com.example.financeapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id")
    val id: Int,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String
)

data class UserRequest (
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

data class UserResponse (
    @SerializedName("token")
    val token: String,

    @SerializedName("user")
    val user: User
)