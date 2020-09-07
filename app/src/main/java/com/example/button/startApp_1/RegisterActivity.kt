package com.example.button.startApp_1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
        //gender_all.checkedRadioButtonId
//        if (gender_all.getCheckedRadioButtonId() === -1) {
//            Toast.makeText(applicationContext, "Please select Gender", Toast.LENGTH_SHORT)
//                .show()
//        } else {
//            // get selected radio button from radioGroup
//            val selectedId: Int = gender_all.getCheckedRadioButtonId()
//            // find the radiobutton by returned id
//            var selectedRadioButton = findViewById<View>(selectedId) as RadioButton
//            if(selectedRadioButton.getText().toString().toString()=="여성")
//            Toast.makeText(
//                applicationContext,
//                selectedRadioButton.getText().toString().toString() + " is selected",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//        if(gender_female.isChecked){
//           var gender="FEMALE"
//            Log.d("gender","female")
//        }
//        else if(gender_male){
//            var gender="MALE"
//            Log.d("gender","male")
//        }
//        else{
//           var gender="ETC"
//            Log.d("gender","etc")
//        }
        var gender ="ETC"

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
                            val intent1 = Intent(this@RegisterActivity, MyCloset::class.java)
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