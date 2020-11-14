package com.example.button.startApp_1.data



data class ResInsertCoordiForCalendar(
    var id : Int,
    var outfitID : Int,
    var outfitName : String,
    var date : String,
    var outfit_worn : MutableList<CoordiList>
)
