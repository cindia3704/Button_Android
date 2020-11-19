package com.example.button.startApp_1

import android.provider.ContactsContract
import java.io.Serializable

class User(
    var id : Int? = null,
    var password : String? = null,
    var userEmail : String? = null,
    var userNickName : String? = null,
    var userGender : String? = null,
    var dateRegistered : String? = null,
    var confirmedEmail : Boolean? = null,
    var photo:String?=null
):Serializable

