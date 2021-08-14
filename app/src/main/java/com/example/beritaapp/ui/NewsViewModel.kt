package com.example.beritaapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.beritaapp.NewsApplication
import com.example.beritaapp.models.Article
import com.example.beritaapp.models.NewsResponse
import com.example.beritaapp.repository.NewsRepository
import com.example.beritaapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Response<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Response<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    val category = ""

//    init {
//        getBreakingNews("id", category)
//    }

//    fun getBreakingNews(countryCode: String, category: String) = viewModelScope.launch {
////        breakingNews.postValue(Resource.Loading())
////        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
////        breakingNews.postValue(handleBreakingNewsResponse((response)))
//        safeBreakingNewsCall(countryCode,category)
//    }

    fun getBreakingNews2(countryCode: String, category: String) = viewModelScope.launch {
//        breakingNews.postValue(Resource.Loading())
//        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
//        breakingNews.postValue(handleBreakingNewsResponse((response)))
        safeBreakingNewsCall(countryCode, category)

    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
//        searchNews.postValue(Resource.Loading())
//        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
//        searchNews.postValue(handleSearchNewsResponse(response))
        safeSearchNewsCall(searchQuery)
    }

//    private fun handleBreakingNewsResponse(
//        response: Response<NewsResponse>
//    ): Response<NewsResponse> {
//        if (response.isSuccessful) {
//            response.body()?.let { resultResponse ->
//                breakingNewsPage++
//                if (breakingNewsResponse == null) {
//                    breakingNewsResponse = resultResponse
//                } else {
//                    val oldArticle = breakingNewsResponse?.articles
//                    val newArticle = resultResponse.articles
//                    oldArticle?.addAll(newArticle)
//                }
//                return Resource.data(breakingNewsResponse ?: resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }

//    private fun handleSearchNewsResponse(
//        response: Response<NewsResponse>
//    ): Resource<NewsResponse> {
////        if (response.isSuccessful) {
////            response.body()?.let { resultResponse ->
////                searchNewsPage++
////                if (searchNewsResponse == null) {
////                    searchNewsResponse = resultResponse
////                } else {
////                    val oldArticle = searchNewsResponse?.articles
////                    val newArticle = resultResponse.articles
////                    oldArticle?.addAll(newArticle)
////                }
////                return Resource.Success(searchNewsResponse ?: resultResponse)
////            }
////        }
////        return Resource.Error(response.message())
//
//    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertData(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        try {
            if (hasInternetConnection()) {
                viewModelScope.launch {
                    val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                    breakingNews.value = response
                }
            } else {
                searchNews.postValue(null)
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(null)
                else -> searchNews.postValue(null)
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String, category: String) {
        try {
            if (hasInternetConnection()) {
                viewModelScope.launch {
                    val response =
                        newsRepository.getBreakingNews(countryCode, breakingNewsPage, category)
                    breakingNews.value = response
                }
            } else {
                breakingNews.value = null
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(null)
                else -> breakingNews.postValue(null)
            }
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}