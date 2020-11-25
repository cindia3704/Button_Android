package com.example.button.startApp_1.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.activity.CalendarCoordiRegisterActivity
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.data.ResInsertCoordiForCalendar
import com.example.button.startApp_1.fragment.CalendarFragment
import com.example.button.startApp_1.network.RetrofitClient
import java.util.*


class CalendarDayItem(
    var fragment: CalendarFragment,
    var year: Int,
    var month: Int,
    var userId: Int
) : RecyclerView.Adapter<CalendarDayItem.ItemViewHolder>() {


    private var startWeek = 0

    init {

        var cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DATE, 1)

        startWeek = cal.get(Calendar.DAY_OF_WEEK) -1

    }

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
        var ivCoordi = view.findViewById<ImageView>(R.id.ivCoordi)
        var cardView = view.findViewById<CardView>(R.id.cardview)
        var cardView_bottom=view.findViewById<CardView>(R.id.cardview_bottom)
        var ivCoordi_bottom = view.findViewById<ImageView>(R.id.ivCoordi_bottom)
        var cardView_dress=view.findViewById<CardView>(R.id.cardview_dress)
        var ivCoordi_dress = view.findViewById<ImageView>(R.id.ivCoordi_dress)

        @SuppressLint("ResourceAsColor")
        fun bind(position: Int) {
            if(position < startWeek){
                tv_day.visibility = View.INVISIBLE
                tv_day.visibility = View.INVISIBLE
                cardView.visibility = View.INVISIBLE
                cardView_bottom.visibility=View.INVISIBLE
            }else{
                var day = (position - startWeek) + 1
                var valueMonth = if(month < 10) "0$month" else "$month"
                var valueDay = if (day < 10) "0$day" else "$day"
                tv_day.text = "${day}"
                Log.e("calendarDaytoday","day=${Calendar.getInstance().get(Calendar.DATE)}")

//                if(""+year.toString()+"-"+month.toString()+"-"+valueDay.toString()==(Calendar.getInstance().get(Calendar.DATE) +1).toString()) {
//                    tv_day.setBackgroundResource(R.drawable.borderr)
//                }
                if(valueDay.toInt()==Calendar.getInstance().get(Calendar.DATE) && month==Calendar.getInstance().get(Calendar.MONTH)+1){
                    tv_day.setBackgroundResource(R.drawable.borderr)
                }
                cardView.visibility = View.GONE
                cardView_bottom.visibility=View.GONE
                cardView_dress.visibility=View.GONE
                var outfitImage = outfitList["$year-$valueMonth-$valueDay"]
                Log.e("calendarDayItem","year=${year},month=${month},value=${valueDay}\noutfitImage=${outfitImage?.toString()}")
                outfitImage?.let {
                    var cloth = it.clothes
                    var cloth_top=it.clothes[0]
                    var cloth_bottom=it.clothes[0]
                    var cloth_dress=it.clothes[0]
                    for( item in it.clothes){
                        if(TextUtils.equals("TOP",item.category)){
                            cardView.visibility = View.VISIBLE
                            cloth_top = item
                        }
                        else if(TextUtils.equals("BOTTOM",item.category)){
                            cardView_bottom.visibility=View.VISIBLE
                            cloth_bottom = item
                        }
                        else if(TextUtils.equals("DRESS",item.category)){
                            cardView_dress.visibility=View.VISIBLE
                            cloth_dress = item
                        }
                        else{

                        }
                    }
                    tv_day.setTextColor(ContextCompat.getColor(fragment.context!!,R.color.buttonColor))
                    Glide.with(fragment)
                        .load(RetrofitClient.imageBaseUrl + cloth_top.photo)
                        .placeholder(R.drawable.circle)
                        .apply(RequestOptions.circleCropTransform()).into(ivCoordi)
                    Glide.with(fragment)
                        .load(RetrofitClient.imageBaseUrl + cloth_bottom.photo)
                        .placeholder(R.drawable.circle)
                        .apply(RequestOptions.circleCropTransform()).into(ivCoordi_bottom)
                    Glide.with(fragment)
                        .load(RetrofitClient.imageBaseUrl + cloth_dress.photo)
                        .placeholder(R.drawable.circle)
                        .into(ivCoordi_dress)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_day_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size + startWeek
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }
}
