package com.example.button.startApp_1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.button.R
import com.example.button.startApp_1.adapter.CoordiItemAdapter

class AddCoordiActivity : AppCompatActivity() {


    var friendUserId : Int = 0
    private var selectTopId = 0
    private var selectBottomId = 0
    private var uploadClosetCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_coordi)


        friendUserId = intent.getIntExtra("friendId",0)
    }
}
