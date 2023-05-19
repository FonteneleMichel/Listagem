package com.sdk.listagemapp.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sdk.listagemapp.model.User
import androidx.recyclerview.widget.RecyclerView
import com.sdk.listagemapp.R

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val userList: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(users: List<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textUsername: TextView = itemView.findViewById(R.id.textUsername)
        private val textBio: TextView = itemView.findViewById(R.id.textBio)

        fun bind(user: User) {
            textUsername.text = user.username
            textBio.text = user.bio
        }
    }
}
