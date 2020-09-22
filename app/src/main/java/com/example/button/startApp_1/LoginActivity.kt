package com.example.button.startApp_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.button.R
import com.example.button.startApp_1.data.LoginResponse
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // 로그인
        val PREFERENCE = "com.example.button"
        login.setOnClickListener {
            requestLogin()
           /* Client_Login.retrofitService.logIn(enter_ID.text.toString(), enter_PW.text.toString()).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                        val editor = pref.edit()
                        editor.putString("username", enter_ID.text.toString())
                        editor.commit()
                        finish()
                        val intent1=Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent1)
                        }
                    else{
                        Toast.makeText(this@LoginActivity,"로그인 실패!!! \n아이디와 비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@LoginActivity,"실패!!!",Toast.LENGTH_SHORT).show()
                    Log.d("error",t.toString())
                }
            })*/

        }
        // 회원가입
        register.setOnClickListener({
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        })


        // 아이디 찾기
        lost_id.setOnClickListener {
            val intent = Intent(this, FindEmailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestLogin(){
        Client_Login.retrofitService.logIn(enter_ID.text.toString(), enter_PW.text.toString()).enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@LoginActivity,"실패!!!",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.body()?.token!=null) {
                    Client_Login.token = response.body()?.token?:""
                    /*val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putString("username", enter_ID.text.toString())
                    editor.commit()*/
                    val intent1=Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent1)
                    finish()
                }
                else{
                    Toast.makeText(this@LoginActivity,"로그인 실패!!! \n아이디와 비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show()
                }

            }

        })
    }
}

