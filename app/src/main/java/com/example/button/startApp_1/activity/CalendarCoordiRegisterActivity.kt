package com.example.button.startApp_1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.button.R
import com.example.button.startApp_1.adapter.CalendarCoordiRegisterListItemAdapter
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.data.CoordiListForCalendar
import com.example.button.startApp_1.data.SelectCoordiForCalendarBody
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.*
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.coordiBottom
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.coordiDress
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.coordiName
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.coordiOuter
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.coordiTop
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.cvDress
import retrofit2.Call
import retrofit2.Response

class CalendarCoordiRegisterActivity : AppCompatActivity() {
    companion object {
        const val KEY_USER_ID = "KEY_USER_ID"
    }


    var listAdapter = CalendarCoordiRegisterListItemAdapter(this)
    var userID = 0

    private var year = 0
    private var month = 0
    private var day = 0

    private var preMemo = ""// 기존에 작성한 메모
    private var preOutfitId = 0// 기존에 작성한 코디 번호
    private var preCalendarId = 0

    private var isUdapteCoordi = true
    private var isUpdateMemo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_coordi_register)
        userID = intent.getIntExtra(KEY_USER_ID, 0)
        year = intent.getIntExtra("year", 0)
        month = intent.getIntExtra("month", 0)
        day = intent.getIntExtra("day", 0)

        layoutInit()
        getCoordiList()
        reqData()
    }


    private fun reqData() {
        RetrofitClient.retrofitService.getCoordiForDay(
            userID,
            year,
            month,
            day,
            "Token " + RetrofitClient.token
        )
            .enqueue(object : retrofit2.Callback<CoordiListForCalendar> {
                override fun onFailure(
                    call: Call<CoordiListForCalendar>,
                    t: Throwable
                ) {
                }

                override fun onResponse(
                    call: Call<CoordiListForCalendar>,
                    response: Response<CoordiListForCalendar>
                ) {
                    val data = response.body()

                    data?.let {
                        preMemo = it.diary
                        preCalendarId = it.calendarID

                        et_memo.setText("${preMemo}")


                        var coordiList = it.outfit_worn
                        setCoordiName(coordiList.outfitName)
                        preOutfitId = coordiList.outfitID

                        selectCoordi(coordiList.clothes)


                    }
                }

            })
    }

    fun setCoordiName(coordiname : String){
        coordiName.setText(coordiname)
    }
    fun selectCoordi(cloths: MutableList<Cloth>) {
        cvTop.visibility = View.INVISIBLE
        cvBottom.visibility = View.INVISIBLE
        cvOuter.visibility = View.INVISIBLE
        cvDress.visibility = View.INVISIBLE
        cvTop_only_.visibility=View.INVISIBLE
        cvBottom_only_.visibility=View.INVISIBLE
        cvDress_only_.visibility=View.INVISIBLE
        if(cloths.size<=2){
            if(cloths.size==1){
                if(TextUtils.equals(cloths[0].category,"DRESS")){
                    cvDress_only_.visibility = View.VISIBLE
                    Glide.with(this@CalendarCoordiRegisterActivity)
                        .load(RetrofitClient.imageBaseUrl + cloths[0].photo)
                        .placeholder(R.drawable.circle)
                        .into(coordiDress_only_)
                }
            }
            else{
                for(i in 0 until cloths.size) {
                    if (TextUtils.equals(cloths[i].category, "TOP")) {
//                        holder.cvDres_only.visibility = View.INVISIBLE

                        cvTop_only_.visibility = View.VISIBLE
//                        holder.cvBottom.visibility = View.VISIBLE

                        Glide.with(this@CalendarCoordiRegisterActivity)
                            .load(RetrofitClient.imageBaseUrl + cloths[i].photo)
                            .placeholder(R.drawable.circle)
                            .into(coordiTop_only_)
                    }

                    if (TextUtils.equals(cloths[i].category, "BOTTOM")) {
//                        holder.cvDress.visibility = View.INVISIBLE
//
//                        holder.cvTop.visibility = View.VISIBLE
                        cvBottom_only_.visibility = View.VISIBLE

                        Glide.with(this@CalendarCoordiRegisterActivity)
                            .load(RetrofitClient.imageBaseUrl + cloths[i].photo)
                            .placeholder(R.drawable.circle)
                            .into(coordiBottom_only_)
                    }
                    if(TextUtils.equals(cloths[i].category,"OUTER")){

                        cvOuter.visibility = View.VISIBLE

                        Glide.with(this@CalendarCoordiRegisterActivity)
                            .load(RetrofitClient.imageBaseUrl + cloths[i].photo)
                            .placeholder(R.drawable.circle).into(coordiOuter)
                    }

                    if(TextUtils.equals(cloths[i].category,"DRESS")){
                        cvDress.visibility = View.VISIBLE

//                        cvTop.visibility = View.INVISIBLE
//                        cvBottom.visibility = View.INVISIBLE

                        Glide.with(this@CalendarCoordiRegisterActivity)
                            .load(RetrofitClient.imageBaseUrl + cloths[i].photo)
                            .placeholder(R.drawable.circle)
                            .into(coordiDress)
                    }
                }
            }
        }
        else {
            for (i in 0 until cloths.size) {
                var item = cloths[i]
                if (TextUtils.equals(item.category, "TOP")) {
//                                selectedTopId = item.clothID

                    cvTop.visibility = View.VISIBLE

                    Glide.with(this@CalendarCoordiRegisterActivity)
                        .load(RetrofitClient.imageBaseUrl + item.photo)
                        .placeholder(R.drawable.circle)
                        .into(coordiTop)
                }
                if (TextUtils.equals(item.category, "BOTTOM")) {
//                                selectedBottomId = item.clothID
                    cvBottom.visibility = View.VISIBLE

                    Glide.with(this@CalendarCoordiRegisterActivity)
                        .load(RetrofitClient.imageBaseUrl + item.photo)
                        .placeholder(R.drawable.circle)
                        .into(coordiBottom)
                }
                if (TextUtils.equals(item.category, "OUTER")) {
//                                selectedOuterId = item.clothID
                    cvOuter.visibility = View.VISIBLE
                    Glide.with(this@CalendarCoordiRegisterActivity)
                        .load(RetrofitClient.imageBaseUrl + item.photo)
                        .placeholder(R.drawable.circle)
                        .into(coordiOuter)
                }
                if (TextUtils.equals(item.category, "DRESS")) {
//                                selectedDressId = item.clothID

                    cvDress.visibility = View.VISIBLE
                    Glide.with(this@CalendarCoordiRegisterActivity)
                        .load(RetrofitClient.imageBaseUrl + item.photo)
                        .placeholder(R.drawable.circle)
                        .into(coordiDress)
                }
            }
        }
    }


    private fun layoutInit() {

        recyclerviewList.apply {
            adapter = listAdapter
            layoutManager =
                GridLayoutManager(
                    this@CalendarCoordiRegisterActivity,
                    3,
                    RecyclerView.VERTICAL,
                    false
                )

        }


        register.setOnClickListener {

            var selectOutfitId = listAdapter.selectItemId()


            var body = SelectCoordiForCalendarBody(userID, et_memo.text.toString())
            if (TextUtils.isEmpty(preMemo)) {
                if (selectOutfitId == -1) return@setOnClickListener

                RetrofitClient.retrofitService.selectCoordiForCalendar(
                    userID,
                    selectOutfitId,
                    year,
                    month,
                    day,
                    body,
                    "Token " + RetrofitClient.token
                )
                    .enqueue(object : retrofit2.Callback<SelectCoordiForCalendarBody> {
                        override fun onFailure(
                            call: Call<SelectCoordiForCalendarBody>,
                            t: Throwable
                        ) {
                        }

                        override fun onResponse(
                            call: Call<SelectCoordiForCalendarBody>,
                            response: Response<SelectCoordiForCalendarBody>
                        ) {
                            if (response.isSuccessful) {
                                finish()
                            }

                        }
                    })
            } else {
                if (preOutfitId != 0 && selectOutfitId != preOutfitId) {

                    isUdapteCoordi = false
                    //기존에 고른 코디가 아닌 다른 코디를 골랐을때
                    RetrofitClient.retrofitService.updateCoordiForCalendar(
                        userID,
                        preCalendarId,
                        selectOutfitId,
                        "Token " + RetrofitClient.token
                    )
                        .enqueue(object : retrofit2.Callback<Void> {
                            override fun onFailure(call: Call<Void>, t: Throwable) {
                            }

                            override fun onResponse(
                                call: Call<Void>,
                                response: Response<Void>
                            ) {
                                if (response.isSuccessful) {
                                    isUdapteCoordi = true

                                    if (isUdapteCoordi && isUpdateMemo) {
                                        finish()
                                    }

                                }

                            }
                        })
                }
                RetrofitClient.retrofitService.updateCalendarMemo(
                    userID,
                    year,
                    month,
                    day,
                    body,
                    "Token " + RetrofitClient.token
                )
                    .enqueue(object : retrofit2.Callback<Void> {
                        override fun onFailure(call: Call<Void>, t: Throwable) {
                        }

                        override fun onResponse(
                            call: Call<Void>,
                            response: Response<Void>
                        ) {
                            if (response.isSuccessful) {
                                isUpdateMemo = true

                                if (isUdapteCoordi && isUpdateMemo) {
                                    finish()
                                }
                            }

                        }
                    })


            }


        }
        back.setOnClickListener {
            finish()
        }
    }

    private fun getCoordiList() {
        RetrofitClient.retrofitService.getCoordiList(userID, "Token " + RetrofitClient.token)
            .enqueue(object : retrofit2.Callback<MutableList<CoordiList>> {
                override fun onFailure(call: Call<MutableList<CoordiList>>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<MutableList<CoordiList>>,
                    response: Response<MutableList<CoordiList>>
                ) {
                    val data = response.body()
                    listAdapter.myCoordiList = data ?: mutableListOf()
                }

            })
    }

    override fun onResume() {
        super.onResume()
        getCoordiList();
    }

}
