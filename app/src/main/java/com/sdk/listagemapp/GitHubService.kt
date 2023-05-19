package com.sdk.listagemapp

import com.sdk.listagemapp.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("users")
    fun getUsers(): Call<List<User>>
}