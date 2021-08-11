package com.example.beritaapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.models.Article
import kotlinx.android.synthetic.main.item_news_vertical.view.*

class NewsAdapter2: RecyclerView.Adapter<NewsAdapter2.ViewHolder>(){

    private var arrayList = emptyList<Article>()
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

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    fun setData(newList: List<Article>){
        arrayList = newList
        notifyDataSetChanged()
    }
}

