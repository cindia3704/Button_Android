package com.example.button.startApp_1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.example.button.startApp_1.adapter.CoordiListItemAdapter
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.CoordiList
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_coordi_list.*
import kotlinx.android.synthetic.main.fragment_recommend.*
import retrofit2.Call
import retrofit2.Response

class CoordiListActivity : AppCompatActivity() {
    companion object {
        const val KEY_USER_ID = "KEY_USER_ID"
        const val KEY_FRIEND_ID = "KEY_FRIEND_ID"
    }

    var listAdapter = CoordiListItemAdapter(this)
    var friendListAdpater = CoordiListItemAdapter(this)
    var userID = 0
    var friendID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordi_list)

        userID = intent.getIntExtra(KEY_USER_ID, 0)
        friendID = intent.getIntExtra(KEY_FRIEND_ID, 0)
        Log.e("CoordiListActivity","userID="+userID+"\nfriendID="+friendID)

        layoutInit()
    }

    private fun getFriendCoordiList(){
        var id = if(friendID==0) userID else friendID
        RetrofitClient.retrofitService.getFriendCoordiList(id, "Token " + RetrofitClient.token)
            .enqueue(object : retrofit2.Callback<MutableList<CoordiList>> {
                override fun onFailure(call: Call<MutableList<CoordiList>>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<MutableList<CoordiList>>,
                    response: Response<MutableList<CoordiList>>
                ) {
                    val data = response.body()
                    friendListAdpater.myCoordiList = data ?: mutableListOf()
                }

            })

    }
    private fun getCoordiList() {
        var id = if(friendID==0) userID else friendID
        RetrofitClient.retrofitService.getCoordiList(id, "Token " + RetrofitClient.token)
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
                    Log.e("data", "data=" + data)
                }

            })
    }

    override fun onResume() {
        super.onResume()
        getCoordiList();

        getFriendCoordiList()
    }


    private fun layoutInit() {

        listAdapter.userId = userID
        listAdapter.friendId = friendID

        friendListAdpater.userId = userID
        friendListAdpater.friendId = friendID
        recyclerviewList.apply {
            adapter = listAdapter
            layoutManager=GridLayoutManager(this@CoordiListActivity,2,RecyclerView.HORIZONTAL,false)
        }


        friendList.apply {
            adapter = friendListAdpater
            layoutManager=GridLayoutManager(this@CoordiListActivity,2,RecyclerView.HORIZONTAL,false)
        }

        edit.setOnClickListener {
            listAdapter.updateEdit()
        }
        back.setOnClickListener {
            finish()
        }
    }

    fun clickItem(item: CoordiList) {

        var intent = Intent(this, CoordiDetailActivity::class.java)
        intent.putExtra(CoordiDetailActivity.KEY_USER_ID, userID)
        intent.putExtra(CoordiDetailActivity.KEY_COORDI_ID, item.outfitID)
        startActivity(intent)
    }

    fun deleteItem(item: CoordiList) {
        RetrofitClient.retrofitService.deleteOutfit(
            userID, item.outfitID,
            "Token " + RetrofitClient.token
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
                    Log.e("data", "data=" + data)

                    if (response.isSuccessful)
                        listAdapter.deleteItem(item)
                }

            })
    }
}
