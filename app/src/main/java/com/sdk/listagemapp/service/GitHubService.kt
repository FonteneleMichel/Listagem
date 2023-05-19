package com.sdk.listagemapp.service

import com.google.gson.JsonObject
import com.sdk.listagemapp.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{username}")
    fun getUserDetails(@Path("username") username: String): Call<User>


    @GET("users/{username}")
    fun getAllFields(@Path("username") username: String): Call<JsonObject>
}
