package com.example.beritaapp

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)