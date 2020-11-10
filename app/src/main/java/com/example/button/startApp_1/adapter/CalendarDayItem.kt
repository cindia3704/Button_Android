package com.example.button.startApp_1.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.example.button.startApp_1.activity.CalendarCoordiRegisterActivity
import com.example.button.startApp_1.fragment.CalendarFragment


class CalendarDayItem(var fragment : CalendarFragment,var year : Int,var month : Int,var userId : Int) : RecyclerView.Adapter<CalendarDayItem.ItemViewHolder>() {



    var items = mutableListOf<Int>()
        set(value) {
            field = value
            Log.e("value", "value.size=" + value.size)
            notifyDataSetChanged()
        }


    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tv_day = view.findViewById<TextView>(R.id.tv_day)

        fun bind(position: Int) {
            tv_day.text = "${position + 1}"

            tv_day.setOnClickListener {
                var intent = Intent(fragment.context,CalendarCoordiRegisterActivity::class.java)
                intent.putExtra("year",year)
                intent.putExtra("month",month)
                intent.putExtra("KEY_USER_ID",userId)
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
