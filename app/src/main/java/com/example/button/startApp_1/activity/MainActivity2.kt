package com.example.button.startApp_1.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.button.R
import com.example.button.startApp_1.adapter.MainFragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main2.tl_ac_main_bottom_menu
import kotlinx.android.synthetic.main.activity_main2.user_profile_image
import kotlinx.android.synthetic.main.activity_main2.vp_ac_main_frag_pager

class MainActivity2 : AppCompatActivity() {

    private var userId: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        userId = getIntent().getIntExtra("userId", 1)

        init()
    }

    private fun init() {
        vp_ac_main_frag_pager.run {

            offscreenPageLimit = 3
            adapter = MainFragmentStatePagerAdapter(
                supportFragmentManager,
                5,
                userId
            )

            currentItem = 2
        }

        user_profile_image.setOnClickListener {
            val intent = Intent(this, MyProfile::class.java)
            intent.putExtra("id", userId)
            startActivity(intent)
        }

        setBottomNavi()
    }

    private fun setBottomNavi() {

        val bottomNaviLayout: View =
            this.layoutInflater.inflate(R.layout.bottom_navigation_tab, null, false)



        tl_ac_main_bottom_menu.run {
            setupWithViewPager(vp_ac_main_frag_pager)

            getTabAt(0)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_recommend_tab) as RelativeLayout

            getTabAt(1)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_mycloset_tab) as RelativeLayout

            getTabAt(2)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_selfcoordi_tab) as RelativeLayout

            getTabAt(3)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_friendlist_tab) as RelativeLayout

            getTabAt(4)!!.customView =
                bottomNaviLayout.findViewById(R.id.btn_bottom_navi_calendar_tab) as RelativeLayout
        }
    }
}
