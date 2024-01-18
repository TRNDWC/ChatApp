package com.example.baseproject.api

import com.example.baseproject.model.Data
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.imgflip.com/")
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("get_memes")
    fun getMemes(): Call<Data>

}