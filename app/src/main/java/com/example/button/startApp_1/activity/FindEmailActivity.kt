package com.example.button.startApp_1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.button.R
import com.example.button.startApp_1.network.RetrofitService
import kotlinx.android.synthetic.main.activity_find_email.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FindEmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_email)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://141.223.121.111:9999/admin/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        yes_to_find_id.setOnClickListener {
            val service = retrofit.create(RetrofitService::class.java)
            var userEmail = enter_email_to_find_id.text.toString()

            service.findUserEmail(userEmail.toString()).enqueue(object : Callback<ExistsOrNot> {
                override fun onResponse(call: Call<ExistsOrNot>, response: Response<ExistsOrNot>) {
                    if (response.isSuccessful) {
                        var exists_text = response.body()!!.exists
                        Log.d("emailtext",exists_text.toString())
                        var exists_string = exists_text.toString()
                        Log.d("eText", exists_text.toString())
                        if (exists_string.equals("true")) {
                            Toast.makeText(
                                this@FindEmailActivity,
                                "해당 아이디가 존재합니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@FindEmailActivity,
                                "해당 아이디는 존재하지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@FindEmailActivity,
                            "해당 아이디는 존재하지 않습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ExistsOrNot>, t: Throwable) {
                    Log.d("error", "에러"+t.toString())
                    Toast.makeText(
                        this@FindEmailActivity,
                        "아이디 찾기에 실패하였습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        go_back_to_login.setOnClickListener({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })
    }

}
class ExistsOrNot(var exists:String){

}

