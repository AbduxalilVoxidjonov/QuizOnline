package com.example.quizonline

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.quizonline.databinding.ActivityQuizBinding
import com.example.quizonline.databinding.ScoreDialogBinding
import com.example.quizonline.network.ApiClient
import com.example.quizonline.network.ApiServise
import com.example.quizonline.network.model.quiz.QuizListItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityQuizBinding.inflate(layoutInflater) }

    companion object {
        var questionModelList: ArrayList<QuizListItem> = arrayListOf()
    }

    var currentQuestionIndex = 0;
    var selectedAnswer = ""
    var score = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)
        }

        getQuiz()

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

                    if (quizList != null) {
                        questionModelList = quizList
                        loadQuestions()
                    } else {
                        Toast.makeText(this@QuizActivity, "Failed to get quiz", Toast.LENGTH_SHORT)
                            .show()
                    }

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


    private fun loadQuestions(
    ) {
        selectedAnswer = ""
        if (currentQuestionIndex == questionModelList.size) {
            finishQuiz()
            return
        }

        try {
            binding.apply {
                questionCountTextView.text =
                    "Question ${currentQuestionIndex + 1}/ ${questionModelList.size} "
                questionProgressIndicator.progress =
                    (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
                if (currentQuestionIndex == questionModelList.size - 1) {
                    nextBtn.text = "Finish"
                } else {
                    nextBtn.text = "Next"
                }
                when (questionModelList[currentQuestionIndex].caseType) {
                    "kr_uz" -> {
                        questionTextView.text = questionModelList[currentQuestionIndex].korean
                        btn0.text = questionModelList[currentQuestionIndex].options[0]
                        btn1.text = questionModelList[currentQuestionIndex].options[1]
                        btn2.text = questionModelList[currentQuestionIndex].options[2]
                        btn3.text = questionModelList[currentQuestionIndex].options[3]
                    }

                    "uz_kr" -> {
                        questionTextView.text = questionModelList[currentQuestionIndex].uzbek
                        btn0.text = questionModelList[currentQuestionIndex].options[0]
                        btn1.text = questionModelList[currentQuestionIndex].options[1]
                        btn2.text = questionModelList[currentQuestionIndex].options[2]
                        btn3.text = questionModelList[currentQuestionIndex].options[3]
                    }

                    "writing" -> {
                        currentQuestionIndex++
                    }
                }
            }


        } catch (e: Exception) {
            return
        }

    }

    private fun finishQuiz() {
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat()) * 100).toInt()

        val dialogBinding = ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgresIndicator.progress = percentage
            scoreProgresText.text = "$percentage %"
            if (percentage > 60) {
                scoreTitle.text = "Congrats! You have passed"
                scoreTitle.setTextColor(Color.BLUE)
            } else {
                scoreTitle.text = "Oops! You have failed"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text = "$score out of $totalQuestions are correct"
            scoreFinishButton.setOnClickListener {
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }

    override fun onClick(view: View?) {
        try {

            binding.apply {
                btn0.setBackgroundColor(getColor(R.color.grey))
                btn1.setBackgroundColor(getColor(R.color.grey))
                btn2.setBackgroundColor(getColor(R.color.grey))
                btn3.setBackgroundColor(getColor(R.color.grey))
            }

            val clickedBtn = view as Button
            if (clickedBtn.id == R.id.next_btn) {
                //next button is clicked
                if (selectedAnswer.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Please select answer to continue",
                        Toast.LENGTH_SHORT
                    ).show()
                    return;
                }
                if (questionModelList[currentQuestionIndex].caseType == "kr_uz") {
                    if (selectedAnswer == questionModelList[currentQuestionIndex].uzbek) {
                        score++
                    }
                } else if (questionModelList[currentQuestionIndex].caseType == "uz_kr") {
                    if (selectedAnswer == questionModelList[currentQuestionIndex].korean) {
                        score++
                    }
                } else if (questionModelList[currentQuestionIndex].caseType == "writing") {
                    score++
                }

                currentQuestionIndex++
                loadQuestions()
            } else {
                //options button is clicked
                selectedAnswer = clickedBtn.text.toString()
                clickedBtn.setBackgroundColor(getColor(R.color.orange))
            }
        } catch (e: Exception) {
            return
        }
    }
}
