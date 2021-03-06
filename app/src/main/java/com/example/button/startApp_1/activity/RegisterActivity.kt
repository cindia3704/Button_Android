package com.example.button.startApp_1.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.button.R
import com.example.button.startApp_1.network.RetrofitClient
import com.example.button.startApp_1.network.RetrofitService
import kotlinx.android.synthetic.main.activity_find_email.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val PREFERENCE = "com.example.button"

        lateinit var radioGroup: RadioGroup
        radioGroup = findViewById(R.id.gender_all)

        // 비밀번호 check!
        var passwordValid:Boolean=false
        nickname.setOnClickListener{
                if(initial_enter_PW.text.toString() == recheck_pw.text.toString()) {
                    passwordValid = true
                }
                else {
                    Toast.makeText(this@RegisterActivity, "비밀번호가 틀렸습니다!!!", Toast.LENGTH_SHORT)
                        .show()
                    passwordValid = false
                }
        }
        findEmail.setOnClickListener {
            var userEmail = initial_enter_ID.text.toString()

            if(TextUtils.isEmpty(userEmail)){
                Toast.makeText(
                    this@RegisterActivity,
                    "이메일을 입력해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            RetrofitClient.retrofitService.findUserEmail(userEmail).enqueue(object : Callback<ExistsOrNot> {
                override fun onResponse(call: Call<ExistsOrNot>, response: Response<ExistsOrNot>) {
                    if (response.isSuccessful) {
                        var exists_text = response.body()!!.exists
                        Log.d("emailtext",exists_text.toString())
                        var exists_string = exists_text.toString()
                        Log.d("eText", exists_text.toString())
                        if (exists_string.equals("true")) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "이미 등록된 아이디입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "등록되지 않은 아이디 입니다. 회원가입을 진행해주세요",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "등록되지 않은 아이디 입니다. 회원가입을 진행해주세요",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ExistsOrNot>, t: Throwable) {

                }
            })
        }

        make_account.setOnClickListener {
            Log.d("pass",passwordValid.toString())
            //Gender 변환
            val gender_id = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(gender_id)
            var gender ="ETC"

            if(radioButton.text.equals("여성")){
                gender="FEMALE"
            }
            else if(radioButton.text.equals("남성")){
                gender="MALE"
            }
            else{
                gender="ETC"
            }
            //Toast.makeText(this@RegisterActivity, radioButton.text, Toast.LENGTH_SHORT).show()
            //Toast.makeText(this@RegisterActivity, gender, Toast.LENGTH_SHORT).show()
            if(passwordValid==false) {
                Toast.makeText(this@RegisterActivity,"비밀번호가 틀렸습니다!!!", Toast.LENGTH_SHORT).show()
            }
            else {
                RetrofitClient.retrofitService.register(
                    userEmail = initial_enter_ID.text.toString(),
                    passward = initial_enter_PW.text.toString(),
                    userNickName = nickname.text.toString(),
                    userGender = gender
                ).enqueue(object :
                    Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
                            val editor = pref.edit()
                            Toast.makeText(
                                this@RegisterActivity,
                                "회원가입이 성공적으로 완료되었습니다.",
                                Toast.LENGTH_SHORT
                            ).show()

                            editor.putString("username", initial_enter_ID.text.toString())
                            editor.commit()
                            finish()
                            val intent1 = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent1)

                        } else {
                            Toast.makeText(this@RegisterActivity, "회원가입 실패!!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "실패!!!", Toast.LENGTH_SHORT).show()
                        Log.d("error", t.toString())
                    }
                })
            }
        }
    }
}
