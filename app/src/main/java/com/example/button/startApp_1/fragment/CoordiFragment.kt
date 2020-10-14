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
import android.view.Window
import android.widget.Button
import android.widget.EditText
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
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response


class CoordiFragment : Fragment() {


    private var userId = 0

    private var topAdapter = CoordiItemAdapter(this,CoordiItemAdapter.TYPE_TOP)
    private var bottomAdapter = CoordiItemAdapter(this,CoordiItemAdapter.TYPE_BOTTOM)



    private var selectTopId = 0
    private var selectBottomId = 0
    private var uploadClosetCount = 0
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
        reqCloth(userId)
    }

    fun layoutInit(){
        recyclerView_category_top.apply {
            adapter = topAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
        recyclerView_category_bottom.apply {
            adapter = bottomAdapter
            layoutManager = LinearLayoutManager(context,  RecyclerView.HORIZONTAL, false)
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

        RetrofitClient.retrofitService.uploadOutfit(
            "Token "+ RetrofitClient.token,userId, closetID,userId, outFitID)
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
                        if(uploadClosetCount == 2){
                            var intent = Intent(context,CoordiListActivity::class.java)
                            intent.putExtra(CoordiListActivity.KEY_USER_ID,userId)
                            startActivity(intent)
                        }
                    }

                }

            })
    }

    private fun saveCloth(){
        var outfitName = coordiName.text.toString()
        if(TextUtils.isEmpty(outfitName)){
            Toast.makeText(context,"코디 이름을 입력해주세요",Toast.LENGTH_SHORT).show()
            return
        }
//        var userIdBody = RequestBody.create(MediaType.parse("text/plain"), (userId).toString())
        RetrofitClient.retrofitService.getOutfitId(userId, userId,"Token " + RetrofitClient.token,outfitName)
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

                        uploadCloth(selectTopId,it.outfitID)
                        uploadCloth(selectBottomId,it.outfitID)

                    }
                    Log.e("resopone","data="+data)
                }

            })
    }

    private fun reqCloth(id : Int){
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
                    topItem.addAll(data?.filter {  TextUtils.equals(it.category,"TOP") } ?: mutableListOf())
                    topAdapter.clothList = topItem

                    bottomItem.addAll(data?.filter {  TextUtils.equals(it.category,"BOTTOM") } ?: mutableListOf())
                    bottomAdapter.clothList = bottomItem
                }

            })
    }


    fun clickTop(item : Cloth){
        Log.e("fragment","clickTop id=${item.clothID}")
        selectTopId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiTop)
    }

    fun clickBottom(item : Cloth){
        Log.e("fragment","clickBottom id=${item.clothID}")
        selectBottomId = item.clothID
        Glide.with(this)
            .load(RetrofitClient.imageBaseUrl+item.photo)
            .placeholder(R.drawable.circle)
            .apply(RequestOptions.circleCropTransform()).into(coordiBottom)
    }


}
