package com.example.quizonline.network

import com.example.quizonline.network.model.Token
import com.example.quizonline.network.model.topic.TopicDataItem
import com.example.quizonline.network.model.User
import com.example.quizonline.network.model.quiz.QuizListItem
import com.example.quizonline.network.model.user.UserListItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServise {
    @POST("/api/auth/login")
    fun loginUser(@Body user: User): Call<Token>


    // get topic acces topik request bareer token
    @GET("/api/topic")
    fun getTopics(@Header("Authorization") token: String): Call<ArrayList<TopicDataItem>>

    @GET("/api/results/{id}")
    fun getScore(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ArrayList<UserListItem>>


    // id quiz defaul 1
    @GET("/api/quizes/{id}")
    fun getQuiz(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Call<ArrayList<QuizListItem>>
}