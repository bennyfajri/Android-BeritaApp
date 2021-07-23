package com.example.beritaapp.ui

import androidx.lifecycle.ViewModel
import com.example.beritaapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

}