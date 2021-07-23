package com.example.beritaapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beritaapp.R
import com.example.beritaapp.adapters.Newsadapter
import com.example.beritaapp.ui.MainActivity
import com.example.beritaapp.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_bookmarks.*
import kotlinx.android.synthetic.main.fragment_news.*

class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    lateinit var viewModel : NewsViewModel
    lateinit var newsAdapter : Newsadapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_bookmarksFragment_to_articleFragment2,
                bundle
            )
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = Newsadapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
}