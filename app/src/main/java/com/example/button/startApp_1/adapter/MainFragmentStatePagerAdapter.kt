package com.example.button.startApp_1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.button.startApp_1.fragment.CalendarFragment
import com.example.button.startApp_1.fragment.MyclosetFragment
import com.example.button.startApp_1.fragment.RecommendFragment

class MainFragmentStatePagerAdapter(fm: FragmentManager, val fragmentCount : Int) : FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        when(position){
            0->return RecommendFragment()
            1->return MyclosetFragment()
            else->return CalendarFragment()
        }
    }

    override fun getCount(): Int = fragmentCount
}