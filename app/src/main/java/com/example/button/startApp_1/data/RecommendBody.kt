package com.example.button.startApp_1.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class RecommendBody(val id: Int, val season: MutableList<String> ,val place1 : Int,val place2 : Int,val people1 : Int,val people2 : Int,val event1 : Int,val event2 : Int,val mood :Int)
