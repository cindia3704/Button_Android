package com.example.button.startApp_1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.example.button.startApp_1.adapter.CalendarCoordiRegisterListItemAdapter
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_coordi_list.*
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_coordi_register)
        userID = intent.getIntExtra(KEY_USER_ID, 0)
        year = intent.getIntExtra("year",0)
        month = intent.getIntExtra("month",0)

        layoutInit()
        getCoordiList()
    }


    private fun layoutInit() {

        recyclerviewList.apply {
            adapter = listAdapter
            layoutManager =
                GridLayoutManager(this@CalendarCoordiRegisterActivity, 3, RecyclerView.VERTICAL, false)

        }


//        edit.setOnClickListener {
//            listAdapter.updateEdit()
//        }
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
