package com.example.beritaapp.repository

import android.content.Context
import com.example.beritaapp.api.RetrofitInstance
import com.example.beritaapp.db.ArticleDatabase
import com.example.beritaapp.models.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalDataSource(context: Context) {
    private val articleDatabase = ArticleDatabase.createDatabase(context)
    private val articleDao = articleDatabase.getArticleDao()

    fun insertNews(article: Article){
        CoroutineScope(Dispatchers.Main).launch {
            articleDao.upsert(article)
        }
    }
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int, category: String) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber,category)

    suspend fun deleteArticle(article: Article) {
        CoroutineScope(Dispatchers.Main).launch {
            articleDao.deleteArticle(article)
        }
    }

    fun getSavedNews(){
        CoroutineScope(Dispatchers.Main).launch {
            articleDao.getAllArticles()
        }
    }

}