package com.example.beritaapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beritaapp.R
import com.example.beritaapp.adapters.Newsadapter
import com.example.beritaapp.util.Constants
import com.example.beritaapp.util.Resource
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    lateinit var viewModel: ActivityViewModel
    lateinit var newsAdapter: Newsadapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.hide()
        icBack.setOnClickListener {
            finish()
        }

        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        showCardView()
        setupRecyclerView()
        var job: Job? = null
        svSearch1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(Constants.SEARCH_NEWS_TIME_DELAY)
                    if(svSearch1.isNotEmpty()){
                        viewModel.getSearchNews(svSearch1.query.toString(), applicationContext )
                    }
                }
                return false
            }

        })

//        { editable ->
//            job?.cancel()
//            job = MainScope().launch {
//                delay(Constants.SEARCH_NEWS_TIME_DELAY)
//                editable?.let {
//                    if(editable.toString().isNotEmpty()){
//                        viewModel.getSearchNews(editable.toString(), applicationContext)
//                    }
//                }
//            }
//        }

    }

    private fun showCardView() {
        viewModel.searchNews.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            rvSearchNewss.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{ message ->
                        Toast.makeText(applicationContext, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

//    val scrollListener = object : RecyclerView.OnScrollListener(){
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                isScrolling = true
//            }
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//
//            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//            val visibleItemCount = layoutManager.childCount
//            val totalItemCount = layoutManager.itemCount
//
//            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
//            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
//            val isNotAtBeginning = firstVisibleItemPosition >= 0
//            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
//            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
//                    && isTotalMoreThanVisible && isScrolling
//            if(shouldPaginate){
//                viewModel.getBreakingNews("id")
//                isScrolling = false
//
//            }
//        }
//    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView(){
        newsAdapter = Newsadapter(applicationContext)
        newsAdapter.notifyDataSetChanged()
        rvSearchNewss?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }
}