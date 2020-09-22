package com.example.button.startApp_1.activity

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.button.R
import com.example.button.startApp_1.adapter.MainFragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class  MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var currentPhotoPath: String


    private fun configureBottomNavigation() {
        vp_ac_main_frag_pager.adapter =
            MainFragmentStatePagerAdapter(
                supportFragmentManager,
                3
            )
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




