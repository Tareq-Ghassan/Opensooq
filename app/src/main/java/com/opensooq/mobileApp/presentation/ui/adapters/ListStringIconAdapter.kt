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

/**
 * Adapter for displaying a list of options with either an image or text in a RecyclerView.
 * It allows users to select or deselect items, and the selected items' IDs are tracked.
 *
 * @property items The list of options to display.
 * @property onSelectionChanged A callback to notify when the selection changes.
 */
class ListStringIconAdapter(
    private val items: List<Options>,
    private val onSelectionChanged: (Set<String>) -> Unit
) : RecyclerView.Adapter<ListStringIconAdapter.ItemViewHolder>() {

    // Set to track the IDs of selected items
    private val selectedIds = mutableSetOf<String>()

    /**
     * ViewHolder class for individual items in the RecyclerView.
     *
     * @param itemView The view representing an individual item.
     */
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.circle_image)
        private val textView: TextView = itemView.findViewById(R.id.circle_text)

        /**
         * Binds data to the item view.
         *
         * @param item The option item to bind.
         * @param isSelected Whether the item is selected or not.
         */
        fun bind(item: Options, isSelected: Boolean) {
            // Load image if available, otherwise show text
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

            // Update the background based on selection state
            itemView.setBackgroundResource(
                if (isSelected) R.drawable.circle_selected_background
                else R.drawable.circle_background
            )

            // Handle item click to toggle selection state
            itemView.setOnClickListener {
                if (selectedIds.contains(item.labelEn)) {
                    selectedIds.remove(item.labelEn)
                } else {
                    selectedIds.add(item.labelEn)
                }
                notifyItemChanged(bindingAdapterPosition) // Update only the clicked item
                onSelectionChanged(selectedIds) // Notify about the change in selection
            }
        }
    }

    /**
     * Creates a new ViewHolder for an item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A new ItemViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.circle_item_view, parent, false)
        return ItemViewHolder(view)
    }

    /**
     * Binds the data at the specified position to the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item within the data set.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val isSelected = selectedIds.contains(item.labelEn)
        holder.bind(item, isSelected)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    override fun getItemCount(): Int = items.size

    /**
     * Updates the selection state of the items.
     *
     * @param selectedIds The set of selected item IDs.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateSelection(selectedIds: Set<String>) {
        this.selectedIds.clear()
        this.selectedIds.addAll(selectedIds)
        notifyDataSetChanged() // Update the entire list
    }

    /**
     * Returns the set of currently selected item IDs.
     *
     * @return The set of selected IDs.
     */
    fun getSelectedIds(): Set<String> = selectedIds
}