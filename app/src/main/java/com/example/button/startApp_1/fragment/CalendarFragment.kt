package com.example.button.startApp_1.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.button.R
import com.example.button.startApp_1.adapter.CalendarCoordiListItemAdapter
import com.example.button.startApp_1.adapter.CalendarDayItem
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.data.CoordiListForCalendar
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar_item.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

class CalendarFragment : Fragment() {

    companion object {

        private const val MY_INT = "userId"

        fun newInstance(userId: Int): CalendarFragment {
            var bundle = Bundle()
            bundle.putInt(MY_INT, userId)
            var fragment = CalendarFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    private var userId = 0
    private var worstAdapter = CalendarCoordiListItemAdapter(this)
    private var bestAdatper = CalendarCoordiListItemAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt(MY_INT) ?: 0

        calendar.adapter = ItemAdapter(2020, 12)


        rv_more.adapter = bestAdatper
        rv_less.adapter = worstAdapter

        getBestCoordiList()
        getWorstCoordiList()
    }

    inner class ItemAdapter(var year: Int, var month: Int) : RecyclerView.Adapter<ItemVieHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVieHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_calendar_item, parent, false)
            return ItemVieHolder(view)
        }

        override fun getItemCount(): Int {
            return 12
        }

        override fun onBindViewHolder(holder: ItemVieHolder, position: Int) {
            val realMonth = month - position
            getCoordiForCalendar(year, realMonth)
            holder.bind(position, year, realMonth)
        }

        private fun getCoordiForCalendar(year: Int, month: Int) {
            RetrofitClient.retrofitService.getCoordiForMonth(userId, year, month,"Token " + RetrofitClient.token)
                .enqueue(object : retrofit2.Callback<CoordiListForCalendar> {
                    override fun onFailure(call: Call<CoordiListForCalendar>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<CoordiListForCalendar>,
                        response: Response<CoordiListForCalendar>
                    ) {


                    }
                })
        }
    }

    private fun getBestCoordiList() {
        RetrofitClient.retrofitService.getBestCoordi(userId, "Token " + RetrofitClient.token)
            .enqueue(object : retrofit2.Callback<MutableList<CoordiList>> {
                override fun onFailure(call: Call<MutableList<CoordiList>>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<MutableList<CoordiList>>,
                    response: Response<MutableList<CoordiList>>
                ) {
                    val data = response.body()
                    bestAdatper.myCoordiList = data ?: mutableListOf()
                }

            })
    }

    private fun getWorstCoordiList() {
        RetrofitClient.retrofitService.getWorstCoordi(userId, "Token " + RetrofitClient.token)
            .enqueue(object : retrofit2.Callback<MutableList<CoordiList>> {
                override fun onFailure(call: Call<MutableList<CoordiList>>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<MutableList<CoordiList>>,
                    response: Response<MutableList<CoordiList>>
                ) {
                    val data = response.body()
                    worstAdapter.myCoordiList = data ?: mutableListOf()
                }

            })
    }

    inner class ItemVieHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var adapter : CalendarDayItem? = null

        private var tv_day = view.findViewById<TextView>(R.id.tv_day)
        private var rv_day = view.findViewById<RecyclerView>(R.id.rv_day)

        fun bind(position: Int, year: Int, month: Int) {
            var calendar = Calendar.getInstance()
            calendar.set(year, month - 1, 1)

            var endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            var daylist = (1..endDay).toMutableList()

            adapter = CalendarDayItem(this@CalendarFragment,year,month,userId)
            adapter?.items = daylist

            tv_day.text = "${month}월 ${year}년"
            rv_day.adapter = adapter

        }
    }
}

