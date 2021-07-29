package com.example.beritaapp.models

import com.example.beritaapp.R

object CategoryData {
    private val name = arrayOf("Bisnis", "Hiburan",
        "Kesehatan", "Sains", "Olahraga", "Teknologi"
    )

    private val icon = intArrayOf(
       R.drawable.business, R.drawable.entertainment, R.drawable.healhh,
        R.drawable.science, R.drawable.sports, R.drawable.technology
    )

    private val color = arrayOf(
        "#0000fc", "#b00000", "#00d93d",
        "#6c00d9", "#d9d900", "#00add9"
    )

    val listData: ArrayList<Category>
    get() {
        val list = arrayListOf<Category>()
        for(position in name.indices){
            val category = Category()
            category.name = name[position]
            category.icon = icon[position]
            category.color = color[position]
            list.add(category)
        }
        return list
    }

}