package com.sdk.listagemapp.view

import Repository
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sdk.listagemapp.R
import com.sdk.listagemapp.adapter.RepositoryAdapter
import com.sdk.listagemapp.model.User
import com.sdk.listagemapp.service.GitHubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetalhesActivity : AppCompatActivity() {
    private lateinit var textViewName: TextView
    private lateinit var textViewSelectedDetail: TextView
    private lateinit var imageViewProfile: ImageView
    private lateinit var recyclerViewRepositories: RecyclerView
    private lateinit var repositoryAdapter: RepositoryAdapter

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        textViewName = findViewById(R.id.textViewName)
        textViewSelectedDetail = findViewById(R.id.textViewSelectedDetail)
        imageViewProfile = findViewById(R.id.imageViewProfile)
        recyclerViewRepositories = findViewById(R.id.recyclerViewRepositories)
        repositoryAdapter = RepositoryAdapter()

        recyclerViewRepositories.layoutManager = LinearLayoutManager(this)
        recyclerViewRepositories.adapter = repositoryAdapter

        user = intent?.getParcelableExtra<User>("user")!!

        user.let {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(GitHubService::class.java)

            getUserDetails(service, it)
            getUserAvatarUrl(service, it)
            getUserRepositories(service, it.reposUrl)
        }
    }

    private fun getUserDetails(service: GitHubService, user: User) {
        val call = user.username?.let { service.getUserDetails(it) }

        call?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val userDetails = response.body()
                    userDetails?.let {
                        updateUserDetails(userDetails)
                        getUserRepositories(service, it.reposUrl) // Buscar os repositórios do usuário
                    }
                } else {
                    // Tratar o caso de resposta não bem-sucedida
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
            }
        })
    }

    private fun updateUserDetails(user: User) {
        textViewName.text = "Name: ${user.name}"
        textViewSelectedDetail.text = "Username: ${user.username}"

        if (!user.profileImageUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imageViewProfile)
        } else {
            imageViewProfile.setImageResource(R.drawable.placeholder)
        }
    }

    private fun getUserAvatarUrl(service: GitHubService, user: User) {
        val callAvatarUrl = user.username?.let { service.getUserAvatarUrl(it) }

        callAvatarUrl?.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                if (response.isSuccessful) {
                    val avatarUrl = response.body()
                    user.profileImageUrl = avatarUrl
                    updateUserProfileImage(user.profileImageUrl)
                } else {
                    // Tratar o caso de resposta não bem-sucedida
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
            }
        })
    }

    private fun updateUserProfileImage(profileImageUrl: String?) {
        profileImageUrl?.let {
            if (it.isNotEmpty()) {
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageViewProfile)
            } else {
                imageViewProfile.setImageResource(R.drawable.placeholder)
            }
        }
    }
    private fun updateRepositories(repositories: List<Repository>) {
        repositoryAdapter.setData(repositories)
    }
    private fun getUserRepositories(service: GitHubService, reposUrl: String?) {
        reposUrl?.let {
            val call = service.getUserRepositories(it)

            call.enqueue(object : Callback<List<Repository>> {
                override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>) {
                    if (response.isSuccessful) {
                        val repositories = response.body()
                        repositories?.let {
                            // Atualizar a lista de repositórios do usuário
                            updateRepositories(repositories)
                        }
                    } else {
                        // Tratar o caso de resposta não bem-sucedida
                    }
                }

                override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                }
            })
        }
    }
}

