package com.example.button.startApp_1.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Cloth(val id: Int,val clothID:Int,val color:String?,val season:MutableList<String>,val category : String, val dateBought : String, val dateLastWorn : String, val photo : String,val outfit : MutableList<Int>) :
    Parcelable
