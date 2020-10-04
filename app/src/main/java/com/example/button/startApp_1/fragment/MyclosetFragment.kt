package com.example.button.startApp_1.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.example.button.startApp_1.activity.AddClosetActivity
import com.example.button.startApp_1.adapter.ClothAdapter
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_mycloset.*
import retrofit2.Call
import retrofit2.Response

class MyclosetFragment : Fragment() {
    private lateinit var topClothadapter : ClothAdapter
    private lateinit var bottomClothadapter : ClothAdapter
    private lateinit var outerClothadapter : ClothAdapter
    private lateinit var onepieceClothadapter : ClothAdapter
    private var adapterList = mutableListOf<ClothAdapter>()
    private lateinit var mContext : Context
    private val categoryList = mutableListOf("TOP","BOTTOM","OUTER","DRESS")

    private var userId = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mycloset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutInit()
        reqUser()
    }

    private fun layoutInit(){
        topClothadapter = ClothAdapter(
            LayoutInflater.from(activity), mContext
        )
        adapterList.add(topClothadapter)
        recyclerView_category_top.apply {
            adapter= topClothadapter
            layoutManager = GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)
        }

        bottomClothadapter = ClothAdapter(
            LayoutInflater.from(activity), mContext
        )
        recyclerView_category_bottom.apply {
            adapter = bottomClothadapter
            layoutManager = GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)
        }
        adapterList.add(bottomClothadapter)
        outerClothadapter = ClothAdapter(
            LayoutInflater.from(activity), mContext
        )

        recyclerView_category_outer.apply {
            adapter=outerClothadapter
            layoutManager=GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)
        }
        adapterList.add(outerClothadapter)
        onepieceClothadapter =
            ClothAdapter(
                LayoutInflater.from(activity), mContext
            )
        recyclerView_category_onepiece.apply {
            adapter = onepieceClothadapter
            layoutManager=GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)
        }
        adapterList.add(onepieceClothadapter)


        add_clothes_top.setOnClickListener {
            var intent = Intent(context,AddClosetActivity::class.java)
            intent.putExtra("id",userId)
            intent.putExtra("category","TOP")
            startActivity(intent)
        }
        add_clothes_bottom.setOnClickListener {
            var intent = Intent(context,AddClosetActivity::class.java)
            intent.putExtra("id",userId)
            intent.putExtra("category","BOTTOM")
            startActivity(intent)
        }
        add_clothes_outer.setOnClickListener {
            var intent = Intent(context,AddClosetActivity::class.java)
            intent.putExtra("id",userId)
            intent.putExtra("category","OUTER")
            startActivity(intent)
        }
        add_clothes_onepiece.setOnClickListener {
            var intent = Intent(context,AddClosetActivity::class.java)
            intent.putExtra("id",userId)
            intent.putExtra("category","ONEPIECE")
            startActivity(intent)
        }
    }

    private fun reqUser(){
        setProgress()
        RetrofitClient.retrofitService.getUser().enqueue(object : retrofit2.Callback<MutableList<User>>{
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                val data = response.body()?.get(0)
                userId = data?.id?:1

                topClothadapter.user_id = userId
                bottomClothadapter.user_id = userId
                outerClothadapter.user_id = userId
                onepieceClothadapter.user_id = userId

                reqCloth(userId)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        reqCloth(userId)
    }

    private fun reqCloth(id : Int){
        RetrofitClient.retrofitService.getCloth(id,"Token "+ RetrofitClient.token).enqueue(object : retrofit2.Callback<MutableList<Cloth>>{
            override fun onFailure(call: Call<MutableList<Cloth>>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<MutableList<Cloth>>, response: Response<MutableList<Cloth>>) {
                val data = response.body()
                for(i in 0 until categoryList.size){
                    sortData(i,data)
                }
                setProgress()
            }

        })
    }

    private fun sortData(index : Int , list:MutableList<Cloth>?){
        val data = mutableListOf<Cloth>()
        data.addAll(list?.filter { it.category == categoryList[index] }?: mutableListOf())
        adapterList[index].setClothList(data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    private fun setProgress(){
        when(progress.visibility){
            View.GONE -> progress.visibility = View.VISIBLE
            View.VISIBLE -> progress.visibility = View.GONE
        }
    }

}



//
//class categoryAdapter(
//    val categoryList:ArrayList<String>,
//    val inflater: LayoutInflater
//):RecyclerView.Adapter<categoryAdapter.ViewHolder>(){
//    class ViewHolder(categoryView:View):RecyclerView.ViewHolder(categoryView){
//        val categoryText:TextView
//
//        init{
//            categoryText=categoryView.findViewById(R.id.category)
//        }
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryAdapter.ViewHolder {
//        val view=inflater.inflate(R.layout.mycloset_category,parent,false)
//        return categoryAdapter.ViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return categoryList.size
//    }
//
//    override fun onBindViewHolder(holder: categoryAdapter.ViewHolder, position: Int) {
//        holder.categoryText.setText(categoryList.get(position))
//    }
//}
