package com.example.financeapp.model

import com.google.gson.annotations.SerializedName

data class Article (
    @SerializedName("id")
    val id : Int,

    @SerializedName("createdAt")
    val createdAt : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("body")
    val body : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("readed")
    val readed : Boolean
)