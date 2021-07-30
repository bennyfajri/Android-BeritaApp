package com.example.beritaapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.beritaapp.R
import com.example.beritaapp.models.Category
import com.example.beritaapp.ui.CategoryActivity
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(
    val context: Context,
val arrayList: ArrayList<Category>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName:TextView = itemView.findViewById(R.id.txt)
        var img: ImageView = itemView.findViewById(R.id.icCategory)
        var layout: LinearLayout = itemView.findViewById(R.id.lnCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val category = arrayList[position]

        Glide.with(holder.itemView.context)
            .load(category.icon)
            .apply(RequestOptions().override(55,55))
            .into(holder.img)

        holder.tvName.text = category.nama
        holder.img.setOnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("namaID", category.nama)
            intent.putExtra("nameEN", category.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }


}