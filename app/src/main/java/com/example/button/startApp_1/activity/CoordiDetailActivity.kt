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
import kotlinx.android.synthetic.main.activity_coordi_detail.coordiTop
import kotlinx.android.synthetic.main.activity_coordi_detail.recyclerView_category_bottom
import kotlinx.android.synthetic.main.activity_coordi_detail.recyclerView_category_top
import kotlinx.android.synthetic.main.activity_coordi_detail.save
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


    private var coordiID = 0
    private var userID = 0
    private var friendID = 0

    private var selectTopId = 0
    private var selectBottomId = 0
    private var uploadClosetCount = 0
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

        if(coordiID == 0){
            inputCoordiname.visibility = View.VISIBLE
            coordiName.visibility = View.INVISIBLE
        }else{
            inputCoordiname.visibility = View.INVISIBLE
            coordiName.visibility = View.VISIBLE
        }

        save.setOnClickListener {
            uploadClosetCount = 0

            if(coordiID == 0){
                // 친구꺼 코디 추가하는 경우
                saveCloth()
            }else{

                uploadCloth(selectTopId, coordiID)
                uploadCloth(selectBottomId, coordiID)
            }
        }
    }

    private fun saveCloth() {

        var outfitName = inputCoordiname.text.toString()
        if(TextUtils.isEmpty(outfitName)){
            Toast.makeText(this,"코디 이름을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        RetrofitClient.retrofitService.getOutfitId(userID, userID,"Token " + RetrofitClient.token,outfitName)
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
                        uploadClosetCount = 0

                        uploadCloth(selectTopId,it.outfitID)
                        uploadCloth(selectBottomId,it.outfitID)
                    }
                }

            })
    }

    private fun uploadCloth(closetID: Int, outFitID: Int) {

        RetrofitClient.retrofitService.uploadOutfit(
            "Token " + RetrofitClient.token, if(friendID == 0 )userID else friendID, closetID, if(friendID == 0 )userID else friendID, outFitID
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
                        uploadClosetCount++
                        if (uploadClosetCount == 2) {
                            if(coordiID == 0){

                                var intent = Intent(this@CoordiDetailActivity,CoordiListActivity::class.java)
                                intent.putExtra(CoordiListActivity.KEY_USER_ID,userID)
                                startActivity(intent)
                            }else{
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
            userID,
            coordiID
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

                        when (it.clothes.size) {
                            0 -> {
                                coordiTop.setImageResource(R.drawable.circle)
                                coordiBottom.setImageResource(R.drawable.circle)


                            }

                            1 -> {
                                selectTopId = it.clothes[0].clothID

                                Glide.with(this@CoordiDetailActivity)
                                    .load(RetrofitClient.imageBaseUrl + it.clothes[0].photo)
                                    .placeholder(R.drawable.circle)
                                    .apply(RequestOptions.circleCropTransform()).into(coordiTop)

                                coordiBottom.setImageResource(R.drawable.circle)
                            }


                            else -> {
                                var findTop = false
                                var findBottom = false
                                var index = it.clothes.size - 1

                                while (index >= 0 && (!findTop || !findBottom)) {

                                    var item = it.clothes[index]

                                    if (!findTop && TextUtils.equals(item.category, "TOP")) {
                                        findTop = true
                                        selectTopId = item.clothID

                                        Glide.with(this@CoordiDetailActivity)
                                            .load(RetrofitClient.imageBaseUrl + item.photo)
                                            .placeholder(R.drawable.circle)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(coordiTop)
                                    }

                                    if (!findBottom && TextUtils.equals(item.category, "BOTTOM")) {
                                        findBottom = true
                                        selectBottomId = item.clothID

                                        Glide.with(this@CoordiDetailActivity)
                                            .load(RetrofitClient.imageBaseUrl + item.photo)
                                            .placeholder(R.drawable.circle)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(coordiBottom)
                                    }
                                    index--
                                }

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
                    topItem.addAll(data?.filter { TextUtils.equals(it.category, "TOP") }
                        ?: mutableListOf())
                    topAdapter.clothList = topItem

                    bottomItem.addAll(data?.filter { TextUtils.equals(it.category, "BOTTOM") }
                        ?: mutableListOf())
                    bottomAdapter.clothList = bottomItem
                }

            })
    }


    fun clickTop(item: Cloth) {
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl + item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiTop)
        selectTopId = item.clothID
    }

    fun clickBottom(item: Cloth) {
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl + item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiBottom)
        selectBottomId = item.clothID
    }
}
