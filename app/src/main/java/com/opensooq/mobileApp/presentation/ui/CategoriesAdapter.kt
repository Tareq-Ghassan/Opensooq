package com.opensooq.mobileApp.presentation.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import com.squareup.picasso.Picasso

class CategoriesAdapter(private val onItemClicked: (MainCategory) -> Unit) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private  val categories: MutableList<MainCategory> = mutableListOf()
    private  val subCategories: MutableList<SubCategory> = mutableListOf()
    private var isShowingCategories = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return  CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return if (isShowingCategories) {
            categories.size
        } else {
            subCategories.size
        }
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        if (isShowingCategories) {
            val category = categories[position]
            holder.categoryName.text = category.labelEn
            Picasso.get()
                .load(category.icon)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.categoryIcon)

            holder.itemView.setOnClickListener {
                onItemClicked(category)
            }
        } else {
            val subCategory = subCategories[position]
            val category = categories.find { category ->
                subCategory.parentId == category.id
            }
            holder.categoryName.text = subCategory.labelEn
            Picasso.get()
                .load(subCategory.icon)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.categoryIcon)

            holder.itemView.setOnClickListener {
                category?.let {
                    onItemClicked(it)
                } ?: run {
                    Log.e("CategoriesAdapter", "Parent category not found for SubCategory: ${subCategory.labelEn}")
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categories: MutableList<MainCategory>){
        this.categories.clear()
        this.categories.addAll(categories)
        isShowingCategories = true
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSubCategories(subCategories: MutableList<SubCategory>){
        this.subCategories.clear()
        this.subCategories.addAll(subCategories)
        isShowingCategories = false
        notifyDataSetChanged()
    }

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)

    }
}