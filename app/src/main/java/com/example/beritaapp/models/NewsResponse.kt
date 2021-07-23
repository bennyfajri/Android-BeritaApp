package com.example.beritaapp.models

import com.example.beritaapp.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)