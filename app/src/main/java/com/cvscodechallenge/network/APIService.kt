package com.cvscodechallenge.network

import com.cvscodechallenge.model.Root
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("services/feeds/photos_public.gne")
    suspend fun getSearch(
        @Query("tags") tags: String,
        @Query("format") format: String = "json",
        @Query("nojsoncallback") callback: Int = 1
    ): Response<Root>

    companion object {
        var retrofit: Retrofit? = null

        fun getRetrofit(): APIService {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://api.flickr.com/")
                    .client(createClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            } // if
            return retrofit!!.create(APIService::class.java)
        }

        private fun createClient() =
            OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            ).build()
    }
}