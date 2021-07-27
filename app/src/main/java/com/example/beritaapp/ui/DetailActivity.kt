package com.example.beritaapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.models.Article
import com.example.beritaapp.models.Source
import com.google.android.material.snackbar.Snackbar
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
    lateinit var viewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        i = intent
        getIntentValue()
        setViewValue()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        val source = Source(null, name)
        val article = Article(id, author, content, descr, published, source, title, url, image)
        fab.setOnClickListener {
            viewModel.saveArticle(applicationContext,article)
            Snackbar.make(constraintDetail, "Berita berhasil disimpan", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun setViewValue() {
        Glide.with(this)
            .load(image)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}