package com.opensooq.mobileApp.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.presentation.ui.viewholders.BooleanViewHolder
import com.opensooq.mobileApp.presentation.ui.viewholders.DefaultViewHolder
import com.opensooq.mobileApp.presentation.ui.viewholders.NumericViewHolder
import com.opensooq.mobileApp.presentation.ui.viewholders.StringIconViewHolder

/**
 * Adapter for displaying various types of filters in a RecyclerView.
 * It supports multiple view types, such as numeric, string with icons, and boolean options.
 *
 * @param items A list of pairs, where each pair contains a Fields object and its corresponding FieldLabel.
 * @param nestedItems A list of lists, where each inner list contains the Options associated with the corresponding item in the items list.
 */
class FilterAdapter(
    private val items: List<Pair<Fields, FieldLabel>>,
    private val nestedItems: List<List<Options>>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_NUMERIC = 1
        private const val VIEW_TYPE_STRING_ICON = 2
        private const val VIEW_TYPE_STRING = 3
        private const val VIEW_TYPE_STRING_BOOLEAN = 4
        private const val VIEW_TYPE_DEFAULT = 5
    }

    /**
     * Determines the view type for a given position in the RecyclerView.
     *
     * @param position The position of the item in the RecyclerView.
     * @return An integer representing the view type.
     */
    override fun getItemViewType(position: Int): Int {
        return when (items[position].first.dataType) {
            "list_numeric" -> VIEW_TYPE_NUMERIC
            "list_string_icon" -> VIEW_TYPE_STRING_ICON
            "list_string" -> VIEW_TYPE_STRING
            "list_string_boolean" -> VIEW_TYPE_STRING_BOOLEAN
            else -> VIEW_TYPE_DEFAULT
        }
    }

    /**
     * Creates a new ViewHolder based on the view type.
     *
     * @param parent The parent ViewGroup into which the new view will be added after it is bound to an adapter position.
     * @param viewType The view type of the new view.
     * @return A new RecyclerView.ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NUMERIC -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_numeric_view, parent, false)
                NumericViewHolder(view)
            }
            VIEW_TYPE_STRING_ICON -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_string_icon_view, parent, false)
                StringIconViewHolder(view)
            }
            VIEW_TYPE_STRING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_string_icon_view, parent, false)
                StringIconViewHolder(view)
            }
            VIEW_TYPE_STRING_BOOLEAN -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_string_boolean_view, parent, false)
                BooleanViewHolder(view)
            }
            VIEW_TYPE_DEFAULT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_view, parent, false)
                DefaultViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_view, parent, false)
                DefaultViewHolder(view)
            }
        }
    }

    /**
     * Binds the data to the ViewHolder for the specified position.
     *
     * @param holder The ViewHolder that should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is NumericViewHolder -> holder.bind(item.second.labelEn, nestedItems[position])
            is StringIconViewHolder -> holder.bind(item.first, item.second.labelEn, nestedItems[position])
            is BooleanViewHolder -> holder.bind(item.second.labelEn, nestedItems[position])
            is DefaultViewHolder -> holder.bind()
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = items.size
}
