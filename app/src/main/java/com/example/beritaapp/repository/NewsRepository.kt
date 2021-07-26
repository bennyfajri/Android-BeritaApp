package com.example.beritaapp.repository

import com.example.beritaapp.api.RetrofitInstance
import com.example.beritaapp.db.ArticleDatabase
import com.example.beritaapp.models.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit

class NewsRepository(val db: ArticleDatabase){

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
         RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    fun insertData(article: Article){

    }

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun  deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}