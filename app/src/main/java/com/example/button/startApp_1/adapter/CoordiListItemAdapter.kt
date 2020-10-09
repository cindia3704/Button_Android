package com.example.button.startApp_1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.activity.CoordiListActivity
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.network.RetrofitClient


class CoordiListItemAdapter(val activity: CoordiListActivity) :
    RecyclerView.Adapter<CoordiListItemAdapter.ViewHolder>() {


    var isEdit = false
    var myCoordiList = mutableListOf<CoordiList>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    fun updateEdit() {
        isEdit = !isEdit
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.coordi_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myCoordiList.size
    }
    fun deleteItem(item : CoordiList){
        myCoordiList.remove(item)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var coordItem = myCoordiList[position]
        holder.closetName.setText(coordItem.outfitName + "")

        var closetLists = coordItem.clothes

        when(closetLists.size){
            0 -> {
                holder.closetTop.setImageResource(R.drawable.circle)
                holder.closetBottom.setImageResource(R.drawable.circle)
            }

            1 -> {
                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[0].photo)
                    .placeholder(R.drawable.circle)
                    .apply(RequestOptions.circleCropTransform()).into(holder.closetTop)

                holder.closetBottom.setImageResource(R.drawable.circle)
            }

            else -> {
                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[closetLists.size -2].photo)
                    .placeholder(R.drawable.circle)
                    .apply(RequestOptions.circleCropTransform()).into(holder.closetTop)

                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[closetLists.size -1].photo)
                    .placeholder(R.drawable.circle)
                    .apply(RequestOptions.circleCropTransform()).into(holder.closetBottom)
            }

        }


        holder.delete.visibility = if (isEdit) View.VISIBLE else View.GONE

        holder.content.setOnClickListener {

            if (isEdit) {
                activity.deleteItem(myCoordiList[position])
            } else {
                activity.clickItem(myCoordiList[position])
            }
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val content: ConstraintLayout = view.findViewById(R.id.content)
        val closetTop: ImageView = view.findViewById(R.id.closetTop)
        val closetBottom: ImageView = view.findViewById(R.id.closetBottom)
        val delete: ImageView = view.findViewById(R.id.delete)
        val closetName: TextView = view.findViewById(R.id.closetName)
    }

}
