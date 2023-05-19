package com.sdk.listagemapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.gson.JsonObject
import com.sdk.listagemapp.service.GitHubService
import com.sdk.listagemapp.R
import com.sdk.listagemapp.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetalhesActivity : AppCompatActivity() {
    private lateinit var textViewName: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var textViewSelectedDetail: TextView
    private lateinit var textViewRepositories: TextView

    private val detailOptions: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        textViewName = findViewById(R.id.textViewName)
        textViewEmail = findViewById(R.id.textViewEmail)
        textViewSelectedDetail = findViewById(R.id.textViewSelectedDetail)
        textViewRepositories = findViewById(R.id.textViewRepositories)

        val user = intent?.getParcelableExtra<User>("user")
        user?.let { getUserDetails(it) }
    }

    private fun getUserDetails(user: User) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val call = user.username?.let { service.getUserDetails(it) }

        call?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val userDetails = response.body()
                    userDetails?.let {
                        updateDetailsOptions(it)
                        updateUserDetails(it)
                    }
                } else {
                    // Tratar o caso de resposta não bem-sucedida
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Tratar o caso de falha na requisição
            }
        })
    }

    private fun updateDetailsOptions(user: User) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val call = user.username?.let { service.getAllFields(it) }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    jsonObject?.let {
                        val fields = it.keySet().toList()
                        detailOptions.clear()
                        detailOptions.addAll(fields)
                    }
                } else {
                    // Tratar o caso de resposta não bem-sucedida
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Tratar o caso de falha na requisição
            }
        })
    }

    private fun updateUserDetails(user: User) {
        textViewName.text = "Name: ${user.name}"
        textViewEmail.text = "Email: ${user.email}"
        textViewSelectedDetail.text = "Username: ${user.username}"
        textViewRepositories.text = "Repositories: ${user.repositoryList?.joinToString(", ")}"
    }
}
