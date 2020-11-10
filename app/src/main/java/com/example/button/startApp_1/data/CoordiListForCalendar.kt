package com.example.button.startApp_1.data




data class CoordiListForCalendar(
    var id : Int,
    var clendarID : Int,
    var date : String,
    var outfit_worn : MutableList<CoordiList>
)
