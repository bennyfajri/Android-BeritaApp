package com.example.beritaapp.ui

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.models.Article
import com.example.beritaapp.models.Source
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
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
    lateinit var article: Article
    var id: Int = 0
    var isSaved = false
    lateinit var viewModel: ActivityViewModel
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        i = intent
        getIntentValue()
        setViewValue()
        bookmarked()
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            fab.isClickable = false
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        val source = Source(null, name)
        article = Article(id, author, content, descr, published, source, title, url, image)
        fab.setOnClickListener {
            if (isSaved == false) {
                viewModel.saveArticle(applicationContext, article)
                Snackbar.make(constraintDetail, "Berita berhasil disimpan", Snackbar.LENGTH_LONG)
                    .show()
                fab.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@DetailActivity, R.color.red))
                isSaved = true
                if (id == 0) {
                    fab.isClickable = false
                }

            } else {
                val delete = viewModel.deleteArticle(applicationContext, article)
                if (delete.isCompleted) {
                    Snackbar.make(constraintDetail, "Berita berhasil dihapus", Snackbar.LENGTH_LONG)
                        .show()
                    fab.imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            this@DetailActivity,
                            R.color.white
                        )
                    )
                    isSaved = false

                }
            }

        }

    }

    private fun bookmarked() {
        if (id != 0) {
            isSaved = true
            fab.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this@DetailActivity, R.color.red))
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
        id = i.getIntExtra("id", 0)
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
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}