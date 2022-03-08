package com.cvscodechallenge.model

import com.google.gson.annotations.SerializedName

data class Item(
    val title : String,
    val link : String,
    val media : Media,
    @SerializedName("date_taken")
    val dateTaken : String,
    val description : String,
    val published : String,
    val author : String,
    val tags : String
)
