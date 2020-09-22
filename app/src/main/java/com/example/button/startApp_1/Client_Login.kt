package com.example.button.startApp_1

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Client_Login {
    var retrofitService: RetrofitService_Login
    var token = ""

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val logger = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://18.191.146.76:9999/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(logger)
            .build()

        retrofitService = retrofit.create(RetrofitService_Login::class.java)

    }
}
