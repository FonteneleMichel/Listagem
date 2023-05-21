package com.sdk.listagemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdk.listagemapp.service.GitHubService
import com.sdk.listagemapp.R
import com.sdk.listagemapp.model.User
import com.sdk.listagemapp.adapter.UserAdapter
import com.sdk.listagemapp.model.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var service: GitHubService
    private lateinit var editTextSearch: EditText
    private var userList: List<User> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        editTextSearch = findViewById(R.id.editTextSearch)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = UserAdapter(::onItemClick)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(GitHubService::class.java)

        val call = service.getUsers()

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val userList = response.body()
                    userList?.let {
                        Log.d("ListActivity", "Lista de usuários recebida: $userList")
                        this@ListActivity.userList =
                            userList // Armazena a lista original de usuários
                        adapter.setData(userList)
                    }
                } else {
                    Log.e("ListActivity", "Erro na resposta da API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("ListActivity", "Falha na requisição: ${t.message}")
            }
        })

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                searchUser(query)
            }
        })
    }

    private fun onItemClick(user: User) {
        val intent = Intent(this, DetalhesActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun searchUser(query: String) {
        // Verifique se a query não está vazia
        if (query.isNotEmpty()) {
            // Realize a chamada à API para buscar o usuário com base na query
            val call = service.searchUsers(query)

            call.enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        val searchResponse = response.body()
                        val userList = searchResponse?.items
                        userList?.let {
                            Log.d("ListActivity", "Lista de usuários recebida: $userList")
                            this@ListActivity.userList =
                                userList // Armazena a lista original de usuários
                            adapter.setData(userList)
                        }
                    } else {
                        Log.e("ListActivity", "Erro na resposta da API: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    Log.e("ListActivity", "Falha na requisição: ${t.message}")
                }
            })
        } else {
            // Se a query estiver vazia, retorne à lista original de usuários
            adapter.setData(userList)
        }
    }
}