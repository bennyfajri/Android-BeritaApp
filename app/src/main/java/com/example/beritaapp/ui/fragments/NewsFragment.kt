package com.example.beritaapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beritaapp.R
import com.example.beritaapp.adapters.CategoryAdapter
import com.example.beritaapp.adapters.Newsadapter
import com.example.beritaapp.models.Category
import com.example.beritaapp.models.CategoryData
import com.example.beritaapp.ui.MainActivity
import com.example.beritaapp.ui.NewsViewModel
import com.example.beritaapp.ui.SearchActivity
import com.example.beritaapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.beritaapp.util.Resource
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: Newsadapter
    private var list: ArrayList<Category> = arrayListOf()


    val TAG = "NewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        showCardView()


        slNews.setOnRefreshListener {
            Handler().postDelayed(Runnable {
                slNews.isRefreshing = false
                setupRecyclerView()
                showCardView()
            }, 500)
        }

        etSearch.setOnClickListener {
            val intent = Intent(context, SearchActivity::class.java)
            startActivity(intent)
        }


        rvKategori.setHasFixedSize(true)
        list.addAll(CategoryData.listData)
        showCategory()


    }

    private fun showCategory() {
        rvKategori.layoutManager = GridLayoutManager(context, 3)
        val adapter = CategoryAdapter(requireContext(), list)
        rvKategori.adapter = adapter

    }


    private fun showCardView() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            rvSearchNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{ message ->
                        Toast.makeText(context, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
//        setupRecyclerView()
        showCardView()
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("id")
                isScrolling = false

            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = Newsadapter(requireContext())
        newsAdapter.notifyDataSetChanged()
        rvSearchNews?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.scrollListener)
        }
    }
}