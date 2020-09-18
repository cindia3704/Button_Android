package com.example.button.startApp_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.button.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_navigation_tab.*
import kotlinx.android.synthetic.main.fragment_mycloset.*
import kotlinx.android.synthetic.main.mycloset_category.*
import java.lang.reflect.Array.set
import com.example.button.startApp_1.Clothes
import com.example.button.startApp_1.ClothList

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




