package com.example.quizonline.network.model.quiz

data class QuizListItem(
    val caseType: String,
    val korean: String,
    val options: List<String>,
    val uzbek: String
)