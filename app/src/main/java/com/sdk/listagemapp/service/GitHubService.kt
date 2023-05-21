package com.sdk.listagemapp.service

import Repository
import android.provider.ContactsContract
import com.google.gson.JsonObject
import com.sdk.listagemapp.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface GitHubService {
    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{username}")
    fun getUserDetails(@Path("username") username: String): Call<User>

    @GET("users/{username}/avatar_url")
    fun getUserAvatarUrl(@Path("username") username: String): Call<String>

    @GET
    fun getUserRepositories(@Url url: String): Call<List<Repository>>
}
