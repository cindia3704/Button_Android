package com.example.button.startApp_1.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.button.R
import com.example.button.startApp_1.data.LoggedUserInfo
import com.example.button.startApp_1.data.LoginResponse
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인
        login.setOnClickListener {
            //confirmLog()
            val intent1 = Intent(
                this@LoginActivity,
                ReMainActivity::class.java
            )
            requestLogin(intent1)

        }
        // 회원가입
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        // 아이디 찾기
        lost_id.setOnClickListener {
            val intent = Intent(this, FindEmailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestLogin(intent1:Intent){
        RetrofitClient.retrofitService.logIn(enter_ID.text.toString(), enter_PW.text.toString()).enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LoginActivity,"실패!!!",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.body()?.token!=null) {
                    RetrofitClient.token = response.body()?.token?:""
                    var userId=1

                    RetrofitClient.retrofitService.getLoggedUser(enter_ID.text.toString()).enqueue(object :Callback<LoggedUserInfo>{
                        override fun onFailure(call: Call<LoggedUserInfo>, t: Throwable) {
                            Log.d("error", "에러"+t.toString())
                            Toast.makeText(
                                this@LoginActivity,
                                "로그인한 유저 못가져.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<LoggedUserInfo>,
                            response: Response<LoggedUserInfo>
                        ) {
                            userId=response.body()!!.id
                            Log.e("id","userid id="+userId)
                            var confirmedEmail=response.body()!!.confirmedEmail
                            if(confirmedEmail==false){
                                Toast.makeText(this@LoginActivity,"이메일을 인증한 후 다시 시도해 주세요",Toast.LENGTH_SHORT).show()
                            }else {
                                intent1.putExtra("userId", userId)
                                startActivity(intent1)
                                finish()
                            }
                        }
                    })
                    finish()
                }
                else{
                    Toast.makeText(this@LoginActivity,"로그인 실패!!! \n아이디와 비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show()
                }

            }

        })
    }
}
