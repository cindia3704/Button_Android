package com.example.button.startApp_1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.activity.CoordiDetailActivity
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.fragment.CoordiFragment
import com.example.button.startApp_1.network.RetrofitClient


class CoordiDetailItemAdapter(val activity: CoordiDetailActivity, val type : Int) :
    RecyclerView.Adapter<CoordiDetailItemAdapter.ViewHolder>() {

    companion object{

        val TYPE_TOP = 0
        val TYPE_BOTTOM = 1
        val TYPE_OUTER = 2
        val TYPE_DRESS = 3




    }


    var clothList = mutableListOf<Cloth>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.mycloset_clothes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return clothList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(activity)
            .load(RetrofitClient.imageBaseUrl+clothList[position].photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(holder.clothImage)


        holder.clothImage.setOnClickListener {

            when(type){
                TYPE_TOP -> {
                    activity.clickTop(clothList[position])
                }

                TYPE_BOTTOM -> {
                    activity.clickBottom(clothList[position])
                }

                CoordiItemAdapter.TYPE_DRESS -> {
                    activity.clickDress(clothList[position])
                }

                CoordiItemAdapter.TYPE_OUTER -> {
                    activity.clickOuter(clothList[position])
                }
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val clothImage: ImageView = view.findViewById(R.id.cloth_image)
    }

}
