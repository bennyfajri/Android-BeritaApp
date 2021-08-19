package com.example.beritaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.beritaapp.R
import com.example.beritaapp.models.SliderItem
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(val context: Context, val sliderData: ArrayList<SliderItem>) : SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {

    inner class SliderAdapterViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {
        var img: ImageView = itemView!!.findViewById(R.id.imgSlide)
    }

    override fun getCount(): Int {
        return sliderData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val inflate: View =
            LayoutInflater.from(parent?.getContext()).inflate(R.layout.slider_layout, null)
        return SliderAdapterViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder?, position: Int) {
        val item = sliderData[position]
        Glide.with(viewHolder!!.itemView)
            .load(item.img)
            .fitCenter()
            .into(viewHolder.img)
    }


}