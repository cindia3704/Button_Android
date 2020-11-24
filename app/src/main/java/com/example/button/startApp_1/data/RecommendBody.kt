package com.example.button.startApp_1.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class RecommendBody(
    val id: Int,
    val season: String,
    val place1: Int,
    val place2: Int,
    val people1: Int,
    val people2: Int,
    val event1: Int,
    val event2: Int,
    val mood: Int
) : Parcelable
