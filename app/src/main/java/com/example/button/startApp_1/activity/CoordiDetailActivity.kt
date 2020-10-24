package com.example.button.startApp_1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.adapter.CoordiDetailItemAdapter
import com.example.button.startApp_1.adapter.CoordiItemAdapter
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.data.GetOutfitIdResponse
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_coordi_detail.*
import kotlinx.android.synthetic.main.activity_coordi_detail.coordiBottom
import kotlinx.android.synthetic.main.activity_coordi_detail.coordiDress
import kotlinx.android.synthetic.main.activity_coordi_detail.coordiName
import kotlinx.android.synthetic.main.activity_coordi_detail.coordiOuter
import kotlinx.android.synthetic.main.activity_coordi_detail.coordiTop
import kotlinx.android.synthetic.main.activity_coordi_detail.recyclerView_category_bottom
import kotlinx.android.synthetic.main.activity_coordi_detail.recyclerView_category_onepiece
import kotlinx.android.synthetic.main.activity_coordi_detail.recyclerView_category_outer
import kotlinx.android.synthetic.main.activity_coordi_detail.recyclerView_category_top
import kotlinx.android.synthetic.main.activity_coordi_detail.save
import kotlinx.android.synthetic.main.fragment_coordi.*
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class CoordiDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_COORDI_ID = "KEY_COORDI_ID"
        const val KEY_USER_ID = "KEY_USER_ID"
        const val KEY_FRIEND_ID = "KEY_FRIEND_ID"
    }

    private var topAdapter = CoordiDetailItemAdapter(this, CoordiItemAdapter.TYPE_TOP)
    private var bottomAdapter = CoordiDetailItemAdapter(this, CoordiItemAdapter.TYPE_BOTTOM)
    private var outerAdapter = CoordiDetailItemAdapter(this,CoordiItemAdapter.TYPE_OUTER)
    private var dressAdapter = CoordiDetailItemAdapter(this,CoordiItemAdapter.TYPE_DRESS)


    private var coordiID = 0
    private var userID = 0
    private var friendID = 0

    // 기존에 선택한 아이템
    private var selectedTopId = 0
    private var selectedBottomId = 0
    private var selectedOuterId = 0
    private var selectedDressId = 0


    // 새로 선택한 아이템들
    private var selectTopId = 0
    private var selectBottomId = 0
    private var selectOuterId = 0
    private var selectDressId = 0

    private var currentUploadClosetCount = 0
    private var totalUploadClosetCount = 0
    private var currentDeleteClosetCount = 0
    private var totalDeleteClosetCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordi_detail)


        coordiID = intent.getIntExtra(KEY_COORDI_ID, 0)
        userID = intent.getIntExtra(KEY_USER_ID, 0)
        friendID = intent.getIntExtra(KEY_FRIEND_ID, 0)

        Log.e("CoordiDetailActivity", "coordiID=" + coordiID + "\nuserID=" + userID+"\nfriendID="+friendID)

        layoutInit()
        reqCloth(if(friendID == 0 )userID else friendID)
        reqCoordiDetail()
    }


    fun layoutInit() {
        recyclerView_category_top.apply {
            adapter = topAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        recyclerView_category_bottom.apply {
            adapter = bottomAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        recyclerView_category_outer.apply {
            adapter = outerAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        recyclerView_category_onepiece.apply {
            adapter = dressAdapter
            layoutManager = LinearLayoutManager(context,  RecyclerView.HORIZONTAL, false)
        }

        coordiOuter.setOnClickListener {
            selectOuterId = 0
            coordiOuter.visibility = View.GONE
        }

        if(coordiID == 0){
            inputCoordiname.visibility = View.VISIBLE
            coordiName.visibility = View.INVISIBLE
        }else{
            inputCoordiname.visibility = View.INVISIBLE
            coordiName.visibility = View.VISIBLE
        }

        coordiOuter.setOnClickListener {
            selectOuterId = 0
            coordiOuter.visibility = View.GONE
        }

        save.setOnClickListener {

            if(coordiID == 0){
                // 코디 등록하는 경우
                saveCloth()
            }else{

                currentDeleteClosetCount = 0
                totalDeleteClosetCount = 0


                if(selectedTopId != 0){
                    // 탑 지울게 있을 경우
                    if(selectTopId == 0 && selectDressId != 0){
                        // 근데 새로운 아무 옷도 선택하지 않았고, 드레스는 선택했으면 지워야지
                        totalDeleteClosetCount++
                        deleteCloth(selectedTopId,coordiID)
                    }else if(selectTopId != 0){
                        // 새로운 상의를 골랐을때
                        totalDeleteClosetCount++
                        deleteCloth(selectedTopId,coordiID)
                    }
                }

                if(selectedBottomId != 0){
                    // 하의 지울게 있을 경우
                    if(selectBottomId == 0 && selectDressId != 0){
                        // 근데 새로운 아무 옷도 선택하지 않았고, 드레스는 선택했으면 지워야지
                        totalDeleteClosetCount++
                        deleteCloth(selectedBottomId,coordiID)
                    }else if(selectBottomId != 0){
                        // 새로운 상의를 골랐을때
                        totalDeleteClosetCount++
                        deleteCloth(selectedBottomId,coordiID)
                    }
                }


                if(selectedDressId !=0 ){
                    // 선택한 드레스가 있어
                    if(selectTopId != 0 && selectBottomId != 0){
                        // 새로운 상하의를 고른 꼉우
                        totalDeleteClosetCount++
                        deleteCloth(selectedDressId,coordiID)
                    }else if(selectDressId != 0){
                        // 새로운 원피스를 고를 경우
                        totalDeleteClosetCount++
                        deleteCloth(selectedDressId,coordiID)
                    }

                }

                if(selectedOuterId != 0){
                    if(coordiOuter.visibility == View.GONE){
                        totalDeleteClosetCount++
                        deleteCloth(selectedOuterId,coordiID)
                    }else if(selectOuterId != 0){
                        totalDeleteClosetCount++
                        deleteCloth(selectedOuterId,coordiID)
                    }
                }


                if(totalDeleteClosetCount == 0){
                    // 삭제할게 없는 경우

                    if(selectTopId != 0){
                        totalUploadClosetCount++
                        uploadCloth(selectTopId,coordiID)
                    }
                    if(selectBottomId != 0){
                        totalUploadClosetCount++
                        uploadCloth(selectBottomId,coordiID)
                    }
                    if(selectOuterId != 0){
                        totalUploadClosetCount++
                        uploadCloth(selectOuterId,coordiID)
                    }
                    if(selectDressId != 0){
                        totalUploadClosetCount++
                        uploadCloth(selectDressId,coordiID)
                    }
                }


            }
        }
    }

    private fun saveCloth() {

        var outfitName = inputCoordiname.text.toString()
        if(TextUtils.isEmpty(outfitName)){
            Toast.makeText(this,"코디 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        RetrofitClient.retrofitService.getOutfitId(if(friendID == 0 )userID else friendID, if(friendID == 0 )userID else friendID,"Token " + RetrofitClient.token,outfitName)
            .enqueue(object : retrofit2.Callback<GetOutfitIdResponse> {
                override fun onFailure(call: Call<GetOutfitIdResponse>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<GetOutfitIdResponse>,
                    response: Response<GetOutfitIdResponse>
                ) {
                    val data = response.body()
                    Log.e("resopone","data="+data)

                    data?.let{
                        uploadCloth(it.outfitID)
                    }
                }

            })
    }
    private fun uploadCloth(outFitID: Int){
        totalUploadClosetCount  = 0
        currentUploadClosetCount = 0

        if(selectTopId == 0 &&
            selectBottomId== 0 &&
            selectOuterId == 0 &&
            selectDressId == 0){

            if(coordiID == 0){

                var intent = Intent(this@CoordiDetailActivity,CoordiListActivity::class.java)
                intent.putExtra(CoordiListActivity.KEY_USER_ID,userID)
                startActivity(intent)
            }else{
                Toast.makeText(this@CoordiDetailActivity,"코디가 정상적으로 수정됐습니다.",Toast.LENGTH_SHORT).show()
                finish()
            }
        }else{
            if(selectTopId!=0){
                totalUploadClosetCount++
                uploadCloth(selectTopId,outFitID)
            }
            if(selectBottomId!=0){
                totalUploadClosetCount++
                uploadCloth(selectBottomId,outFitID)
            }
            if(selectOuterId!=0){
                totalUploadClosetCount++
                uploadCloth(selectOuterId,outFitID)
            }
            if(selectDressId!=0){
                totalUploadClosetCount++
                uploadCloth(selectDressId,outFitID)
            }
        }



    }
    private fun deleteCloth(closetID: Int, outFitID: Int){
        RetrofitClient.retrofitService.deleteClothCoordi(
            "Token " + RetrofitClient.token, if(friendID == 0 )userID else friendID, closetID, outFitID
        )
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    val data = response.body()
                    if (response.isSuccessful) {
                        currentDeleteClosetCount++
                        if (currentDeleteClosetCount == totalDeleteClosetCount) {
                            uploadCloth(outFitID)
                        }
                    }

                }

            })
    }

    private fun uploadCloth(closetID: Int, outFitID: Int) {

        RetrofitClient.retrofitService.addClothCoordi(
            "Token " + RetrofitClient.token, if(friendID == 0 )userID else friendID, closetID, outFitID
        )
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    val data = response.body()
                    if (response.isSuccessful) {
                        currentUploadClosetCount++
                        if (totalUploadClosetCount == currentUploadClosetCount) {
                            if(coordiID == 0){

                                var intent = Intent(this@CoordiDetailActivity,CoordiListActivity::class.java)
                                intent.putExtra(CoordiListActivity.KEY_USER_ID,if(friendID == 0 )userID else friendID)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this@CoordiDetailActivity,"코디가 정상적으로 수정됐습니다.",Toast.LENGTH_SHORT).show()
                                finish()
                            }

                        }
                    }

                }

            })
    }

    private fun reqCoordiDetail() {
        if(coordiID == 0) return
        RetrofitClient.retrofitService.getOutFitDetail(
            if(friendID == 0 )userID else friendID,
            coordiID,
            "Token " + RetrofitClient.token
        )
            .enqueue(object : retrofit2.Callback<CoordiList> {
                override fun onFailure(call: Call<CoordiList>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<CoordiList>,
                    response: Response<CoordiList>
                ) {
                    val data = response.body()
                    data?.let {
                        coordiName.setText("${it.outfitName}")

                        for(i in 0 until it.clothes.size){
                            var item = it.clothes[i]
                            if(TextUtils.equals(item.category, "TOP")) {
                                selectedTopId = item.clothID

                                Glide.with(this@CoordiDetailActivity)
                                    .load(RetrofitClient.imageBaseUrl + item.photo)
                                    .placeholder(R.drawable.circle)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(coordiTop)
                            }
                            if(TextUtils.equals(item.category, "BOTTOM")) {
                                selectedBottomId = item.clothID

                                Glide.with(this@CoordiDetailActivity)
                                    .load(RetrofitClient.imageBaseUrl + item.photo)
                                    .placeholder(R.drawable.circle)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(coordiBottom)
                            }
                            if(TextUtils.equals(item.category, "OUTER")) {
                                selectedOuterId = item.clothID

                                Glide.with(this@CoordiDetailActivity)
                                    .load(RetrofitClient.imageBaseUrl + item.photo)
                                    .placeholder(R.drawable.circle)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(coordiOuter)
                            }
                            if(TextUtils.equals(item.category, "DRESS")) {
                                selectedDressId = item.clothID

                                coordiDress.visibility = View.VISIBLE
                                Glide.with(this@CoordiDetailActivity)
                                    .load(RetrofitClient.imageBaseUrl + item.photo)
                                    .placeholder(R.drawable.circle)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(coordiDress)
                            }
                        }

                    }

                }

            })
    }

    private fun reqCloth(id: Int) {
        RetrofitClient.retrofitService.getCloth(id, "Token " + RetrofitClient.token)
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

                    topItem.addAll(data?.filter { TextUtils.equals(it.category, "TOP") }
                        ?: mutableListOf())
                    topAdapter.clothList = topItem

                    bottomItem.addAll(data?.filter { TextUtils.equals(it.category, "BOTTOM") }
                        ?: mutableListOf())
                    bottomAdapter.clothList = bottomItem

                    outerItem.addAll(data?.filter {  TextUtils.equals(it.category,"OUTER") } ?: mutableListOf())
                    outerAdapter.clothList = outerItem

                    dressItem.addAll(data?.filter {  TextUtils.equals(it.category,"DRESS") } ?: mutableListOf())
                    dressAdapter.clothList = dressItem
                }

            })
    }


    fun clickTop(item: Cloth) {
        coordiDress.visibility = View.INVISIBLE
        coordiBottom.visibility = View.VISIBLE
        coordiTop.visibility = View.VISIBLE
        selectDressId = 0
        selectTopId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl + item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiTop)
        selectTopId = item.clothID
    }

    fun clickBottom(item: Cloth) {
        coordiDress.visibility = View.INVISIBLE
        coordiBottom.visibility = View.VISIBLE
        coordiTop.visibility = View.VISIBLE
        selectDressId = 0
        selectBottomId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl + item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiBottom)
        selectBottomId = item.clothID
    }
    fun clickDress(item : Cloth){
        coordiBottom.visibility = View.INVISIBLE
        coordiTop.visibility = View.INVISIBLE
        coordiDress.visibility = View.VISIBLE
        selectTopId = 0
        selectBottomId = 0
        selectDressId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiDress)

    }

    fun clickOuter(item: Cloth){

        coordiOuter.visibility = View.VISIBLE
        selectOuterId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiOuter)
    }
}
