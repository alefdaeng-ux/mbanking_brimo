package com.example.myapplication.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ApiResponse<UserData>>

    @FormUrlEncoded
    @POST("insert.php")
    fun registerUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("pin") pin: String
    ): Call<ApiResponse<Any>>

    @GET("read.php")
    fun getAllUsers(): Call<List<UserData>>

    @FormUrlEncoded
    @POST("update.php")
    fun updateUser(
        @Field("id") id: Int,
        @Field("password") password: String,
        @Field("pin") pin: String
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("delete.php")
    fun deleteUser(
        @Field("id") id: Int
    ): Call<ApiResponse<Any>>

    @FormUrlEncoded
    @POST("verify_pin.php")
    fun verifyPin(
        @Field("username") username: String,
        @Field("pin") pin: String
    ): Call<ApiResponse<Any>>
}
