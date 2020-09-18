package com.example.button.startApp_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import kotlinx.android.synthetic.main.fragment_mycloset.*
import com.example.button.startApp_1.Clothes

class MyclosetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mycloset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clothLists=ArrayList<Clothes>()
        // 임의로 5개의 옷 만듬
        for(i in 0 until 5){
            clothLists.add(Clothes(name=""+i+"번째 옷"))
        }
        val adapter=clothAdapter(clothLists, LayoutInflater.from(activity))
        recyclerView_category!!.adapter=adapter
        recyclerView_category!!.layoutManager=LinearLayoutManager(activity)
    }
}

class clothAdapter(
    val itemList:ArrayList<Clothes>,
    val inflater: LayoutInflater
): RecyclerView.Adapter<clothAdapter.ViewHolder>(){
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val clothImage:ImageView
        val clothText: TextView

        init {
             clothImage=itemView.findViewById(R.id.cloth_image)
            clothText=itemView.findViewById(R.id.cloth_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=inflater.inflate(R.layout.mycloset_clothes,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.clothText.setText(itemList.get(position).name)
//        holder.clothImage.setImageURI(itemList.get(position).image)
    }
}
