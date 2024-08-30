package com.opensooq.mobileApp.presentation.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.MainCategory
import com.squareup.picasso.Picasso

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private  val categories: MutableList<MainCategory> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return  CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.categoryName.text = categories[position].labelEn
        Picasso.get()
            .load(categories[position].icon)
            .placeholder(R.drawable.icon_placeholder)
            .error(R.drawable.icon_error)
            .into(holder.categoryIcon)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categories: MutableList<MainCategory>){
        this.categories.clear()
        this.categories.addAll(categories)
        notifyDataSetChanged()
    }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)

    }
}