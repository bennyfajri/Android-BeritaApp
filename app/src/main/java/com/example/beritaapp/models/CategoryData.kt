package com.example.beritaapp.models

import com.example.beritaapp.R

object CategoryData {
    private val nama = arrayOf("Bisnis", "Hiburan",
        "Kesehatan", "Sains", "Olahraga", "Teknologi"
    )

    private val icon = intArrayOf(
       R.drawable.business, R.drawable.entertainment, R.drawable.healhh,
        R.drawable.science, R.drawable.sports, R.drawable.technology
    )

    private val name = arrayOf(
        "business", "entertainment", "health",
        "science", "sports", "technology"
    )

    val listData: ArrayList<Category>
    get() {
        val list = arrayListOf<Category>()
        for(position in name.indices){
            val category = Category()
            category.name = name[position]
            category.icon = icon[position]
            category.nama = nama[position]
            list.add(category)
        }
        return list
    }

}