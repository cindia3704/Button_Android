package com.example.button.startApp_1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.button.R
import kotlinx.android.synthetic.main.fragment_mycloset.*

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
//        val adapter=clothAdapter(clothLists, LayoutInflater.from(activity))
//
//        recyclerView_category!!.adapter=adapter
//        recyclerView_category!!.layoutManager=GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)
//        val categoryLists=ArrayList<String>()
//        categoryLists.add("상의")
//        categoryLists.add("하의")
//        categoryLists.add("아우터")
//        val adapter = categoryAdapter(categoryLists, LayoutInflater.from(activity))
//        recyclerView_category_name!!.adapter=adapter
//        recyclerView_category_name!!.layoutManager=LinearLayoutManager(activity)
        val clothLists_top=ArrayList<Clothes>()
        // 임의로 10개의 상의 만듬
        for(i in 0 until 10){
            clothLists_top.add(Clothes())
        }
        val adapter=clothAdapter(clothLists_top, LayoutInflater.from(activity))
        recyclerView_category_top!!.adapter=adapter
        recyclerView_category_top!!.layoutManager=GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)

        val clothLists_bottom=ArrayList<Clothes>()
        // 임의로 10개의 하의 만듬
        for(i in 0 until 5){
            clothLists_bottom.add(Clothes())
        }
        val adapter2=clothAdapter(clothLists_bottom, LayoutInflater.from(activity))
        recyclerView_category_bottom!!.adapter=adapter2
        recyclerView_category_bottom!!.layoutManager=GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)

        val clothLists_outer=ArrayList<Clothes>()
        // 임의로 20개의 아우터 만듬
        for(i in 0 until 20){
            clothLists_outer.add(Clothes())
        }
        val adapter3=clothAdapter(clothLists_outer, LayoutInflater.from(activity))
        recyclerView_category_outer!!.adapter=adapter3
        recyclerView_category_outer!!.layoutManager=GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)

        val clothLists_onepiece=ArrayList<Clothes>()
        // 임의로 1개의 상의 만듬
        for(i in 0 until 1){
            clothLists_onepiece.add(Clothes())
        }
        val adapter4=clothAdapter(clothLists_onepiece, LayoutInflater.from(activity))
        recyclerView_category_onepiece!!.adapter=adapter4
        recyclerView_category_onepiece!!.layoutManager=GridLayoutManager(activity,2,RecyclerView.HORIZONTAL,false)
    }
}

class clothAdapter(
    val itemList:ArrayList<Clothes>,
    val inflater: LayoutInflater
): RecyclerView.Adapter<clothAdapter.ViewHolder>(){
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val clothImage:ImageView
        //val clothText: TextView

        init {
            clothImage=itemView.findViewById(R.id.cloth_image)
            //clothText=itemView.findViewById(R.id.cloth_text)
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
       // holder.clothText.setText(itemList.get(position).name)
//        holder.clothImage.setImageURI(itemList.get(position).image)
    }
}
//
//class categoryAdapter(
//    val categoryList:ArrayList<String>,
//    val inflater: LayoutInflater
//):RecyclerView.Adapter<categoryAdapter.ViewHolder>(){
//    class ViewHolder(categoryView:View):RecyclerView.ViewHolder(categoryView){
//        val categoryText:TextView
//
//        init{
//            categoryText=categoryView.findViewById(R.id.category)
//        }
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): categoryAdapter.ViewHolder {
//        val view=inflater.inflate(R.layout.mycloset_category,parent,false)
//        return categoryAdapter.ViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return categoryList.size
//    }
//
//    override fun onBindViewHolder(holder: categoryAdapter.ViewHolder, position: Int) {
//        holder.categoryText.setText(categoryList.get(position))
//    }
//}