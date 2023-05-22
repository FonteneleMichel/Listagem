package com.sdk.listagemapp.adapter

import Repository
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sdk.listagemapp.R

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {

    private var repositoryList: List<Repository> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
        return RepositoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val repository = repositoryList[position]
        holder.bind(repository)
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

    fun setData(repositories: List<Repository>) {
        repositoryList = repositories
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewRepositoryName: TextView = itemView.findViewById(R.id.textViewRepositoryName)
        private val textViewRepositoryDescription: TextView = itemView.findViewById(R.id.textViewRepositoryDescription)

        fun bind(repository: Repository) {
            textViewRepositoryName.text = repository.name
            textViewRepositoryDescription.text = repository.description
        }
    }
}
