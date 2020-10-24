package com.example.button.startApp_1.adapter

import android.text.TextUtils
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

        holder.closetDress.visibility = View.INVISIBLE
        holder.closetTop.visibility = View.INVISIBLE
        holder.closetBottom.visibility = View.INVISIBLE
        holder.closetOuter.visibility = View.INVISIBLE

        for(i in 0 until closetLists.size){
            if(TextUtils.equals(closetLists[i].category,"TOP")){
                holder.closetDress.visibility = View.INVISIBLE

                holder.closetTop.visibility = View.VISIBLE
                holder.closetBottom.visibility = View.VISIBLE

                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                    .placeholder(R.drawable.circle)
                    .apply(RequestOptions.circleCropTransform()).into(holder.closetTop)
            }

            if(TextUtils.equals(closetLists[i].category,"BOTTOM")){
                holder.closetDress.visibility = View.INVISIBLE

                holder.closetTop.visibility = View.VISIBLE
                holder.closetBottom.visibility = View.VISIBLE

                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                    .placeholder(R.drawable.circle)
                    .apply(RequestOptions.circleCropTransform()).into(holder.closetBottom)
            }

            if(TextUtils.equals(closetLists[i].category,"OUTER")){

                holder.closetOuter.visibility = View.VISIBLE

                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                    .placeholder(R.drawable.circle)
                    .apply(RequestOptions.circleCropTransform()).into(holder.closetOuter)
            }

            if(TextUtils.equals(closetLists[i].category,"DRESS")){
                holder.closetDress.visibility = View.VISIBLE

                holder.closetTop.visibility = View.INVISIBLE
                holder.closetBottom.visibility = View.INVISIBLE

                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                    .placeholder(R.drawable.circle)
                    .apply(RequestOptions.circleCropTransform()).into(holder.closetDress)
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
        val closetDress: ImageView = view.findViewById(R.id.closetDress)
        val closetOuter: ImageView = view.findViewById(R.id.closetOuter)
        val delete: ImageView = view.findViewById(R.id.delete)
        val closetName: TextView = view.findViewById(R.id.closetName)
    }

}
