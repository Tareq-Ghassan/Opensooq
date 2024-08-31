package com.opensooq.mobileApp.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options

class FilterAdapter(private val items: List<Pair<Fields, FieldLabel>>,private val nestedItems: List<List<Options>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_NUMERIC = 1
        private const val VIEW_TYPE_STRING_ICON = 2
        private const val VIEW_TYPE_STRING = 3
        private const val VIEW_TYPE_STRING_BOOLEAN = 4
        private const val VIEW_TYPE_DEFAULT = 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].first.dataType) {
            "list_numeric" -> VIEW_TYPE_NUMERIC
            "list_string_icon" -> VIEW_TYPE_STRING_ICON
            "list_string" -> VIEW_TYPE_STRING
            "List_string_boolean" -> VIEW_TYPE_STRING_BOOLEAN
            else -> VIEW_TYPE_DEFAULT
        }
    }

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
                    .inflate(R.layout.empty_view, parent, false) // A layout with minimal/no content
                DefaultViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_view, parent, false) // A layout with minimal/no content
                DefaultViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is NumericViewHolder -> holder.bind(item.second.labelEn)
            is StringIconViewHolder -> holder.bind(item.first, item.second.labelEn, nestedItems[position], "")
            is BooleanViewHolder ->  holder.bind(item.second.labelEn)
            is DefaultViewHolder -> holder.bind()
        }
    }

    override fun getItemCount(): Int = items.size

    class NumericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val label: TextView = itemView.findViewById(R.id.list_Numeric_label)

        fun bind(labelText: String) {
            label.text = labelText
        }
    }


    class StringIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val label: TextView = itemView.findViewById(R.id.item_title)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.horizontal_recycler_view)
        private val selectedItems: TextView = itemView.findViewById(R.id.selected_items)

        fun bind(field: Fields, labelText: String, options: List<Options>, customText: String) {
            // Set the title label
            label.text = labelText

            // Set up the RecyclerView with horizontal layout
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = ListStringIconAdapter(options) // Pass the list of Strings

            // Set the customizable text at the bottom
            selectedItems.text = customText
        }
    }

    class BooleanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val label: TextView = itemView.findViewById(R.id.list_boolean_label)
//        private val toggleButton: ToggleButton = itemView.findViewById(R.id.boolean_toggle)

        fun bind(labelText: String) {
            label.text = labelText
            // You can set the toggle button state based on your data or leave it for the user to toggle
        }
    }

    class DefaultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            // Optionally bind some default content or leave empty
        }
    }
}