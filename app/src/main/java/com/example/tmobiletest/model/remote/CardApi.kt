package com.example.tmobiletest.model.remote

import com.example.tmobiletest.model.PageResponse
import retrofit2.http.GET

interface CardApi {
    @GET("test/home")
    suspend fun getCards(): PageResponse
}