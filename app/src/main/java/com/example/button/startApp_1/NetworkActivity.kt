package com.example.button.startApp_1

import android.animation.LayoutTransition
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_network.*
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkActivity : AppCompatActivity(){
    fun Oncreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        NetworkTask(
            rvrview,
            LayoutInflater.from(this@NetworkActivity)
        ).execute()
    }
}

class NetworkTask(
    val recyclerView: RecyclerView,
    val inflater : LayoutInflater
):AsyncTask<Any?, Any?, Array<User>>(){
    override fun onPostExecute(result: Array<User>?) {
        val adapter = UserAdapter(result!!, inflater)
        recyclerView.adapter = adapter
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg params: Any?): Array<User> {
        val urlString :String= "http://18.191.146.76:9999/user"
        val url :URL = URL(urlString)
        val connection : HttpURLConnection = url.openConnection() as HttpURLConnection

        connection.requestMethod="GET"
        connection.setRequestProperty("Content-Type","application/json")

        var buffer =""
        if(connection.responseCode==HttpURLConnection.HTTP_OK){
            val reader = BufferedReader(
                InputStreamReader(
                    connection.inputStream,
                    "UTF-8"
                )
            )
            buffer=reader.readLine()
        }

        val temp = buffer.get(7)
        Log.d("conn","inputstream"+temp)

        val data = Gson().fromJson(buffer, Array<User>::class.java)
        val nick = data[0].userNickName
        Log.d("hey","nick:"+nick)
        return data
    }
}

class UserAdapter(
    val UserList : Array<User>,
    val inflater : LayoutInflater
): RecyclerView.Adapter<UserAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idd: TextView
        val passsword: TextView
        val useremail: TextView
        val usernickName: TextView
        val usergender: TextView
        val ddateregistered: TextView
        val confirmedemail: TextView

        init{
            idd = itemView.findViewById(R.id.user_id)
            passsword=itemView.findViewById(R.id.user_pw)
            useremail=itemView.findViewById(R.id.user_email)
            usernickName=itemView.findViewById(R.id.user_nickname)
            usergender=itemView.findViewById(R.id.user_gender)
            ddateregistered=itemView.findViewById(R.id.date_register)
            confirmedemail=itemView.findViewById(R.id.user_confirm)
        }
    }

    override fun getItemCount(): Int {
       return UserList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.test_network, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.idd.setText(UserList.get(position).id.toString()?:"")
        holder.passsword.setText(UserList.get(position).password?:"")
        holder.useremail.setText(UserList.get(position).userEmail?:"")
        holder.usernickName.setText(UserList.get(position).userNickName?:"")
        holder.usergender.setText(UserList.get(position).userGender?:"")
        holder.ddateregistered.setText(UserList.get(position).dateRegistered?:"")
        holder.confirmedemail.setText(UserList.get(position).confirmedEmail.toString()?:"")
    }

}