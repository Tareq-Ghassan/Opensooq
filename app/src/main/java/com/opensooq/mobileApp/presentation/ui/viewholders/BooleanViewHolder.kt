package com.opensooq.mobileApp.presentation.ui.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R

class BooleanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val label: TextView = itemView.findViewById(R.id.list_boolean_label)
//        private val toggleButton: ToggleButton = itemView.findViewById(R.id.boolean_toggle)

    fun bind(labelText: String) {
        label.text = labelText
        // You can set the toggle button state based on your data or leave it for the user to toggle
    }
}