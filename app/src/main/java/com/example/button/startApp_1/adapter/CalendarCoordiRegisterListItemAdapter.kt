package com.example.button.startApp_1.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

    fun selectItemId() : Int {
        if(selectPosition >= 0){
            return myCoordiList[selectPosition].outfitID
        }else{
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
    fun deleteItem(item : CoordiList){
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

        for(i in 0 until closetLists.size){
            if(TextUtils.equals(closetLists[i].category,"TOP")){
                holder.cvDress.visibility = View.INVISIBLE

                holder.cvTop.visibility = View.VISIBLE
                holder.cvBottom.visibility = View.VISIBLE

                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                    .placeholder(R.drawable.circle)
                    .into(holder.closetTop)
            }

            if(TextUtils.equals(closetLists[i].category,"BOTTOM")){
                holder.cvDress.visibility = View.INVISIBLE

                holder.cvTop.visibility = View.VISIBLE
                holder.cvBottom.visibility = View.VISIBLE

                Glide.with(activity)
                    .load(RetrofitClient.imageBaseUrl + closetLists[i].photo)
                    .placeholder(R.drawable.circle)
                    .into(holder.closetBottom)
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


        holder.delete.visibility = if (isEdit) View.VISIBLE else View.GONE

        holder.bind(position)
        holder.content.setOnClickListener {
            activity.selectCoordi(closetLists)
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

        val content: ConstraintLayout = view.findViewById(R.id.content)
        val closetTop: ImageView = view.findViewById(R.id.closetTop)
        val closetBottom: ImageView = view.findViewById(R.id.closetBottom)
        val closetDress: ImageView = view.findViewById(R.id.closetDress)
        val closetOuter: ImageView = view.findViewById(R.id.closetOuter)

        val delete: ImageView = view.findViewById(R.id.delete)
        val closetName: TextView = view.findViewById(R.id.closetName)

        fun bind(position : Int){
//            if(selectPosition == position){
//                ctSelect.visibility = View.VISIBLE
//            }else{
//                ctSelect.visibility = View.GONE
//            }
        }
    }

}
