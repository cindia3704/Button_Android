package com.example.button.startApp_1.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.fragment.FriendFragment


class FriendAdatper(val fragment : FriendFragment) :RecyclerView.Adapter<FriendAdatper.ViewHolder>() {


    var friendItems = mutableListOf<User>()

    inner class ViewHolder(view : View)  : RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return friendItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
