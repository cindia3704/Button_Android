package com.example.button.startApp_1.activity

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.button.R
import com.example.button.startApp_1.adapter.MainFragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class  ReMainActivity : AppCompatActivity() {

    var userId: Int=1

    private fun configureBottomNavigation(userId : Int) {
        vp_ac_main_frag_pager.adapter =
            MainFragmentStatePagerAdapter(
                supportFragmentManager,
                5,
                userId
            )
        tl_ac_main_bottom_menu.setupWithViewPager(vp_ac_main_frag_pager)

        val bottomNaviLayout: View =
            this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)
        tl_ac_main_bottom_menu.getTabAt(0)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_recommend_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(1)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_mycloset_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(2)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_selfcoordi_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(3)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_friendlist_tab) as RelativeLayout
        tl_ac_main_bottom_menu.getTabAt(4)!!.customView =
            bottomNaviLayout.findViewById(R.id.btn_bottom_navi_calendar_tab) as RelativeLayout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userId=getIntent().getIntExtra("userId",3)

        configureBottomNavigation(userId)
        vp_ac_main_frag_pager.setCurrentItem(1)
    }
}





