package com.example.button.startApp_1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.button.startApp_1.fragment.*

class MainFragmentStatePagerAdapter(fm: FragmentManager, val fragmentCount : Int,val userId : Int) : FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        when(position){
            0->return RecommendFragment()
            1->return MyclosetFragment.newInstance(userId)
            2->return CoordiFragment.newInstance(userId)
            3->return FriendFragment.newInstance(userId)
            else->return CalendarFragment()
        }
    }

    override fun getCount(): Int = fragmentCount
}
