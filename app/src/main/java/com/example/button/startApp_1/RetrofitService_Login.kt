package com.example.button.startApp_1

import com.example.button.startApp_1.data.LoginResponse
import com.example.button.startApp_1.data.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService_Login {
    @POST("login/")
    @FormUrlEncoded
    fun logIn(@Field("username") username: String,@Field("password") passward:String): Call<LoginResponse>

    @POST("register/")
    @FormUrlEncoded
    fun register(@Field("userEmail") userEmail: String,@Field("password") passward:String,@Field("userNickName") userNickName:String,@Field("userGender") userGender:String): Call<Void>

    @GET("user/findEmail/{userEmail}/")
    fun findUserEmail(@Path("userEmail")userEmail:String):Call<ExistsOrNot>

    @GET("user")
    fun getUser():Call<MutableList<User>>

    @GET("closet/{id}")
    fun getCloth(@Path("id")id:Int,
                 @Header("WWW-Authenticate") token : String):Call<JsonObject>
}