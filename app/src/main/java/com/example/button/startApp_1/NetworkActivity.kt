package com.example.button.startApp_1

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_network.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        NetworkTask(
            recycler_person,
            LayoutInflater.from(this@NetworkActivity)
        ).execute()

    }
}

class NetworkTask(
    val recyclerView: RecyclerView,
    val inflater: LayoutInflater
) : AsyncTask<Any?, Any?, Array<User>>() {
    override fun onPostExecute(result: Array<User>?) {
        val adapter = PersonAdapter(result!!, inflater)
        recyclerView.adapter = adapter
        super.onPostExecute(result)
    }

    override fun doInBackground(vararg params: Any?): Array<User> {
        val urlString: String = "http://18.191.146.76:9999/user/"
        val url: URL = URL(urlString)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json")

        var buffer = ""
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(
                InputStreamReader(
                    connection.inputStream,
                    "UTF-8"
                )
            )
            buffer = reader.readLine()
        }
        val data = Gson().fromJson(buffer, Array<User>::class.java)
        return data
    }
}


class PersonAdapter(
    val personList: Array<User>,
    val inflater: LayoutInflater
) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idd : TextView
        val passwordd: TextView
        val nick: TextView
        val email: TextView
        val gender : TextView
        val date : TextView
        val ifconfirm : TextView

        init {
            idd = itemView.findViewById(R.id.user_id)
            passwordd = itemView.findViewById(R.id.user_pw)
            nick = itemView.findViewById(R.id.user_nickname)
            email = itemView.findViewById(R.id.user_email)
            gender = itemView.findViewById(R.id.user_gender)
            date = itemView.findViewById(R.id.date_register)
            ifconfirm = itemView.findViewById(R.id.user_confirm)
        }
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.test_network, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.idd.setText(personList.get(position).id.toString() ?: "")
        holder.passwordd.setText(personList.get(position).password ?:"")
        holder.nick.setText(personList.get(position).userNickName ?: "")
        holder.email.setText(personList.get(position).userEmail ?: "")
        holder.gender.setText(personList.get(position).userGender ?: "")
        holder.date.setText(personList.get(position).dateRegistered ?: "")
        holder.ifconfirm.setText(personList.get(position).confirmedEmail.toString() ?: "")
    }
}