package com.sdk.listagemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdk.listagemapp.service.GitHubService
import com.sdk.listagemapp.R
import com.sdk.listagemapp.model.User
import com.sdk.listagemapp.adapter.UserAdapter
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
        adapter = UserAdapter(::onItemClick)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)
        val call = service.getUsers()

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let {
                        Log.d("ListActivity", "Lista de usuários recebida: $userList")
                        adapter.setData(it)
                    }
                } else {
                    Log.e("ListActivity", "Erro na resposta da API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("ListActivity", "Falha na requisição: ${t.message}")
            }
        })
    }

    private fun onItemClick(user: User) {
        val intent = Intent(this, DetalhesActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}
