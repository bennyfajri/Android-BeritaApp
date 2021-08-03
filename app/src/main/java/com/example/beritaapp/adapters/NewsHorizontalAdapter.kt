package com.example.beritaapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.models.Article
import com.example.beritaapp.ui.DetailActivity
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_news_vertical.view.*

class NewsHorizontalAdapter(context: Context) : RecyclerView.Adapter<NewsHorizontalAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private val differCallback = object  : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_news_vertical, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsHorizontalAdapter.ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(article.urlToImage)
                .into(imgNews)
            tvTitleH.text = article.title
            tvSourceH.text = article.source?.name
            setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("id", article.id)
                intent.putExtra("author", article.author)
                intent.putExtra("content", article.content)
                intent.putExtra("description", article.description)
                intent.putExtra("published", article.publishedAt)
                intent.putExtra("title", article.title)
                intent.putExtra("url", article.url)
                intent.putExtra("image", article.urlToImage)
                intent.putExtra("name", article.source?.name)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}