package com.example.beritaapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.models.Article
import com.example.beritaapp.ui.DetailActivity
import kotlinx.android.synthetic.main.item_news_vertical.view.*

class NewsAdapter2: RecyclerView.Adapter<NewsAdapter2.ViewHolder>(){

    private var arrayList = ArrayList<Article>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter2.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_vertical, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val news = arrayList[position]
        holder.itemView.tvSourceH.text = news.source?.name
        holder.itemView.tvTitleH.text = news.title
        Glide.with(holder.itemView.context)
            .load(news.urlToImage)
            .into(holder.itemView.imgNews)

        holder.itemView.cvNews.setOnClickListener{
//                onItemClickListener?.let { it(article) }
            val article = arrayList[position]
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
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
            holder.itemView.context?.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun setData(newList: ArrayList<Article>){
//        arrayList.clear()
        arrayList = newList
        notifyDataSetChanged()
    }
}

