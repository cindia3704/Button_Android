package com.example.button.startApp_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.button.R
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
                Client_Login.retrofitService.register(
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
                            editor.putString("username", initial_enter_ID.text.toString())
                            editor.commit()
                            finish()
                            val intent1 = Intent(this@RegisterActivity, FragmenyMycloset::class.java)
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