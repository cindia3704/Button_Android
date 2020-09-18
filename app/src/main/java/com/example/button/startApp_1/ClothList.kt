package com.example.button.startApp_1
import com.example.button.startApp_1.Clothes
//옷들을 넣을 객체 -- 옷의 list
class ClothList() {
    val list=ArrayList<Clothes>()

    fun addClothes(cloth:Clothes){
        list.add(cloth)
    }
}