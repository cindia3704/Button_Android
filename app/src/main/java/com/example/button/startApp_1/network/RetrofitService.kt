package com.example.button.startApp_1.network

import com.example.button.startApp_1.activity.ExistsOrNot
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.LoginResponse
import com.example.button.startApp_1.data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
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


    @Multipart
    @POST("/closet/{id}/")
    fun insertCloset(@Header("Authorization") token : String,
                     @Path("id") user_id : Int,
                     @Part("id") clothID: RequestBody,
                     @Part("color") color: RequestBody,
                     @Part("category") category:RequestBody,
                     @Part("season") season:MutableList<RequestBody>,
                     @Part("dateBought") dateBought:RequestBody,
                     @Part("dateLastWorn") dateLastWorn:RequestBody,
                     @Part photo : MultipartBody.Part?) : Call<Void>

    @Multipart
    @PUT("/closet/{id}/{clothID}/")
    fun updateCloset(@Header("Authorization") token : String,
                     @Path("id") user_id : Int,
                     @Path("clothID") clothID: Int,
                     @Part("id") id : RequestBody,
                     @Part("color") color: RequestBody,
                     @Part("category") category:RequestBody,
                     @Part("season") season:MutableList<RequestBody>,
                     @Part("dateBought") dateBought:RequestBody,
                     @Part("dateLastWorn") dateLastWorn:RequestBody,
                     @Part photo : MultipartBody.Part?) : Call<Void>
}
