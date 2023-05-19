package com.sdk.listagemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        call.enqueueCallbackExtension(object : Callback<List<User>> {
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

    private fun onItemClick(user: User) {
        // Criar uma Intent para a nova atividade
        val intent = Intent(this, DetalhesActivity::class.java)
        // Passar os dados do usuário para a nova atividade
        intent.putExtra("user", user)
        // Iniciar a nova atividade
        startActivity(intent)
    }

    private fun <T> Call<T>.enqueueCallbackExtension(callback: Callback<T>) {
        enqueue(callback)
    }
}
