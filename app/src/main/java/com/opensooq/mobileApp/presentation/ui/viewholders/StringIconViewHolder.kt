package com.opensooq.mobileApp.presentation.ui.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.presentation.ui.adapters.ListStringIconAdapter

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