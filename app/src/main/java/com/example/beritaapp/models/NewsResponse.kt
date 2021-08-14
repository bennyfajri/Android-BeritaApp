package com.example.beritaapp.models

data class NewsResponse(
    val articles: ArrayList<Article>,
    val status: String,
    val totalResults: Int
)