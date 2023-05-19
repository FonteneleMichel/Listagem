package com.sdk.listagemapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdk.listagemapp.GitHubService
import com.sdk.listagemapp.R
import com.sdk.listagemapp.model.User
import com.sdk.listagemapp.viewmodel.UserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = UserAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val call = service.getUsers()

        call.enqueueCallback(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let { adapter.setData(it) }
                } else {
                    // Tratar o caso de resposta não bem sucedida
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Tratar o caso de falha na requisição
            }
        })
    }
}

fun <T> Call<T>.enqueueCallback(callback: Callback<T>) {
    enqueue(callback)
}
