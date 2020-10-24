package com.example.button.startApp_1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.button.R
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_find_password.*
import retrofit2.Call
import retrofit2.Response

class FindPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)

        back.setOnClickListener {

            finish()
        }


        find.setOnClickListener {
            findPassword(email.text.toString())
        }

    }

    private fun findPassword(email : String){
        RetrofitClient.retrofitService.findPassword(email)
            .enqueue(object : retrofit2.Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    val data = response.message()
                    if(response.isSuccessful){
                        Toast.makeText(this@FindPasswordActivity,"입력하신 이메일로 비밀번호가 전송됐습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    Log.e("data","respon="+data)

                }
            })
    }
}
