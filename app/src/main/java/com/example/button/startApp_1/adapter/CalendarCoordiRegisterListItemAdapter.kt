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
import com.example.button.R
import com.example.button.startApp_1.activity.CalendarCoordiRegisterActivity
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.network.RetrofitClient


class CalendarCoordiRegisterListItemAdapter(val activity: CalendarCoordiRegisterActivity) :
    RecyclerView.Adapter<CalendarCoordiRegisterListItemAdapter.ViewHolder>() {


    var selectPosition = -1
    var isEdit = false
    var myCoordiList = mutableListOf<CoordiList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun selectItemId(): Int {
        if (selectPosition >= 0) {
            return myCoordiList[selectPosition].outfitID
        } else {
            return -1
        }
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

        var closetLists = coordItem.clothes

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


        holder.delete.visibility = if (isEdit) View.VISIBLE else View.GONE

        holder.bind(position)
        holder.content.setOnClickListener {
            activity.selectCoordi(closetLists)
            activity.setCoordiName(coordItem.outfitName)
            selectPosition = position
            notifyDataSetChanged()
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ctSelect: ConstraintLayout = view.findViewById(R.id.ct_select)

        val cvTop: CardView = view.findViewById(R.id.cvTop)
        val cvBottom: CardView = view.findViewById(R.id.cvBottom)
        val cvDress: CardView = view.findViewById(R.id.cvDress)
        val cvOuter: CardView = view.findViewById(R.id.cvOuter)

        val cvTop_only: CardView = view.findViewById(R.id.cvTop_only)
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
        fun bind(position: Int) {
//            if(selectPosition == position){
//                ctSelect.visibility = View.VISIBLE
//            }else{
//                ctSelect.visibility = View.GONE
//            }
        }
    }

}
