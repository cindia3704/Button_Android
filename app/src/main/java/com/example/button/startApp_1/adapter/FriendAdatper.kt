package com.example.button.startApp_1.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.example.button.startApp_1.activity.AddCoordiActivity
import com.example.button.startApp_1.activity.CoordiDetailActivity
import com.example.button.startApp_1.activity.CoordiListActivity
import com.example.button.startApp_1.data.Friend
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.fragment.FriendFragment


class FriendAdatper(val fragment : FriendFragment) :RecyclerView.Adapter<FriendAdatper.ViewHolder>() {


    var friendItems = mutableListOf<Friend>()

    inner class ViewHolder(view : View)  : RecyclerView.ViewHolder(view){


        var friendName : TextView = view.findViewById(R.id.friendName)
        var coordi : ImageView = view.findViewById(R.id.cordiFriend)
        var coordiList : ImageView = view.findViewById(R.id.cordiListFirend)


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.friend_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.run {

            friendName.setText("${friendItems[position].friendName}")
            coordi.setOnClickListener {
                var intent = Intent(fragment.context,CoordiDetailActivity::class.java)
                intent.putExtra("KEY_FRIEND_ID",friendItems[position].frienduser)
                intent.putExtra("KEY_USER_ID",friendItems[position].user)
                fragment.startActivity(intent)
            }

            coordiList.setOnClickListener {

                var intent = Intent(fragment.context,CoordiListActivity::class.java)

                intent.putExtra("KEY_USER_ID",friendItems[position].frienduser)
                fragment.startActivity(intent)
            }
        }
    }
}
