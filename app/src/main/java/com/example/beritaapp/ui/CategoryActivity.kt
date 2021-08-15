package com.example.beritaapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beritaapp.R
import com.example.beritaapp.adapters.Newsadapter
import com.example.beritaapp.util.Constants
import com.example.beritaapp.util.Resource
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_news.*

class CategoryActivity : AppCompatActivity() {

    lateinit var viewModel: ActivityViewModel
    lateinit var newsAdapter: Newsadapter
    lateinit var i: Intent
    lateinit var namaID: String
    lateinit var nameEN: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        i = intent
        namaID = i.getStringExtra("namaID").toString()
        nameEN = i.getStringExtra("nameEN").toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = namaID

        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        showCardView()
        setupRecyclerView()
    }

    private fun showCardView() {
        viewModel.breakingNews.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
//                            rvCategory.setPadding(0,0,0,0)
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
        viewModel.getBreakingNews(applicationContext,"id", nameEN)
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar(){
        paginationBarCategory.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        paginationBarCategory.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView(){
        newsAdapter = Newsadapter(applicationContext)
        newsAdapter.notifyDataSetChanged()
        rvCategory?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@CategoryActivity)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}