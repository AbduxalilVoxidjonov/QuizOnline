package com.example.quizonline.Adapter

import android.annotation.SuppressLint
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizonline.databinding.UserItemBinding
import com.example.quizonline.network.model.user.UserList
import com.example.quizonline.network.model.user.UserListItem

class UserListAdapter(private val userList: UserList) :
    RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList.item[position])
    }

    override fun getItemCount(): Int {
        return userList.item.size
    }

    class UserViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(userData: UserListItem) {
            binding.apply {
                username.text = "Username: ${userData.username}"
                topicName.text = "Topic: ${userData.topicName}"
                score.text = "Score: ${userData.correctTests}"
                takenTime.text = "Taken time: ${userData.takenTime}"
            }
        }
    }
}