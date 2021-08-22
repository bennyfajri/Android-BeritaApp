package com.example.beritaapp.models

import com.example.beritaapp.R


object CategoryData {
    private val nama = arrayOf(
        "Bisnis", "Hiburan", "Kesehatan",
        "Sains", "Olahraga", "Teknologi",
        "Gaya Hidup", "Kecantikan", "Liburan",
        "Kuliner"
    )

    private val icon = intArrayOf(
       R.drawable.business, R.drawable.movie, R.drawable.health,
        R.drawable.science, R.drawable.sport, R.drawable.techno,
        R.drawable.style,R.drawable.beauty,R.drawable.travel,
        R.drawable.culinary,
    )

    private val name = arrayOf(
        "business", "entertainment", "health",
        "science", "sports", "technology",
        "lifestyle", "beauty", "travelling",
        "food"
    )

    private val color = intArrayOf(
        R.color.ltblue, R.color.blue_d, R.color.blue_l,
        R.color.pinky,R.color.oren,R.color.kuning,
        R.color.hijau,R.color.tosca,R.color.biru_tua,
        R.color.abu
    )

    private val image = intArrayOf(
        R.drawable.banner1,
        R.drawable.banner2,
        R.drawable.banner3
    )

    private val desc = arrayOf(
        "https://kitabisa.com/explore/all",
        "https://kitabisa.com/explore/zakat",
        "https://galangdana.kitabisa.com/"
    )

    val listData: ArrayList<Category>
    get() {
        val list = arrayListOf<Category>()
        for(position in name.indices){
            val category = Category()
            category.name = name[position]
            category.icon = icon[position]
            category.nama = nama[position]
            category.color = color[position]
            list.add(category)
        }
        return list
    }

    val listSlider : ArrayList<SliderItem>
    get(){
        val list = arrayListOf<SliderItem>()
        for(position in image.indices){
            val item = SliderItem()
            item.img = image[position]
            item.desc = desc[position]
            list.add(item)
        }
        return list
    }


}