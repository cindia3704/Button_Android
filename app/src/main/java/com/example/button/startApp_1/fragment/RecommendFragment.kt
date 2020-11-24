package com.example.button.startApp_1.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.button.R
import com.example.button.startApp_1.activity.RecommendResultActivity
import com.example.button.startApp_1.data.Cloth
import com.example.button.startApp_1.data.RecommendBody
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_mycloset.*
import kotlinx.android.synthetic.main.fragment_mycloset.select_weather
import kotlinx.android.synthetic.main.fragment_recommend.*
import retrofit2.Call
import retrofit2.Response


class RecommendFragment : Fragment() {

    companion object {
        private const val MY_INT = "userId"
        fun newInstance(userId: Int): RecommendFragment {
            val frag = RecommendFragment()
            val bundle = Bundle()
            bundle.putInt(MY_INT, userId)
            frag.arguments = bundle
            return frag
        }
    }

    private var place1 = 0
    private var place2 = 0
    private var people1 = 0
    private var people2 = 0
    private var event1 = 0
    private var event2 = 0
    private var mood = 0

    var userId: Int = 5
    private val weatherList = arrayOf("여름","겨울","환절기(봄, 가을)")
    private val weatherValueList = arrayOf("SUMMER","WINTER","HWAN")
    private var selectWeatherIndex = 0
    private var weatherAdapter : ArrayAdapter<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getInt(MY_INT) ?: 0
        return inflater.inflate(R.layout.fragment_recommend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutInit()
        click()
    }


    private fun click(){
        llOfficial.setOnClickListener {
            if(!official.isChecked){
                selectMood(1)
            }else{
                unselectMood(1)
            }
        }
        llSimple.setOnClickListener {
            if(!simple.isChecked){
                selectMood(2)
            }else{
                unselectMood(2)
            }
        }
        llClean.setOnClickListener {
            if(!clean.isChecked){
                selectMood(4)
            }else{
                unselectMood(4)
            }
        }
        llFriend.setOnClickListener {
            if(!friend.isChecked){
                selectPeople(1)
            }else{
                unselectPeople(1)
            }
        }
        llCouple.setOnClickListener {
            if(!couple.isChecked){
                selectPeople(2)
            }else{
                unselectPeople(2)
            }
        }
        llCoworker.setOnClickListener {
            if(!coworker.isChecked){
                selectPeople(3)
            }else{
                unselectPeople(3)
            }
        }
        llFamily.setOnClickListener {
            if(!family.isChecked){
                selectEvent(7)
            }else{
                unselectEvent(7)
            }
        }
        llWalk.setOnClickListener {
            if(!walk.isChecked){
                selectEvent(6)
            }else{
                unselectEvent(6)
            }
        }
        llDate.setOnClickListener {
            if(!school.isChecked){
                selectEvent(5)
            }else{
                unselectEvent(5)
            }
        }
        llMeet.setOnClickListener {
            if(!meet.isChecked){
                selectEvent(4)
            }else{
                unselectEvent(4)
            }
        }
        llMerry.setOnClickListener {
            if(!merry.isChecked){
                selectEvent(3)
            }else{
                unselectEvent(3)
            }
        }
        llStudy.setOnClickListener {
            if(!study.isChecked){
                selectEvent(2)
            }else{
                unselectEvent(2)
            }
        }
        llJob.setOnClickListener {
            if(!job.isChecked){
                selectEvent(1)
            }else{
                unselectEvent(1)
            }
        }

        llSchool.setOnClickListener {
            if(!school.isChecked){
                selectPlace(1)
            }else{
                unselectPlace(1)
            }
        }

        llSea.setOnClickListener {
            if(!sea.isChecked){
                selectPlace(5)
            }else{
                unselectPlace(5)
            }
        }

        llWork.setOnClickListener {

            if(!work.isChecked){
                selectPlace(2)
            }else{
                unselectPlace(2)
            }
        }

        llCafe.setOnClickListener {

            if(!cafe.isChecked){
                selectPlace(3)
            }else{
                unselectPlace(3)
            }
        }

        llPark.setOnClickListener {

            if(!park.isChecked){
                selectPlace(4)
            }else{
                unselectPlace(4)
            }
        }
    }


    private fun unselectMood(mood : Int){
        if(this.mood == mood){
            this.mood = 0
            checkMood(mood,false)
        }else{
            return
        }
    }

    private fun checkMood(mood : Int,check : Boolean){
        when(mood){
            1 -> {
                official.isChecked = check
            }
            2 -> {
                simple.isChecked = check
            }

            4 -> {
                clean.isChecked = check
            }
        }
    }

    private fun selectMood(mood : Int){

        if(this.mood == 0){
            this.mood = mood
            checkMood(mood,true)
        }
    }


    private fun unselectPeople(people : Int){
        if(people1 == people){
            people1 = 0
            checkPeople(people,false)
        }else if(people2 == people){
            people2 = 0
            checkPeople(people,false)
        }else{
            return
        }
    }

    private fun checkPeople(people : Int,check : Boolean){
        when(people){
            1 -> {
                friend.isChecked = check
            }
            2 -> {
                couple.isChecked = check
            }
            3 -> {
                coworker.isChecked = check
            }
        }
    }

    private fun selectPeople(people : Int){

        if(people1 == 0){
            people1 = people
            checkPeople(people1,true)
        }else if(people2 == 0){
            people2 = people
            checkPeople(people2,true)
        }
    }





    private fun unselectEvent(event : Int){
        if(event1 == event){
            event1 = 0
            checkEvent(event,false)
        }else if(event2 == event){
            event2 = 0
            checkEvent(event,false)
        }else{
            return
        }
    }

    private fun checkEvent(event : Int,check : Boolean){
        when(event){
            1 -> {
                job.isChecked = check
            }
            2 -> {
                study.isChecked = check
            }
            3 -> {
                merry.isChecked = check
            }
            4 -> {
                meet.isChecked = check
            }
            5 -> {
                date.isChecked = check
            }
            6 -> {
                walk.isChecked = check
            }
            7 -> {
                family.isChecked = check
            }
        }
    }

    private fun selectEvent(event : Int){

        if(event1 == 0){
            event1 = event
            checkEvent(event1,true)
        }else if(event2 == 0){
            event2 = event
            checkEvent(event2,true)
        }
    }


    private fun unselectPlace(place : Int){
        if(place1 == place){
            place1 = 0
            checkPlace(place,false)
        }else if(place2 == place){
            place2 = 0
            checkPlace(place,false)
        }else{
            return
        }
    }

    private fun checkPlace(place : Int,check : Boolean){
        when(place){
            1 -> {
                school.isChecked = check
            }
            2 -> {
                work.isChecked = check
            }
            3 -> {
                cafe.isChecked = check
            }
            4 -> {
                park.isChecked = check
            }
            5 -> {
                sea.isChecked = check
            }
        }
    }

    private fun selectPlace(place : Int){

        if(place1 == 0){
            place1 = place
            checkPlace(place1,true)
        }else if(place2 == 0){
            place2 = place
            checkPlace(place2,true)
        }
    }

    private fun layoutInit(){
        weatherAdapter = ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item,weatherList)
        select_weather.adapter = weatherAdapter
        select_weather.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectWeatherIndex = p2
            }

        }

        ivSelect.setOnClickListener {

            if(place1 == 0 && place2 == 0 && people1 == 0 &&people2 == 0 &&event1 == 0 &&event2 == 0 &&mood == 0){

                Toast.makeText(context,"오늘의 이벤트를 최소 한개 선택해주세요.", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }


            var body = RecommendBody(
                userId,
                weatherValueList[selectWeatherIndex],
                place1,
                place2,
                people1,
                people2,
                event1,
                event2,
                mood
            )

            var intent = Intent(context,RecommendResultActivity::class.java)
            intent.putExtra("recommend",body)
            startActivity(intent)


//            startActivity()


        }
    }
}
