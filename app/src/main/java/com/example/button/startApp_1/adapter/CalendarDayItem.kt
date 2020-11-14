package com.example.button.startApp_1.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.button.R
import com.example.button.startApp_1.activity.CalendarCoordiRegisterActivity
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.data.ResInsertCoordiForCalendar
import com.example.button.startApp_1.fragment.CalendarFragment
import com.example.button.startApp_1.network.RetrofitClient


class CalendarDayItem(
    var fragment: CalendarFragment,
    var year: Int,
    var month: Int,
    var userId: Int
) : RecyclerView.Adapter<CalendarDayItem.ItemViewHolder>() {


    var outfitList = hashMapOf<String, CoordiList>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var items = mutableListOf<Int>()
        set(value) {
            field = value
            Log.e("value", "value.size=" + value.size)
            notifyDataSetChanged()
        }


    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_day = view.findViewById<TextView>(R.id.tv_day)
        var ivCoordi  = view.findViewById<ImageView>(R.id.ivCoordi)
        var cardView = view.findViewById<CardView>(R.id.cardview)

        fun bind(position: Int) {
            var day = position+1
            var valueDay = if(day<10) "0$day" else "$day"
            tv_day.text = "${day}"

            cardView.visibility = View.GONE
            Log.e("test","itemviewholder year=$year-$month-$valueDay")
            var outfitImage = outfitList["$year-$month-$valueDay"]
            outfitImage?.let{
                cardView.visibility = View.VISIBLE
                Glide.with(fragment)
                    .load(RetrofitClient.imageBaseUrl + it.clothes[0].photo)
                    .placeholder(R.drawable.circle)
                    .into(ivCoordi)
            }

            tv_day.setOnClickListener {
                var intent = Intent(fragment.context, CalendarCoordiRegisterActivity::class.java)
                intent.putExtra("year", year)
                intent.putExtra("month", month)
                intent.putExtra("day", day)
                intent.putExtra("KEY_USER_ID", userId)
                fragment.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_day_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }
}
