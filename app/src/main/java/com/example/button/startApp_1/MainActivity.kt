package com.example.button.startApp_1

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.button.R
import com.example.button.startApp_1.data.User
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class  MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String


    private fun configureBottomNavigation() {
        vp_ac_main_frag_pager.adapter = MainFragmentStatePagerAdapter(supportFragmentManager, 3)
        tl_ac_main_bottom_menu.setupWithViewPager(vp_ac_main_frag_pager)

        val bottomNaviLayout: View =
            this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)
        tl_ac_main_bottom_menu.getTabAt(0)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_recommend_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(1)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_mycloset_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(2)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_calendar_tab) as RelativeLayout
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val clothLists=ArrayList<Clothes>()
//        for(i in 0 until 5){
//            clothLists.add(Clothes(name=""+i+"번째 옷"))
//        }
//        val adapter=clothAdapter(clothLists, LayoutInflater.from(this@MainActivity))
//        recyclerView_category.adapter=adapter
//        recyclerView_category.layoutManager=LinearLayoutManager(this@MainActivity)

        configureBottomNavigation()
        vp_ac_main_frag_pager.setCurrentItem(1)
        reqUser()

    }

    private fun reqUser(){
        Client_Login.retrofitService.getUser().enqueue(object : retrofit2.Callback<MutableList<User>>{
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                val data = response.body()?.get(0)
                reqCloth(data?.id?:1)
            }
        })
    }

    private fun reqCloth(id : Int){
        println("test="+Client_Login.token)
        Client_Login.retrofitService.getCloth(id,Client_Login.token).enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                println("data="+response.body())
            }

        })
    }

    // 옷 객체 만들어서 옷 리스트에 추가하는 함수
//    fun createFakeClothList(numberOfItems:Int=5, clothList:ClothList= ClothList()):ClothList{
//        for(i in 0 until numberOfItems){
//            clothList.addClothes(
//                Clothes()
//            )
//        }
//        return clothList
//    }
//
//    // 위에서 만든 옷 리스트를 실제 뷰에 추가하기
//    fun createClothList(clothList:ClothList){
//        val layoutInflater=LayoutInflater.from(this@MainActivity)
//        val container=findViewById<recyclerView >(R.id.clothList_view)
//        for(i in 0 until clothList.list.size){
//            val viewOfCloth=layoutInflater.inflate(R.layout.mycloset_clothes,null)
//            val clothView=viewOfCloth.findViewById<ImageView>(R.id.cloth_image)
////            val clothTextView=viewOfCloth.findViewById<TextView>(R.id.cloth_text)
////            clothTextView.setText(clothList.list.get(i).name)
//            container.addView(clothView)
//        }
//    }

}




