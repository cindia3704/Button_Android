package com.example.button.startApp_1

import android.provider.ContactsContract
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService_Login {
    @POST("login/")
    @FormUrlEncoded
    fun logIn(@Field("username") username: String,@Field("password") passward:String): Call<Void>

    @POST("register/")
    @FormUrlEncoded
    fun register(@Field("userEmail") userEmail: String,@Field("password") passward:String,@Field("userNickName") userNickName:String,@Field("userGender") userGender:String): Call<Void>
}