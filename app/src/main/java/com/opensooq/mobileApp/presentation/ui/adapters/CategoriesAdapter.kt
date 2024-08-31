package com.opensooq.mobileApp.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.CategoryItem
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import com.squareup.picasso.Picasso

/**
 * Adapter for displaying categories and subcategories in a RecyclerView.
 *
 * @param onItemClicked A lambda function that is called when a category or subcategory item is clicked.
 */
class CategoriesAdapter(private val onItemClicked: (CategoryItem) -> Unit) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    // List to hold main categories
    private  val categories: MutableList<MainCategory> = mutableListOf()

    // List to hold subcategories
    private  val subCategories: MutableList<SubCategory> = mutableListOf()

    // Flag to determine whether to show main categories or subcategories
    private var isShowingCategories = true

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return  CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_list_item,parent,false))
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    override fun getItemCount(): Int {
        return if (isShowingCategories) {
            categories.size
        } else {
            subCategories.size
        }
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
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
                onItemClicked(CategoryItem.MainCategoryItem(category))
            }
        } else {
            val subCategory = subCategories[position]
            holder.categoryName.text = subCategory.labelEn
            Picasso.get()
                .load(subCategory.icon)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(holder.categoryIcon)

            holder.itemView.setOnClickListener {
                onItemClicked(CategoryItem.SubCategoryItem(subCategory))
            }
        }
    }

    /**
     * Updates the adapter with a new list of main categories and notifies the RecyclerView of data changes.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setCategories(categories: MutableList<MainCategory>){
        this.categories.clear()
        this.categories.addAll(categories)
        isShowingCategories = true
        notifyDataSetChanged()
    }

    /**
     * Updates the adapter with a new list of subcategories and notifies the RecyclerView of data changes.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setSubCategories(subCategories: MutableList<SubCategory>){
        this.subCategories.clear()
        this.subCategories.addAll(subCategories)
        isShowingCategories = false
        notifyDataSetChanged()
    }

    /**
     * ViewHolder class for categories and subcategories.
     */
    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)

    }
}