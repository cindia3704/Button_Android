package com.example.button.startApp_1.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.activity.CoordiListActivity
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.network.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class CoordiListItemAdapter(val activity: CoordiListActivity) :
    RecyclerView.Adapter<CoordiListItemAdapter.ViewHolder>() {


    var isEdit = false
    var userId = 0
    var friendId = 0// 0 이 아닐 경우 친구 코디를 로딩한 경우
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

    fun deleteItem(item: CoordiList) {
        myCoordiList.remove(item)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var coordItem = myCoordiList[position]
        holder.closetName.setText(coordItem.outfitName + "")
        if(coordItem.id==coordItem.outfitBy){

        }
        else {
            RetrofitClient.retrofitService.getUserSpecific(
                coordItem.outfitBy,
                "Token " + RetrofitClient.token
            ).enqueue(object :
                retrofit2.Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {

                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val nickname = response.body()!!.userNickName
                    holder.outfitBy.visibility = View.VISIBLE
                    holder.closetfrom_id.setText(nickname)
                }
            })
        }

        var closetLists = coordItem.clothes
        var corby=coordItem.outfitBy

        holder.cvDress.visibility = View.INVISIBLE
        holder.cvTop.visibility = View.INVISIBLE
        holder.cvBottom.visibility = View.INVISIBLE
        holder.cvOuter.visibility = View.INVISIBLE
        holder.cvTop_only.visibility = View.INVISIBLE
        holder.cvBottom_only.visibility = View.INVISIBLE
        holder.cvDress_only.visibility = View.INVISIBLE



        if(closetLists.size<=2){
            if(closetLists.size==1){
                if(TextUtils.equals(closetLists[0].category,"DRESS")){
                    holder.cvDress_only.visibility = View.VISIBLE
                    Glide.with(activity)
                        .load(RetrofitClient.imageBaseUrl + closetLists[0].photo)
                        .placeholder(R.drawable.circle)
                        .into(holder.closetDress_only)
                }
            }
            else{
                for(i in 0 until closetLists.size) {
                    if (TextUtils.equals(closetLists[i].category, "TOP")) {
//                        holder.cvDres_only.visibility = View.INVISIBLE

                        holder.cvTop_only.visibility = View.VISIBLE
//                        holder.cvBottom.visibility = View.VISIBLE

                        Glide.with(activity)
                            .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                            .placeholder(R.drawable.circle)
                            .into(holder.closetTop_only)
                    }

                    if (TextUtils.equals(closetLists[i].category, "BOTTOM")) {
//                        holder.cvDress.visibility = View.INVISIBLE
//
//                        holder.cvTop.visibility = View.VISIBLE
                        holder.cvBottom_only.visibility = View.VISIBLE

                        Glide.with(activity)
                            .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                            .placeholder(R.drawable.circle)
                            .into(holder.closetBottom_only)
                    }
                    if(TextUtils.equals(closetLists[i].category,"OUTER")){

                        holder.cvOuter.visibility = View.VISIBLE

                        Glide.with(activity)
                            .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                            .placeholder(R.drawable.circle).into(holder.closetOuter)
                    }

                    if(TextUtils.equals(closetLists[i].category,"DRESS")){
                        holder.cvDress.visibility = View.VISIBLE

                        holder.cvTop.visibility = View.INVISIBLE
                        holder.cvBottom.visibility = View.INVISIBLE

                        Glide.with(activity)
                            .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                            .placeholder(R.drawable.circle)
                            .into(holder.closetDress)
                    }
                }
            }
        }
        else {
            for (i in 0 until closetLists.size) {
                if (TextUtils.equals(closetLists[i].category, "TOP")) {
                    holder.cvDress.visibility = View.INVISIBLE

                    holder.cvTop.visibility = View.VISIBLE
                    holder.cvBottom.visibility = View.VISIBLE

                    Glide.with(activity)
                        .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                        .placeholder(R.drawable.circle)
                        .into(holder.closetTop)
                }

                if (TextUtils.equals(closetLists[i].category, "BOTTOM")) {
                    holder.cvDress.visibility = View.INVISIBLE

                    holder.cvTop.visibility = View.VISIBLE
                    holder.cvBottom.visibility = View.VISIBLE

                    Glide.with(activity)
                        .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                        .placeholder(R.drawable.circle)
                        .into(holder.closetBottom)
                }

                if (TextUtils.equals(closetLists[i].category, "OUTER")) {

                    holder.cvOuter.visibility = View.VISIBLE

                    Glide.with(activity)
                        .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                        .placeholder(R.drawable.circle).into(holder.closetOuter)
                }

                if (TextUtils.equals(closetLists[i].category, "DRESS")) {
                    holder.cvDress.visibility = View.VISIBLE

                    holder.cvTop.visibility = View.INVISIBLE
                    holder.cvBottom.visibility = View.INVISIBLE

                    Glide.with(activity)
                        .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                        .placeholder(R.drawable.circle)
                        .into(holder.closetDress)
                }

            }
        }


        holder.delete.visibility = if (isEdit) {
            if (friendId != 0 && coordItem.outfitBy == userId) {
                View.VISIBLE
            } else {
                View.GONE
            }
        } else {
            View.GONE
        }


        holder.content.setOnClickListener {

            if (isEdit) {
                activity.deleteItem(myCoordiList[position])
            } else {
                activity.clickItem(myCoordiList[position])
            }
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cvTop: CardView = view.findViewById(R.id.cvTop)
        val cvBottom: CardView = view.findViewById(R.id.cvBottom)
        val cvDress: CardView = view.findViewById(R.id.cvDress)
        val cvOuter: CardView = view.findViewById(R.id.cvOuter)
        val cvTop_only:CardView = view.findViewById(R.id.cvTop_only)
        val cvBottom_only: CardView = view.findViewById(R.id.cvBottom_only)
        val cvDress_only: CardView = view.findViewById(R.id.cvDress_only)

        val content: ConstraintLayout = view.findViewById(R.id.content)
        val closetTop: ImageView = view.findViewById(R.id.closetTop)
        val closetBottom: ImageView = view.findViewById(R.id.closetBottom)
        val closetDress: ImageView = view.findViewById(R.id.closetDress)
        val closetOuter: ImageView = view.findViewById(R.id.closetOuter)
        val closetTop_only: ImageView = view.findViewById(R.id.closetTop_only)
        val closetBottom_only: ImageView = view.findViewById(R.id.closetBottom_only)
        val closetDress_only: ImageView = view.findViewById(R.id.closetDress_only)

        val delete: ImageView = view.findViewById(R.id.delete)
        val closetName: TextView = view.findViewById(R.id.closetName)
        val closetfrom_id:TextView=view.findViewById(R.id.closetfrom_id)
        val outfitBy: LinearLayout =view.findViewById(R.id.coordiBY)
    }

}
