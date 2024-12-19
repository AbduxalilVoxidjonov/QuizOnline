package com.example.quizonline.network.model.user

data class UserListItem(
    val correctTests: Int,
    val dateTime: String,
    val takenTime: Int,
    val topicName: String,
    val totalTests: Int,
    val username: String
)