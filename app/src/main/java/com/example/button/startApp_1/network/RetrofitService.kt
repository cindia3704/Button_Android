package com.example.button.startApp_1.network

import com.example.button.startApp_1.activity.ExistsOrNot
import com.example.button.startApp_1.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitService {
    @POST("/login/")
    @FormUrlEncoded
    fun logIn(
        @Field("username") username: String,
        @Field("password") passward: String
    ): Call<LoginResponse>

    @GET("/login/{userEmail}/")
    fun getLoggedUser(@Path("userEmail") userEmail: String): Call<LoggedUserInfo>

    @POST("/register/")
    @FormUrlEncoded
    fun register(
        @Field("userEmail") userEmail: String,
        @Field("password") passward: String,
        @Field("userNickName") userNickName: String,
        @Field("userGender") userGender: String
    ): Call<Void>

    @GET("/user/findEmail/{userEmail}/")
    fun findUserEmail(@Path("userEmail") userEmail: String): Call<ExistsOrNot>

    @GET("/user")
    fun getUser(): Call<MutableList<User>>

    @GET("/user/{id}/")
    fun getUserSpecific(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<User>

    @GET("/closet/{id}")
    fun getCloth(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<MutableList<Cloth>>
    @Multipart
    @POST("/closet/{id}/")
    fun insertCloset(
        @Header("Authorization") token: String,
        @Path("id") user_id: Int,
        @Part("id") clothID: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Call<Void>

    @Multipart
    @POST("/closet/{id}/")
    fun insertCloset(
        @Header("Authorization") token: String,
        @Path("id") user_id: Int,
        @Part("id") clothID: RequestBody,
        @Part("category") category: RequestBody,
        @Part("season") season: MutableList<RequestBody>,
        @Part("dateBought") dateBought: RequestBody,
        @Part("dateLastWorn") dateLastWorn: RequestBody,
        @Part photo: MultipartBody.Part?
    ): Call<Void>

    @DELETE("/closet/{id}/{clothID}/")
    fun deleteCloset(
        @Header("Authorization") token: String,
        @Path("id") user_id: Int,
        @Path("clothID") clothID: Int
    ): Call<Void>
    @Multipart
    @PUT("/closet/{id}/{clothID}/")
    fun updateCloset(
        @Header("Authorization") token: String,
        @Path("id") user_id: Int,
        @Path("clothID") clothID: Int,
        @Part("id") id: RequestBody,
        @Part("category") category: RequestBody,
        @Part("season") season: MutableList<RequestBody>,
        @Part("dateBought") dateBought: RequestBody,
        @Part("dateLastWorn") dateLastWorn: RequestBody,
        @Part("outfit") coordiList: MutableList<RequestBody>?,
        @Part photo: MultipartBody.Part?
    ): Call<Void>


    @Multipart
    @POST("/{id}/outfit/")
    fun getOutfitId(
        @Path("id") user_id: Int,
        @Part("id") id: Int,
        @Header("Authorization") token: String,
        @Part("outfitName") outfitName: String
    ): Call<GetOutfitIdResponse>

    @Multipart
    @PATCH("/closet/{id}/{clothID}/")
    fun uploadOutfit(
        @Header("Authorization") token: String,
        @Path("id") user_id: Int,// 유저 아이디
        @Path("clothID") clothID: Int,// 옷 아이디
        @Part("id") id: Int,// 유저 아이디
        @Part("outfit") outfitID: Int
    ): Call<Void>


    @POST("/{id}/{outfitID}/addcloth/{clothID}/")
    fun addClothCoordi(
        @Header("Authorization") token: String,
        @Path("id") user_id: Int,// 유저 아이디
        @Path("clothID") clothID: Int,// 옷 아이디
        @Path("outfitID") outfitID: Int// 유저 아이디
    ): Call<Void>
    @DELETE("/{id}/{outfitID}/delcloth/{clothID}/")
    fun deleteClothCoordi(
        @Header("Authorization") token: String,
        @Path("id") user_id: Int,// 유저 아이디
        @Path("clothID") clothID: Int,// 옷 아이디
        @Path("outfitID") outfitID: Int// 유저 아이디
    ): Call<Void>

    @GET("/{id}/outfit/list")
    fun getCoordiList(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Call<MutableList<CoordiList>>

    @DELETE("/{id}/outfit/list/{outfitID}/")
    fun deleteOutfit(
        @Path("id") userID: Int,
        @Path("outfitID") outfitID: Int,
        @Header("Authorization") token: String
    ): Call<Void>


    @GET("/{id}/outfit/list/{outfitID}/")
    fun getOutFitDetail(
        @Path("id") userID: Int,
        @Path("outfitID") outfitID: Int,
        @Header("Authorization") token: String
    ): Call<CoordiList>


    @POST("/{id}/addfriend/{email}/")
    fun addFriend(
        @Path("id") userId: Int,
        @Path("email") email: String,
        @Header("Authorization") token: String
    ): Call<FriendAddResponse>

    @GET("/friendlist/accepted/{id}")
    fun getFriendList(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Call<MutableList<Friend>>


    @POST("/{id}/changepassword/")
    fun changePassword(
        @Path("id") id : Int,
        @Body body : ReqChangePasswordBody,
        @Header("Authorization") token: String
    ) : Call<Void>

    @POST("/passwordfind/{email}/")
    fun findPassword(
        @Path("email") id : String
    ) : Call<Void>
}
