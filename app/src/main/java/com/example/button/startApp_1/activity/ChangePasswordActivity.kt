package com.example.button.startApp_1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.button.R
import com.example.button.startApp_1.data.FriendAddResponse
import com.example.button.startApp_1.data.ReqChangePasswordBody
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {


    private var userId : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        userId = intent.getIntExtra("userId",0)


        change.setOnClickListener {


            var nowPassword = now_password.text.toString()
            var newPassword = new_password.text.toString()
            var newPasswordConfirm = new_password_confirm.text.toString()

            if(TextUtils.isEmpty(nowPassword)){
                Toast.makeText(this,"현재 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(newPassword)){
                Toast.makeText(this,"새 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(newPasswordConfirm)){
                Toast.makeText(this,"새 비밀번호 확인을 입력해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!TextUtils.equals(newPasswordConfirm,newPassword)){
                Toast.makeText(this,"새 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            var reqBody = ReqChangePasswordBody(newPassword,nowPassword)
            RetrofitClient.retrofitService.changePassword(userId, reqBody,"Token " + RetrofitClient.token)
                .enqueue(object : retrofit2.Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        val data = response.message()
                        if(response.isSuccessful){
                            Toast.makeText(this@ChangePasswordActivity,"비밀번호가 변경됐습니다.",Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this@ChangePasswordActivity,"기존 비밀번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show()
                        }
                        Log.e("data","respon="+data)

                    }
                })
        }

        back.setOnClickListener {
            finish()
        }
    }
}
