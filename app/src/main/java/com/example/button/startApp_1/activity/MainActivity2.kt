package com.example.button.startApp_1.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.adapter.MainFragmentStatePagerAdapter
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_main2.tl_ac_main_bottom_menu
import kotlinx.android.synthetic.main.activity_main2.user_profile_image
import kotlinx.android.synthetic.main.activity_main2.vp_ac_main_frag_pager
import kotlinx.android.synthetic.main.activity_my_profile.*
import retrofit2.Call
import retrofit2.Response

class MainActivity2 : AppCompatActivity() {

    private var userId: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        userId = getIntent().getIntExtra("userId", 1)
        RetrofitClient.retrofitService.getUserSpecific(
            userId,
            "Token " + RetrofitClient.token
        ).enqueue(object :
            retrofit2.Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val photo=response.body()!!.photo
                if(photo!="media/button/default.jpg") {
                    Glide.with(this@MainActivity2)
                        .load(RetrofitClient.imageBaseUrl + photo)
                        .placeholder(R.drawable.person__icon1)
                        .apply(RequestOptions.circleCropTransform()).into(user_profile_image)
                }

            }
        })
        init()
    }
    override fun onResume(){
        super.onResume()
        RetrofitClient.retrofitService.getUserSpecific(
            userId,
            "Token " + RetrofitClient.token
        ).enqueue(object :
            retrofit2.Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val photo=response.body()!!.photo
                if(photo!="media/button/default.jpg") {
                    Glide.with(this@MainActivity2)
                        .load(RetrofitClient.imageBaseUrl + photo)
                        .placeholder(R.drawable.person__icon1)
                        .apply(RequestOptions.circleCropTransform()).into(user_profile_image)
                }

            }
        })
    }
    private fun init() {
        vp_ac_main_frag_pager.run {

            offscreenPageLimit = 3
            adapter = MainFragmentStatePagerAdapter(
                supportFragmentManager,
                5,
                userId
            )

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
