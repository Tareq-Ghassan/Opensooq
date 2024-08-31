package com.opensooq.mobileApp.presentation.ui.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.presentation.ui.adapters.BooleanOptionsAdapter

/**
 * ViewHolder class responsible for displaying a set of boolean options using a RecyclerView with
 * a GridLayoutManager. Each option is represented by a ToggleButton.
 *
 * @param itemView The root view of this ViewHolder.
 */

class BooleanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // UI elements from the layout
    private val label: TextView = itemView.findViewById(R.id.list_boolean_label)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.boolean_grid)

    /**
     * Binds the data to the ViewHolder.
     *
     * @param labelText The label text to be displayed above the options.
     * @param options The list of options to be displayed as ToggleButtons.
     */
    fun bind(labelText: String, options: List<Options>) {
        label.text = labelText

        // Set up the RecyclerView with a GridLayoutManager to display items in a grid format
        val layoutManager = GridLayoutManager(itemView.context, 3)
        recyclerView.layoutManager = layoutManager

        // Create the adapter for the RecyclerView and set it
        val adapter = BooleanOptionsAdapter(options)
        recyclerView.adapter = adapter
    }
}
