package com.example.beritaapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.models.Article
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var i: Intent
    lateinit var title: String
    lateinit var url: String
    lateinit var published: String
    lateinit var descr: String
    lateinit var content: String
    lateinit var author: String
    lateinit var name: String
    lateinit var image: String
    var id: Int = 0
    lateinit var viewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        i = intent
        getIntentValue()
        setViewValue()

        val article = Article(id, author, content, descr, published, null, title, url, image)
        fab.setOnClickListener {
            viewModel.saveArticle(article)
        }

    }

    private fun setViewValue() {
        Glide.with(this)
            .load(url)
            .into(ivArticleImage)
        tvTitle.text = title
        tvContent.text = content
        tvDetail.text = descr
        tvContent.text = content
        tvSource.text = author
    }

    private fun getIntentValue() {
        id = i.getIntExtra("id",0)
        name = i.getStringExtra("name").toString()
        title = i.getStringExtra("title").toString()
        url = i.getStringExtra("url").toString()
        published = i.getStringExtra("published").toString()
        descr = i.getStringExtra("description").toString()
        content = i.getStringExtra("content").toString()
        author = i.getStringExtra("author").toString()
        image = i.getStringExtra("image").toString()

    }
}