package com.example.button.startApp_1.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.button.R
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_my_profile.*
import retrofit2.Call
import retrofit2.Response

class MyProfile : AppCompatActivity() {
    var userId: Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        userId=getIntent().getIntExtra("id",3)
        Toast.makeText(this@MyProfile,"id:"+userId,Toast.LENGTH_SHORT).show()
        RetrofitClient.retrofitService.getUserSpecific(
            userId,
            "Token " + RetrofitClient.token
        ).enqueue(object :
            retrofit2.Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
              val email = response.body()!!.userEmail
                val nickname_=response.body()!!.userNickName
                profile_id.setText(email.toString())
                profile_name.setText(nickname_)
            }
        })

        change_pw.setOnClickListener {
            val intent= Intent(this@MyProfile,ChangePasswordActivity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }
        logout.setOnClickListener {
            val intent= Intent(this@MyProfile,SplashActivity::class.java)
            startActivity(intent)
            Toast.makeText(this@MyProfile,"로그아웃이 정상적으로 되었습니다",Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity()
            }
        }
    }
}
