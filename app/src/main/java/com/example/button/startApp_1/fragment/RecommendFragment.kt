package com.example.button.startApp_1.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.button.R
import com.example.button.startApp_1.data.RecommendBody
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_mycloset.*
import kotlinx.android.synthetic.main.fragment_mycloset.select_weather
import kotlinx.android.synthetic.main.fragment_recommend.*
import retrofit2.Call
import retrofit2.Response


class RecommendFragment : Fragment() {

    companion object {
        private const val MY_INT = "userId"
        fun newInstance(userId: Int): RecommendFragment {
            val frag = RecommendFragment()
            val bundle = Bundle()
            bundle.putInt(MY_INT, userId)
            // this.userId=userId
            frag.arguments = bundle
            return frag
        }
//        fun newInstance(userId: Int?) = MyclosetFragment().apply {
//            arguments =
//                Bundle().apply { putInt(MY_INT, userId ?: Int.MIN_VALUE) }
//        }
    }



    private var place1 = 0
    private var place2 = 0
    private var people1 = 0
    private var people2 = 0
    private var event1 = 0
    private var event2 = 0
    private var mood = 0

    var userId: Int? = 5
    private val weatherList = arrayOf("여름","겨울","환절기(봄, 가을)")
    private val weatherValueList = arrayOf("SUMMER","WINTER","HWAN")
    private var selectWeatherIndex = 0
    private var weatherAdapter : ArrayAdapter<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getInt(MY_INT)
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutInit()
    }


    private fun selectPlace(place : Int){
        cafe.isChecked = false
        park.isChecked = false
        school.isChecked = false
        work.isChecked = false

        if(place1 == 0){
            place1 = place
        }else if(place2 == 0){
            place2 = place
        }

        when(place2){
            1 -> {
                school.isChecked = true
            }
            2 -> {
                work.isChecked = true
            }
            3 -> {
                cafe.isChecked = true
            }
            4 -> {
                park.isChecked = true
            }
        }
        when(place1){
            1 -> {
                school.isChecked = true
            }
            2 -> {
                work.isChecked = true
            }
            3 -> {
                cafe.isChecked = true
            }
            4 -> {
                park.isChecked = true
            }
        }
    }

    private fun layoutInit(){
        weatherAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,weatherList)
        select_weather.adapter = weatherAdapter
        select_weather.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectWeatherIndex = p2
            }

        }

//        ivSelect.setOnClickListener {
//
//            var body = RecommendBody()
//
//            RetrofitClient.retrofitService.recommend("Token " + RetrofitClient.token)
//                .enqueue(object : retrofit2.Callback<Void> {
//                    override fun onFailure(call: Call<Void>, t: Throwable) {
//                    }
//
//                    override fun onResponse(
//                        call: Call<Void>,
//                        response: Response<Void>
//                    ) {
//                        val data = response.message()
//                        if(response.isSuccessful){
//                        }
//
//                    }
//                })
//        }
    }
}
