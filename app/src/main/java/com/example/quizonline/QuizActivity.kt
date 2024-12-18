package com.example.quizonline

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizonline.databinding.ActivityQuizBinding
import com.example.quizonline.network.ApiClient
import com.example.quizonline.network.ApiServise
import com.example.quizonline.network.model.quiz.QuizListItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {
    private val binding by lazy { ActivityQuizBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }

    fun getQuiz() {
        val topicId = intent.getIntExtra("topicId", 0)
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPref.getString("access_token", null)

        // Check if token is null before making the request
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token is missing or invalid", Toast.LENGTH_SHORT)
                .show()
            return
        }


        val apiService = ApiClient.getRetrofit().create(ApiServise::class.java)
        val call =
            apiService.getQuiz(
                "Bearer $token",
                topicId
            )  // Assuming the token is passed as "Bearer <token>"

        call.enqueue(object : Callback<ArrayList<QuizListItem>> {
            override fun onResponse(
                call: Call<ArrayList<QuizListItem>>,
                response: Response<ArrayList<QuizListItem>>
            ) {
                if (response.isSuccessful) {
                    val quizList = response.body()


                } else {
                    Toast.makeText(this@QuizActivity, "Failed to get quiz", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ArrayList<QuizListItem>>, t: Throwable) {
                Toast.makeText(this@QuizActivity, "Failed to get quiz", Toast.LENGTH_SHORT)
                    .show()
            }

        })

    }
}