package com.example.button.startApp_1.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.button.R
import com.example.button.startApp_1.activity.AddCoordiActivity
import com.example.button.startApp_1.activity.CoordiDetailActivity
import com.example.button.startApp_1.activity.CoordiListActivity
import com.example.button.startApp_1.data.Friend
import com.example.button.startApp_1.data.User
import com.example.button.startApp_1.fragment.FriendFragment
import com.example.button.startApp_1.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_main2.*


class FriendAdatper(val fragment : FriendFragment) :RecyclerView.Adapter<FriendAdatper.ViewHolder>() {


    var friendItems = mutableListOf<Friend>()

    inner class ViewHolder(view : View)  : RecyclerView.ViewHolder(view){

        var friendphoto:ImageView=view.findViewById(R.id.friend_image)
        var friendName : TextView = view.findViewById(R.id.friendName)
        var coordi : ImageView = view.findViewById(R.id.cordiFriend)
        var coordiDeleteFriend : ImageView = view.findViewById(R.id.cordiFriendDelete)


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
//            friendphoto.setImageURI(friendphoto)
            Glide.with(fragment)
                .load(RetrofitClient.imageBaseUrl+friendItems[position].photo)
                .placeholder(R.drawable.person__icon1)
                .apply(RequestOptions.circleCropTransform()).into(friendphoto)
            friendName.setText("${friendItems[position].friendName}")
            coordi.setOnClickListener {
                var intent = Intent(fragment.context,CoordiDetailActivity::class.java)
                intent.putExtra("KEY_FRIEND_ID",friendItems[position].frienduser)
                intent.putExtra("KEY_USER_ID",friendItems[position].user)
                fragment.startActivity(intent)
            }

            coordiDeleteFriend.setOnClickListener {

                fragment.deleteFriend(friendItems[position].frienduser)
//                var intent = Intent(fragment.context,CoordiListActivity::class.java)
//
//                intent.putExtra("KEY_USER_ID",friendItems[position].frienduser)
//                fragment.startActivity(intent)
            }
        }
    }
}
