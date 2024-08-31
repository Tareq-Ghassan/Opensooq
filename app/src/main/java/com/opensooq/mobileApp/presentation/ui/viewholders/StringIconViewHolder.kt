package com.opensooq.mobileApp.presentation.ui.viewholders

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.presentation.ui.adapters.ListStringIconAdapter
import com.opensooq.mobileApp.presentation.ui.adapters.MultiSelectAdapter

class StringIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val label: TextView = itemView.findViewById(R.id.item_title)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.horizontal_recycler_view)
    private val selectedItemsContainer: LinearLayout = itemView.findViewById(R.id.selected_items_container)
    private val selectedItemsText: TextView = itemView.findViewById(R.id.selected_items)

    // Store selected options here
    private val selectedOptions = mutableListOf<Options>()

    fun bind(field: Fields, labelText: String, options: List<Options>, customText: String) {
        label.text = labelText

        // Initialize the RecyclerView to display the selected items horizontally
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ListStringIconAdapter(options) { selectedIds ->
            updateSelectedItemsText(selectedIds)
        }
        recyclerView.adapter = adapter

        // Set the selected items text
        selectedItemsText.text = customText

        // Handle the click to show the multi-selection dialog
        selectedItemsContainer.setOnClickListener {
            showMultiSelectDialog(itemView.context, options, adapter)
        }
    }

    private fun updateSelectedItemsText(selectedIds: Set<String>) {
        selectedItemsText.text = selectedIds.joinToString(", ")
    }

    private fun showMultiSelectDialog(context: Context, options: List<Options>, listAdapter: ListStringIconAdapter) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.list_string_icon_dialog)

        val searchView: SearchView = dialog.findViewById(R.id.search_view)
        val recyclerView: RecyclerView = dialog.findViewById(R.id.recycler_view)
        val cancelButton: Button = dialog.findViewById(R.id.btn_cancel)
        val applyButton: Button = dialog.findViewById(R.id.btn_apply)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Map selectedIds to corresponding Options objects
        val selectedOptions = listAdapter.getSelectedIds().mapNotNull { id -> options.find { it.id == id } }.toMutableList()

        val adapter = MultiSelectAdapter(options, selectedOptions)
        recyclerView.adapter = adapter

        // Handle search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredOptions = options.filter { it.labelEn.contains(newText ?: "", true) }
                adapter.updateList(filteredOptions)
                return true
            }
        })

        // Handle apply button click
        applyButton.setOnClickListener {
            val selectedIds = adapter.getSelectedOptions().map { it.labelEn }.toSet()
            updateSelectedItemsText(selectedIds)

            // Update the ListStringIconAdapter with the selected options
            listAdapter.updateSelection(selectedIds)

            dialog.dismiss()
        }

        // Handle cancel button click
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}
