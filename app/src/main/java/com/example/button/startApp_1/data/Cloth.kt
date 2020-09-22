package com.example.button.startApp_1.data

data class Cloth(
    val id : Int,
    val clothID : Int,
    val color : String,
    val season : MutableList<String>,
    val category : String,
    val dateBought : String,
    val dateLastWorn : String,
    val photo : String
)