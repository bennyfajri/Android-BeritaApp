package com.example.beritaapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.beritaapp.models.Article

@Dao
interface ArticleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}