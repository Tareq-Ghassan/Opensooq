package com.opensooq.mobileApp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.data.models.Options

/**
 * Adapter for displaying a list of options in a dialog, typically used for numeric selection.
 * This adapter supports click events on each item to notify the parent of the selected option.
 *
 * @param options The list of options to display.
 * @param onOptionSelected A lambda function that gets invoked when an option is selected.
 */
class DialogNumericAdapter(
    private val options: List<Options>,
    private val onOptionSelected: (Options) -> Unit
) : RecyclerView.Adapter<DialogNumericAdapter.OptionViewHolder>() {

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new OptionViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return OptionViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder to reflect the item at the given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = options.size

    /**
     * ViewHolder class that represents each option in the list.
     * It binds the data from the Options object to the TextView and handles click events.
     */
    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        /**
         * Binds the given option data to the ViewHolder.
         *
         * @param option The Options object that contains the data to be displayed.
         */
        fun bind(option: Options) {
            textView.text = option.labelEn
            itemView.setOnClickListener {
                onOptionSelected(option)
            }
        }
    }
}
