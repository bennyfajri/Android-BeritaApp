package com.example.beritaapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.beritaapp.R
import com.example.beritaapp.ui.MainActivity
import com.example.beritaapp.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_bookmarks) {

    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }
}