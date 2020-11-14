package com.example.button.startApp_1.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var retrofitService: RetrofitService
    var token = ""
    var gender = ""
    //    val baseUrl = "http://18.191.146.76:9999/"
    val baseUrl = "https://capstonebutton.kro.kr:9000/"
    val imageBaseUrl = "https://capstonebutton.kro.kr:9000"

    var retrofit : Retrofit
    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val logger = OkHttpClient.Builder().addInterceptor(interceptor).build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(logger)
            .build()

        retrofitService = retrofit.create(RetrofitService::class.java)
        Log.d("tokenn",token)
    }

    class ErrorResponse {
        var email = 0

    }
}
