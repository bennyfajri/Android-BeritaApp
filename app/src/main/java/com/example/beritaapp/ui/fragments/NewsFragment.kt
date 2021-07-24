package com.example.beritaapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beritaapp.R
import com.example.beritaapp.adapters.Newsadapter
import com.example.beritaapp.models.Article
import com.example.beritaapp.ui.MainActivity
import com.example.beritaapp.ui.NewsViewModel
import com.example.beritaapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.beritaapp.util.Resource
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFragment : Fragment(R.layout.fragment_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: Newsadapter

    val TAG = "NewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        showCardView()

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }

        newsAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply {
//
//            }
//            findNavController().navigate(
//                R.id.action_newsFragment_to_articleFragment,
//                bundle
//            )
        }

    }

    private fun showCardView() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{ message ->
                        Log.e(TAG, "An error occured: $message")
                        Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show()
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
        setupRecyclerView()
        showCardView()
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = Newsadapter(requireContext())
        newsAdapter.notifyDataSetChanged()
        rvSearchNews?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}