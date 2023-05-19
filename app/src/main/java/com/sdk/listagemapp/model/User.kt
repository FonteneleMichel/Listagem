package com.sdk.listagemapp.model

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("login")
    val username: String,
    @SerializedName("bio")
    val bio: String?
)