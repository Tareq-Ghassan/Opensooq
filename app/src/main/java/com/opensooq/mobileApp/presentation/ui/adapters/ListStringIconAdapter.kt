package com.opensooq.mobileApp.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Options
import com.squareup.picasso.Picasso

class ListStringIconAdapter(
    private val items: List<Options>,
    private val onSelectionChanged: (Set<String>) -> Unit
) : RecyclerView.Adapter<ListStringIconAdapter.ItemViewHolder>() {

    // Track selected item IDs
    private val selectedIds = mutableSetOf<String>()

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.circle_image)
        private val textView: TextView = itemView.findViewById(R.id.circle_text)

        fun bind(item: Options, isSelected: Boolean) {
            // Load the image or show the text based on availability
            if (!item.optionImg.isNullOrEmpty()) {
                imageView.visibility = View.VISIBLE
                textView.visibility = View.GONE
                Picasso.get()
                    .load(item.optionImg)
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_error)
                    .fit()
                    .centerCrop()
                    .into(imageView)
            } else {
                imageView.visibility = View.GONE
                textView.visibility = View.VISIBLE
                textView.text = item.labelEn
            }

            // Update background based on selection
            if (isSelected) {
                itemView.setBackgroundResource(R.drawable.circle_selected_background)
            } else {
                itemView.setBackgroundResource(R.drawable.circle_background)
            }

            // Handle item click
            itemView.setOnClickListener {
                if (selectedIds.contains(item.labelEn)) {
                    selectedIds.remove(item.labelEn)
                } else {
                    selectedIds.add(item.labelEn)
                }
                notifyItemChanged(bindingAdapterPosition) // Update only the clicked item
                onSelectionChanged(selectedIds)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.circle_item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val isSelected = selectedIds.contains(item.id)
        holder.bind(item, isSelected)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelection(selectedIds: Set<String>) {
        this.selectedIds.clear()
        this.selectedIds.addAll(selectedIds)
        notifyDataSetChanged() // Update the entire list
    }

    fun getSelectedIds(): Set<String> = selectedIds
}
