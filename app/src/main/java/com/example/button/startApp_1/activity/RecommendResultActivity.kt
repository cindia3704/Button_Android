package com.example.button.startApp_1.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.button.R
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.GetOutfitIdResponse
import com.example.button.startApp_1.data.RecommendBody
import com.example.button.startApp_1.data.SelectCoordiForCalendarBody
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_calendar_coordi_register.*
import kotlinx.android.synthetic.main.activity_recommend_result.*
import kotlinx.android.synthetic.main.activity_recommend_result.back
import kotlinx.android.synthetic.main.activity_recommend_result.coordiBottom
import kotlinx.android.synthetic.main.activity_recommend_result.coordiDress
import kotlinx.android.synthetic.main.activity_recommend_result.coordiName
import kotlinx.android.synthetic.main.activity_recommend_result.coordiOuter
import kotlinx.android.synthetic.main.activity_recommend_result.coordiTop
import kotlinx.android.synthetic.main.activity_recommend_result.cvBottom
import kotlinx.android.synthetic.main.activity_recommend_result.cvDress
import kotlinx.android.synthetic.main.activity_recommend_result.cvOuter
import kotlinx.android.synthetic.main.activity_recommend_result.cvTop
import kotlinx.android.synthetic.main.activity_recommend_result.save
import kotlinx.android.synthetic.main.activity_recommend_result.tv_list
import kotlinx.android.synthetic.main.fragment_coordi.*
import retrofit2.Call
import retrofit2.Response
import java.util.*

class RecommendResultActivity : AppCompatActivity() {

    var selectData : RecommendBody? = null

    private var uploadClosetCount = 0
    private var totalUploadClosetCount = 0

    private var selectTopId = 0
    private var selectBottomId = 0
    private var selectOuterId = 0
    private var selectDressId = 0


    private var isSelectCalendar = false
    private var selectYear = 0
    private var selectMonth = 0
    private var selectDay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend_result)


        setClick()
        selectData = intent.getParcelableExtra("recommend")
        selectCoordi()
    }

    fun setClick(){
        back.setOnClickListener {
            finish()
        }

        tv_list.setOnClickListener {
            var intent = Intent(this,CoordiListActivity::class.java)
            intent.putExtra(CoordiListActivity.KEY_USER_ID,selectData?.id?:0)
            startActivity(intent)
        }

        reSelect.setOnClickListener {
            selectCoordi()
        }

        save.setOnClickListener {
            saveCloth()
        }

        calendar.setOnClickListener {

            var outfitName = coordiName.text.toString()
            if(TextUtils.isEmpty(outfitName)){
                Toast.makeText(this,"코디 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var calendar = Calendar.getInstance()
            var dialog = DatePickerDialog(this,object : DatePickerDialog.OnDateSetListener{
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    selectYear = p1
                    selectMonth = p2+1
                    selectDay = p3
                    isSelectCalendar = true
                    saveCloth()
//                    closer_buy_day.text = "${p1}-${p2+1}-${p3}"
                }

            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_WEEK))
            dialog.show()
        }
    }

    private fun saveCalendar(selectOutfitId : Int){
        isSelectCalendar = false


        var userId = selectData?.id?:0


        var body = SelectCoordiForCalendarBody(userId, "")
            if (selectOutfitId == -1) return

            RetrofitClient.retrofitService.selectCoordiForCalendar(
                userId,
                selectOutfitId,
                selectYear,
                selectMonth,
                selectDay,
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
    }
    private fun saveCloth(){
        var outfitName = coordiName.text.toString()
        if(TextUtils.isEmpty(outfitName)){
            Toast.makeText(this,"코디 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        var userId = selectData?.id?:0
        RetrofitClient.retrofitService.getOutfitId(userId, userId,userId,"Token " + RetrofitClient.token,outfitName)
            .enqueue(object : retrofit2.Callback<GetOutfitIdResponse> {
                override fun onFailure(call: Call<GetOutfitIdResponse>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<GetOutfitIdResponse>,
                    response: Response<GetOutfitIdResponse>
                ) {
                    val data = response.body()
                    data?.let{
                        uploadClosetCount = 0
                        totalUploadClosetCount = 0

                        if(selectTopId != 0){
                            totalUploadClosetCount++
                            uploadCloth(selectTopId,it.outfitID)
                        }
                        if(selectBottomId != 0){
                            totalUploadClosetCount++
                            uploadCloth(selectBottomId,it.outfitID)
                        }
                        if(selectDressId != 0){
                            totalUploadClosetCount++
                            uploadCloth(selectDressId,it.outfitID)
                        }
                        if(selectOuterId != 0){
                            totalUploadClosetCount++
                            uploadCloth(selectOuterId,it.outfitID)
                        }


                    }
                }

            })
    }

    private fun uploadCloth(closetID : Int,outFitID : Int){


        var userId = selectData?.id?:0

        RetrofitClient.retrofitService.addClothCoordi(
            "Token "+ RetrofitClient.token,userId, closetID,outFitID)
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    val data = response.body()
                    if(response.isSuccessful){
                        uploadClosetCount++
                        if(uploadClosetCount ==totalUploadClosetCount){

                            if(isSelectCalendar){
                                saveCalendar(outFitID)
                            }else{
                                Toast.makeText(this@RecommendResultActivity,"코디가 정상적으로 등록됐습니다.",Toast.LENGTH_SHORT).show()
                                var intent = Intent(this@RecommendResultActivity,CoordiListActivity::class.java)
                                intent.putExtra(CoordiListActivity.KEY_USER_ID,userId)
                                startActivity(intent)
                                finish()
                            }

                        }
                    }

                }

            })
    }

    fun selectCoordi(){

        selectData?.let{


            selectTopId = 0
            selectBottomId = 0
            selectOuterId = 0
            selectDressId = 0

            RetrofitClient.retrofitService.recommend(it,"Token " + RetrofitClient.token)
                .enqueue(object : retrofit2.Callback<MutableList<Cloth>> {
                    override fun onFailure(call: Call<MutableList<Cloth>>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<MutableList<Cloth>>,
                        response: Response<MutableList<Cloth>>
                    ) {

                        Log.e("recommd","resoponse="+response.toString())
                        if(response.isSuccessful){

                            cvTop.visibility = View.INVISIBLE
                            cvBottom.visibility = View.INVISIBLE
                            cvOuter.visibility = View.INVISIBLE
                            cvDress.visibility = View.INVISIBLE


                            response.body()?.let{
                                Log.e("recommd","it="+it.toString())
                                for(i in 0 until it.size){

                                    var item = it[i]
                                    Log.e("recommd","category="+item.category)
                                    if(TextUtils.equals(item.category, "TOP")) {
                                        selectTopId = item.clothID
                                        cvTop.visibility = View.VISIBLE
                                        Glide.with(this@RecommendResultActivity)
                                            .load(RetrofitClient.imageBaseUrl + item.photo)
                                            .placeholder(R.drawable.circle)
                                            .into(coordiTop)
                                    }
                                    if(TextUtils.equals(item.category, "BOTTOM")) {
                                        selectBottomId = item.clothID
                                        cvBottom.visibility = View.VISIBLE
                                        Glide.with(this@RecommendResultActivity)
                                            .load(RetrofitClient.imageBaseUrl + item.photo)
                                            .placeholder(R.drawable.circle)
                                            .into(coordiBottom)
                                    }
                                    if(TextUtils.equals(item.category, "OUTER")) {
                                        selectOuterId = item.clothID

                                        cvOuter.visibility = View.VISIBLE
                                        Glide.with(this@RecommendResultActivity)
                                            .load(RetrofitClient.imageBaseUrl + item.photo)
                                            .placeholder(R.drawable.circle)
                                            .into(coordiOuter)
                                    }
                                    if(TextUtils.equals(item.category, "DRESS")) {
                                        selectDressId = item.clothID
                                        cvDress.visibility = View.VISIBLE
                                        Glide.with(this@RecommendResultActivity)
                                            .load(RetrofitClient.imageBaseUrl + item.photo)
                                            .placeholder(R.drawable.circle)
                                            .into(coordiDress)
                                    }
                                }
                            }


                        }

                    }
                })
        }

    }
}
