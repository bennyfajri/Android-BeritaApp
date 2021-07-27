package com.example.beritaapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.beritaapp.db.ArticleDatabase
import com.example.beritaapp.models.Article
import com.example.beritaapp.models.LocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {
    fun saveArticle(context: Context, article: Article){
        val localDataSource = LocalDataSource(context)
        localDataSource.insertNews(article)
    }
}