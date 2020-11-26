package com.example.button.startApp_1.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.activity.CoordiListActivity
import com.example.button.startApp_1.adapter.CoordiItemAdapter
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.data.GetOutfitIdResponse
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_coordi.*
import kotlinx.android.synthetic.main.fragment_coordi.coordiBottom
import kotlinx.android.synthetic.main.fragment_coordi.coordiDress
import kotlinx.android.synthetic.main.fragment_coordi.coordiName
import kotlinx.android.synthetic.main.fragment_coordi.coordiOuter
import kotlinx.android.synthetic.main.fragment_coordi.coordiTop
import kotlinx.android.synthetic.main.fragment_coordi.cvBottom
import kotlinx.android.synthetic.main.fragment_coordi.cvDress
import kotlinx.android.synthetic.main.fragment_coordi.cvOuter
import kotlinx.android.synthetic.main.fragment_coordi.cvTop
import kotlinx.android.synthetic.main.fragment_coordi.recyclerView_category_bottom
import kotlinx.android.synthetic.main.fragment_coordi.recyclerView_category_onepiece
import kotlinx.android.synthetic.main.fragment_coordi.recyclerView_category_outer
import kotlinx.android.synthetic.main.fragment_coordi.recyclerView_category_top
import kotlinx.android.synthetic.main.fragment_coordi.save
import kotlinx.android.synthetic.main.fragment_coordi.select_weather
import kotlinx.android.synthetic.main.fragment_mycloset.*
import retrofit2.Call
import retrofit2.Response


class CoordiFragment : Fragment() {


    private var userId = 0

    private var topAdapter = CoordiItemAdapter(this,CoordiItemAdapter.TYPE_TOP)
    private var bottomAdapter = CoordiItemAdapter(this,CoordiItemAdapter.TYPE_BOTTOM)
    private var outerAdapter = CoordiItemAdapter(this,CoordiItemAdapter.TYPE_OUTER)
    private var dressAdapter = CoordiItemAdapter(this,CoordiItemAdapter.TYPE_DRESS)



    private var selectTopId = 0
    private var selectBottomId = 0
    private var selectOuterId = 0
    private var selectDressId = 0

    private var uploadClosetCount = 0
    private var totalUploadClosetCount = 0


    private val weatherList = arrayOf("전체","여름","겨울","환절기(봄, 가을)")
    private val weatherValueList = arrayOf("ALL","SUMMER","WINTER","HWAN")
    private var selectWeatherIndex = 0
    private var weatherAdapter : ArrayAdapter<String>? = null

    companion object{

        private const val MY_INT = "userId"

        fun newInstance(userId: Int) : CoordiFragment{
            var bundle = Bundle()
            bundle.putInt(MY_INT, userId)
            var fragment = CoordiFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_coordi, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutInit()

        userId = arguments?.getInt(MY_INT) ?: 0
    }

    override fun onResume() {
        super.onResume()

        reqCloth(userId)
    }

    fun layoutInit(){

        if(!TextUtils.equals(RetrofitClient.gender,"FEMALE")){
            tv_dress.visibility = View.GONE
            recyclerView_category_onepiece.visibility = View.GONE
        }
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
                userId?.let { reqCloth(it) }
            }

        }


        recyclerView_category_top.apply {
            adapter = topAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        recyclerView_category_bottom.apply {
            adapter = bottomAdapter
            layoutManager = LinearLayoutManager(context,  RecyclerView.HORIZONTAL, false)
        }
        recyclerView_category_outer.apply {
            adapter = outerAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        recyclerView_category_onepiece.apply {
            adapter = dressAdapter
            layoutManager = LinearLayoutManager(context,  RecyclerView.HORIZONTAL, false)
        }

        cvOuter.setOnClickListener {
            selectOuterId = 0
            cvOuter.visibility = View.GONE
        }


        tv_list.setOnClickListener {
            var intent = Intent(context,CoordiListActivity::class.java)
            intent.putExtra(CoordiListActivity.KEY_USER_ID,userId)
            startActivity(intent)
        }

        save.setOnClickListener {
            saveCloth()
        }
    }

    private fun uploadCloth(closetID : Int,outFitID : Int){

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
                            resetCoordi()
                            Toast.makeText(context,"코디가 정상적으로 등록됐습니다.",Toast.LENGTH_SHORT).show()
                            var intent = Intent(context,CoordiListActivity::class.java)
                            intent.putExtra(CoordiListActivity.KEY_USER_ID,userId)
                            startActivity(intent)
                        }
                    }

                }

            })
    }

    private fun resetCoordi(){
        selectTopId = 0
        selectBottomId = 0
        selectDressId = 0
        selectOuterId = 0

        coordiBottom.setImageResource(0)
        coordiTop.setImageResource(0)
        coordiDress.setImageResource(0)
        coordiOuter.setImageResource(0)

        cvBottom.visibility = View.INVISIBLE
        cvTop.visibility = View.INVISIBLE
        cvDress.visibility = View.INVISIBLE
        coordiOuter.visibility = View.INVISIBLE

        coordiName.setText("")
    }

    private fun saveCloth(){
        var outfitName = coordiName.text.toString()
        if(TextUtils.isEmpty(outfitName)){
            Toast.makeText(context,"코디 이름을 입력해주세요",Toast.LENGTH_SHORT).show()
            return
        }
        else if(outfitName.length>6){
            Toast.makeText(context,"코디 이름을 6자 이내로 입력해주세요",Toast.LENGTH_SHORT).show()
            return
        }
//        var userIdBody = RequestBody.create(MediaType.parse("text/plain"), (userId).toString())
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

    private fun reqCloth(id : Int){

        if(selectWeatherIndex == 0){
            RetrofitClient.retrofitService.getCloth(id,"Token " + RetrofitClient.token)
                .enqueue(object : retrofit2.Callback<MutableList<Cloth>> {
                    override fun onFailure(call: Call<MutableList<Cloth>>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<MutableList<Cloth>>,
                        response: Response<MutableList<Cloth>>
                    ) {
                        val data = response.body()
                        var topItem = mutableListOf<Cloth>()
                        var bottomItem = mutableListOf<Cloth>()
                        var outerItem = mutableListOf<Cloth>()
                        var dressItem = mutableListOf<Cloth>()
                        topItem.addAll(data?.filter {  TextUtils.equals(it.category,"TOP") } ?: mutableListOf())
                        topAdapter.clothList = topItem

                        bottomItem.addAll(data?.filter {  TextUtils.equals(it.category,"BOTTOM") } ?: mutableListOf())
                        bottomAdapter.clothList = bottomItem

                        outerItem.addAll(data?.filter {  TextUtils.equals(it.category,"OUTER") } ?: mutableListOf())
                        outerAdapter.clothList = outerItem

                        dressItem.addAll(data?.filter {  TextUtils.equals(it.category,"DRESS") } ?: mutableListOf())
                        dressAdapter.clothList = dressItem
                    }

                })
        }else{
            RetrofitClient.retrofitService.getCloth(id, weatherValueList[selectWeatherIndex],"Token " + RetrofitClient.token)
                .enqueue(object : retrofit2.Callback<MutableList<Cloth>> {
                    override fun onFailure(call: Call<MutableList<Cloth>>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<MutableList<Cloth>>,
                        response: Response<MutableList<Cloth>>
                    ) {
                        val data = response.body()
                        var topItem = mutableListOf<Cloth>()
                        var bottomItem = mutableListOf<Cloth>()
                        var outerItem = mutableListOf<Cloth>()
                        var dressItem = mutableListOf<Cloth>()
                        topItem.addAll(data?.filter {  TextUtils.equals(it.category,"TOP") } ?: mutableListOf())
                        topAdapter.clothList = topItem

                        bottomItem.addAll(data?.filter {  TextUtils.equals(it.category,"BOTTOM") } ?: mutableListOf())
                        bottomAdapter.clothList = bottomItem

                        outerItem.addAll(data?.filter {  TextUtils.equals(it.category,"OUTER") } ?: mutableListOf())
                        outerAdapter.clothList = outerItem

                        dressItem.addAll(data?.filter {  TextUtils.equals(it.category,"DRESS") } ?: mutableListOf())
                        dressAdapter.clothList = dressItem
                    }

                })
        }

    }

    fun clickDress(item : Cloth){
        cvBottom.visibility = View.INVISIBLE
        cvTop.visibility = View.INVISIBLE
        cvDress.visibility = View.VISIBLE
        selectTopId = 0
        selectBottomId = 0
        selectDressId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .into(coordiDress)

    }

    fun clickOuter(item: Cloth){

        cvOuter.visibility = View.VISIBLE
        selectOuterId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .into(coordiOuter)
    }

    fun clickTop(item : Cloth){
        cvDress.visibility = View.INVISIBLE
        cvBottom.visibility = View.VISIBLE
        cvTop.visibility = View.VISIBLE
        selectDressId = 0
        selectTopId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .into(coordiTop)
    }

    fun clickBottom(item : Cloth){
        cvDress.visibility = View.INVISIBLE
        cvBottom.visibility = View.VISIBLE
        cvTop.visibility = View.VISIBLE
        selectDressId = 0
        Log.e("fragment","clickBottom id=${item.clothID}")
        selectBottomId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .into(coordiBottom)
    }


}
