package com.example.button.startApp_1.network

import com.example.button.startApp_1.activity.ExistsOrNot
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.LoginResponse
import com.example.button.startApp_1.data.User
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {
    @POST("/login/")
    @FormUrlEncoded
    fun logIn(@Field("username") username: String,@Field("password") passward:String): Call<LoginResponse>

    @POST("/register/")
    @FormUrlEncoded
    fun register(@Field("userEmail") userEmail: String,@Field("password") passward:String,@Field("userNickName") userNickName:String,@Field("userGender") userGender:String): Call<Void>

    @GET("/user/findEmail/{userEmail}/")
    fun findUserEmail(@Path("userEmail")userEmail:String):Call<ExistsOrNot>

    @GET("/user")
    fun getUser():Call<MutableList<User>>

    @GET("/closet/{id}")
    fun getCloth(@Path("id")id:Int,
                 @Header("Authorization") token : String):Call<MutableList<Cloth>>
}