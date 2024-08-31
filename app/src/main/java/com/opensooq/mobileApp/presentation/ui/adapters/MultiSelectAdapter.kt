package com.opensooq.mobileApp.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Options

/**
 * Adapter for displaying a list of selectable options with checkboxes in a RecyclerView.
 * Allows multiple selections and updates the selected options accordingly.
 *
 * @property options The list of options to display.
 * @property selectedOptions The list of currently selected options.
 */
class MultiSelectAdapter(
    private var options: List<Options>,
    private val selectedOptions: MutableList<Options>
) : RecyclerView.Adapter<MultiSelectAdapter.MultiSelectViewHolder>() {

    /**
     * Creates a new ViewHolder to represent an item.
     *
     * @param parent The parent ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new MultiSelectViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiSelectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_select_item, parent, false)
        return MultiSelectViewHolder(view)
    }

    /**
     * Binds the data at the specified position to the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item within the data set.
     */
    override fun onBindViewHolder(holder: MultiSelectViewHolder, position: Int) {
        val option = options[position]
        val isSelected = selectedOptions.any { it.labelEn == option.labelEn }
        holder.bind(option, isSelected)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items.
     */
    override fun getItemCount(): Int = options.size

    /**
     * Updates the list of options and refreshes the displayed data.
     *
     * @param filteredOptions The new list of options to display.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(filteredOptions: List<Options>) {
        options = filteredOptions
        notifyDataSetChanged()
    }

    /**
     * Returns the list of currently selected options.
     *
     * @return The list of selected options.
     */
    fun getSelectedOptions(): List<Options> = selectedOptions

    /**
     * ViewHolder class for individual items in the RecyclerView.
     *
     * @param itemView The view representing an individual item.
     */
    inner class MultiSelectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text_view)
        private val checkBox: CheckBox = itemView.findViewById(R.id.check_box)

        /**
         * Binds data to the item view, including setting the text and handling selection state.
         *
         * @param option The option item to bind.
         * @param isSelected Whether the item is selected or not.
         */
        fun bind(option: Options, isSelected: Boolean) {
            textView.text = option.labelEn
            checkBox.isChecked = isSelected

            // Handle item click to toggle selection state
            itemView.setOnClickListener {
                checkBox.isChecked = !checkBox.isChecked
                if (checkBox.isChecked) {
                    if (!selectedOptions.any { it.labelEn == option.labelEn }) {
                        selectedOptions.add(option)
                    }
                } else {
                    selectedOptions.removeAll { it.labelEn == option.labelEn }
                }
            }
        }
    }
}
