package com.example.tmobiletest.model.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Network {

    private val api: CardApi by lazy{
        initRetrofit()
    }

    private fun initRetrofit(): CardApi{
        val client = getClient()

        val service = Retrofit.Builder()

        return service.baseUrl("https://private-8ce77c-tmobiletest.apiary-mock.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CardApi::class.java)
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    fun getService() = api
}