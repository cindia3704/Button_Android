package com.example.button.startApp_1

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService_Login {
    @POST("login/")
    @FormUrlEncoded
    fun logIn(@Field("username") username: String,@Field("password") passward:String): Call<Void>

    @POST("register/")
    @FormUrlEncoded
    fun register(@Field("userEmail") userEmail: String,@Field("password") passward:String,@Field("userNickName") userNickName:String,@Field("userGender") userGender:String): Call<Void>

    @GET("user/findEmail/{userEmail}/")
    fun findUserEmail(@Path("userEmail")userEmail:String):Call<ExistsOrNot>

}