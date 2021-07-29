package com.example.beritaapp.ui

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beritaapp.db.ArticleDatabase
import com.example.beritaapp.models.Article
import com.example.beritaapp.repository.LocalDataSource
import com.example.beritaapp.models.NewsResponse
import com.example.beritaapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class ActivityViewModel: ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    fun saveArticle(context: Context, article: Article){
        val localDataSource = LocalDataSource(context)
        localDataSource.insertNews(article)
    }

    private fun handleBreakingNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(
        response: Response<NewsResponse>
    ): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getBreakingNews(context: Context, countryCode: String, category: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = LocalDataSource(context).getBreakingNews(countryCode, breakingNewsPage, category)
        breakingNews.postValue(handleBreakingNewsResponse((response)))
//        safeBreakingNewsCall(countryCode)
    }

    fun getSearchNews(searchQuery: String, context: Context) = viewModelScope.launch {
        val articleDatabase = ArticleDatabase.createDatabase(context)
        searchNews.postValue(Resource.Loading())
        val response = LocalDataSource(context).searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
//        safeSearchNewsCall(searchQuery)
    }



//    private suspend fun safeSearchNewsCall(searchQuery: String) {
//        searchNews.postValue(Resource.Loading())
//        try {
//            if (hasInternetConnection()) {
//                val response = NewsRepository().searchNews(searchQuery, searchNewsPage)
//                searchNews.postValue(handleSearchNewsResponse((response)))
//            } else {
//                searchNews.postValue(Resource.Error("No internet connection"))
//            }
//        } catch (t: Throwable) {
//            when(t){
//                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
//                else -> searchNews.postValue(Resource.Error("Conversion Error"))
//            }
//        }
//    }

//    private fun hasInternetConnection(): Boolean {
//        val connectivityManager = getApplication<NewsApplication>().getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val activeNetwork = connectivityManager.activeNetwork ?: return false
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//            return when {
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                else -> false
//            }
//        } else {
//            connectivityManager.activeNetworkInfo?.run {
//                return when (type) {
//                    ConnectivityManager.TYPE_WIFI -> true
//                    ConnectivityManager.TYPE_MOBILE -> true
//                    ConnectivityManager.TYPE_ETHERNET -> true
//                    else -> false
//                }
//            }
//        }
//        return false
//    }
}