package com.opensooq.mobileApp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Options

/**
 * Adapter for displaying a list of options as toggle buttons in a RecyclerView.
 *
 * This adapter is used to bind a list of [Options] to toggle buttons in a RecyclerView.
 * Each option is represented as a ToggleButton, where users can select or deselect options.
 *
 * @property options The list of options to be displayed.
 */
class BooleanOptionsAdapter(
    private val options: List<Options>
) : RecyclerView.Adapter<BooleanOptionsAdapter.OptionViewHolder>() {

    /**
     * Called when RecyclerView needs a new [OptionViewHolder] to represent an item.
     *
     * @param parent The parent view that the ViewHolder will be attached to.
     * @param viewType The view type of the new View.
     * @return A new [OptionViewHolder] that holds a View for each item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.boolean_option_item, parent, false)
        return OptionViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder The [OptionViewHolder] which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.bind(option)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    override fun getItemCount(): Int = options.size

    /**
     * ViewHolder class for displaying each option as a ToggleButton.
     */
    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val toggleButton: ToggleButton = itemView.findViewById(R.id.toggle_button)

        /**
         * Binds the [Options] data to the ToggleButton.
         *
         * @param option The [Options] data to bind to the ToggleButton.
         */
        fun bind(option: Options) {
            toggleButton.textOn = option.labelEn
            toggleButton.textOff = option.labelEn
            toggleButton.isChecked = false
        }
    }
}