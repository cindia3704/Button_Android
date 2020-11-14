package com.example.button.startApp_1.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.example.button.startApp_1.activity.AddClosetActivity
import com.example.button.startApp_1.activity.LoginActivity
import com.example.button.startApp_1.activity.MainActivity
import com.example.button.startApp_1.adapter.ClothAdapter
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_add_closet.*
import kotlinx.android.synthetic.main.fragment_mycloset.*
import retrofit2.Call
import retrofit2.Response

class MyclosetFragment : Fragment() {


    private lateinit var topClothadapter: ClothAdapter
    private lateinit var bottomClothadapter: ClothAdapter
    private lateinit var outerClothadapter: ClothAdapter
    private lateinit var onepieceClothadapter: ClothAdapter
    private var adapterList = mutableListOf<ClothAdapter>()
    private lateinit var mContext: Context
    private val categoryList = mutableListOf("TOP", "BOTTOM", "OUTER", "DRESS")
    var userId: Int? = 5


    private val weatherList = arrayOf("전체","여름","겨울","환절기(봄, 가을)")
    private val weatherValueList = arrayOf("ALL","SUMMER","WINTER","HWAN")
    private var selectWeatherIndex = 0
    private var weatherAdapter : ArrayAdapter<String>? = null


    companion object {
        private const val MY_INT = "userId"
        fun newInstance(userId: Int): MyclosetFragment {
            val frag = MyclosetFragment()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getInt(MY_INT)
        return inflater.inflate(R.layout.fragment_mycloset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutInit()
    }

    private fun layoutInit() {
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

        topClothadapter = ClothAdapter(
            LayoutInflater.from(activity), mContext
        )
        adapterList.add(topClothadapter)
        recyclerView_category_top.apply {
            adapter = topClothadapter
            layoutManager = GridLayoutManager(activity, 2, RecyclerView.HORIZONTAL, false)
        }

        bottomClothadapter = ClothAdapter(
            LayoutInflater.from(activity), mContext
        )
        recyclerView_category_bottom.apply {
            adapter = bottomClothadapter
            layoutManager = GridLayoutManager(activity, 2, RecyclerView.HORIZONTAL, false)
        }
        adapterList.add(bottomClothadapter)
        outerClothadapter = ClothAdapter(
            LayoutInflater.from(activity), mContext
        )

        recyclerView_category_outer.apply {
            adapter = outerClothadapter
            layoutManager = GridLayoutManager(activity, 2, RecyclerView.HORIZONTAL, false)
        }
        adapterList.add(outerClothadapter)
        onepieceClothadapter =
            ClothAdapter(
                LayoutInflater.from(activity), mContext
            )

//        if(!TextUtils.equals(RetrofitClient.gender,"FEMALE")){
//            recyclerView_category_onepiece.visibility = View.GONE
//            ll_onepice.visibility = View.GONE
//        }else{
//
//        }
        recyclerView_category_onepiece.apply {
            adapter = onepieceClothadapter
            layoutManager = GridLayoutManager(activity, 2, RecyclerView.HORIZONTAL, false)
        }
        adapterList.add(onepieceClothadapter)



        topClothadapter.user_id = userId
        bottomClothadapter.user_id = userId
        outerClothadapter.user_id = userId
        onepieceClothadapter.user_id = userId

        add_clothes_top.setOnClickListener {
            var intent = Intent(context, AddClosetActivity::class.java)
            Log.e("userId","MyclosetFragment userId="+userId)
            intent.putExtra("userId", userId)
            intent.putExtra("category", "TOP")
            startActivity(intent)
        }
        add_clothes_bottom.setOnClickListener {
            var intent = Intent(context, AddClosetActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("category", "BOTTOM")
            startActivity(intent)
        }
        add_clothes_outer.setOnClickListener {
            var intent = Intent(context, AddClosetActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("category", "OUTER")
            startActivity(intent)
        }
        add_clothes_onepiece.setOnClickListener {
            var intent = Intent(context, AddClosetActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("category", "DRESS")
            startActivity(intent)
        }
    }


    override fun onResume() {
        super.onResume()
        userId?.let { reqCloth(it) }
    }

    private fun reqCloth(id: Int) {
        if(selectWeatherIndex == 0){
            // 전체 불러올때
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
                        for (i in 0 until categoryList.size) {
                            sortData(i, data)
                        }
                    }

                })
        }else{
            RetrofitClient.retrofitService.getCloth(id,weatherValueList[selectWeatherIndex],"Token " + RetrofitClient.token)
                .enqueue(object : retrofit2.Callback<MutableList<Cloth>> {
                    override fun onFailure(call: Call<MutableList<Cloth>>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<MutableList<Cloth>>,
                        response: Response<MutableList<Cloth>>
                    ) {
                        val data = response.body()
                        for (i in 0 until categoryList.size) {
                            sortData(i, data)
                        }
                    }

                })
        }



    }

    private fun sortData(index: Int, list: MutableList<Cloth>?) {
        val data = mutableListOf<Cloth>()
        data.addAll(list?.filter { it.category == categoryList[index] } ?: mutableListOf())
        adapterList[index].setClothList(data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

}
