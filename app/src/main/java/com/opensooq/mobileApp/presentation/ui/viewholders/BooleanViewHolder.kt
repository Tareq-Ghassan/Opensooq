package com.opensooq.mobileApp.presentation.ui.viewholders

import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Options

class BooleanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val label: TextView = itemView.findViewById(R.id.list_boolean_label)
    private val gridLayout: GridLayout = itemView.findViewById(R.id.boolean_grid)

    fun bind(labelText: String, options: List<Options>) {
        label.text = labelText

        // Clear existing views
        gridLayout.removeAllViews()

        // Measure available width of the GridLayout
        val gridWidth = gridLayout.width - gridLayout.paddingLeft - gridLayout.paddingRight

        // List to hold the width of each ToggleButton
        val toggleWidths = mutableListOf<Int>()

        // Create and measure ToggleButtons to determine their width
        options.forEach { option ->
            val toggleButton = ToggleButton(itemView.context).apply {
                textOn = option.labelEn
                textOff = option.labelEn
                isChecked = false // Modify this based on your logic
                measure(0, 0)  // Measure the width and height
                toggleWidths.add(measuredWidth)
            }
        }

        // Calculate the number of columns dynamically
        var currentRowWidth = 0
        var currentColumnCount = 0
        var maxColumnCount = 0

        toggleWidths.forEach { width ->
            if (currentRowWidth + width > gridWidth) {
                maxColumnCount = maxColumnCount.coerceAtLeast(currentColumnCount)
                currentRowWidth = width
                currentColumnCount = 1
            } else {
                currentRowWidth += width
                currentColumnCount++
            }
        }
        maxColumnCount = maxColumnCount.coerceAtLeast(currentColumnCount)

        // Set the calculated column count to the GridLayout
        gridLayout.columnCount = maxColumnCount

        // Now add the ToggleButtons to the GridLayout
        options.forEachIndexed { _, option ->
            val toggleButton = ToggleButton(itemView.context).apply {
                textOn = option.labelEn
                textOff = option.labelEn
                isChecked = false // Modify this based on your logic
                setBackgroundResource(R.drawable.toggle_button_background)
                layoutParams = GridLayout.LayoutParams().apply {
                    width = GridLayout.LayoutParams.WRAP_CONTENT
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    setMargins(4, 4, 4, 4)
                }
            }
            gridLayout.addView(toggleButton)
        }
    }
}