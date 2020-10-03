package com.example.button.startApp_1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.network.RetrofitClient


class ClothAdapter(val inflater: LayoutInflater, val context : Context) : RecyclerView.Adapter<ClothAdapter.ViewHolder>() {
    private var clothList = mutableListOf<Cloth>()
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val clothImage: ImageView = itemView.findViewById(R.id.cloth_image)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= inflater.inflate(R.layout.mycloset_clothes,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clothList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(RetrofitClient.baseUrl+clothList[position].photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(holder.clothImage);
    }




    fun setClothList(data : MutableList<Cloth>){
        clothList = data
        notifyDataSetChanged()
    }
}