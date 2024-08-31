package com.opensooq.mobileApp.presentation.ui.viewholders

import android.annotation.SuppressLint
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

/**
 * ViewHolder class for displaying string and icon lists with multi-selection capability.
 *
 * @param itemView The view representing an individual item in the RecyclerView.
 */
class StringIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val label: TextView = itemView.findViewById(R.id.item_title)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.horizontal_recycler_view)
    private val selectedItemsContainer: LinearLayout = itemView.findViewById(R.id.selected_items_container)
    private val selectedItemsText: TextView = itemView.findViewById(R.id.selected_items)

    // List to store selected options
    private val selectedOptions = mutableListOf<Options>()

    /**
     * Binds the provided field and options to the ViewHolder, initializing the UI components.
     *
     * @param field The field associated with this item.
     * @param labelText The text to display as the label for this item.
     * @param options The list of options available for selection.
     */
    fun bind(field: Fields, labelText: String, options: List<Options>) {
        label.text = labelText

        // Add the "Any" option at the top of the list
        val optionsWithAny = listOf(Options().apply { labelEn = "Any" }) + options

        // Set up the RecyclerView to display the selected items horizontally
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ListStringIconAdapter(optionsWithAny) { selectedIds ->
            updateSelectedItemsText(selectedIds)
        }
        recyclerView.adapter = adapter

        // Set the initial selected items text
        selectedItemsText.text = labelText

        // Show the multi-selection dialog when the container is clicked
        selectedItemsContainer.setOnClickListener {
            showMultiSelectDialog(itemView.context, optionsWithAny, adapter,labelText)
        }
    }

    /**
     * Updates the selected items text based on the selected IDs.
     *
     * @param selectedIds A set of selected option IDs.
     */
    private fun updateSelectedItemsText(selectedIds: Set<String>) {
        selectedItemsText.text = selectedIds.joinToString(", ")
    }

    /**
     * Displays a multi-selection dialog allowing the user to select options.
     *
     * @param context The context in which the dialog should be displayed.
     * @param options The list of options available for selection.
     * @param listAdapter The adapter managing the horizontal RecyclerView.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun showMultiSelectDialog(context: Context, options: List<Options>, listAdapter: ListStringIconAdapter,labelText:String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.list_string_icon_dialog)

        val searchView: SearchView = dialog.findViewById(R.id.search_view)
        val recyclerView: RecyclerView = dialog.findViewById(R.id.recycler_view)
        val cancelButton: Button = dialog.findViewById(R.id.btn_cancel)
        val resetButton: Button = dialog.findViewById(R.id.btn_reset)
        val applyButton: Button = dialog.findViewById(R.id.btn_apply)
        val title: TextView = dialog.findViewById(R.id.info_text)

        title.text= labelText

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Map selectedIds to corresponding Options objects
        val selectedOptions = listAdapter.getSelectedIds().mapNotNull { labelEn -> options.find { it.labelEn == labelEn } }.toMutableList()

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
            selectedOptions.addAll(adapter.getSelectedOptions())

            val selectedIds = selectedOptions.map { it.labelEn }.toSet()
            updateSelectedItemsText(selectedIds)

            // Update the ListStringIconAdapter with the selected options
            listAdapter.updateSelection(selectedIds)

            dialog.dismiss()
        }

        // Handle cancel button click
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        // Handle cancel button click
        resetButton.setOnClickListener {
            selectedOptions.clear()
            val selectedIds = selectedOptions.map { it.labelEn }.toSet()

            // Update the ListStringIconAdapter with the selected options
            listAdapter.updateSelection(selectedIds)
            adapter.notifyDataSetChanged()

            listAdapter.updateSelection(emptySet())
            selectedItemsText.text = label.text
        }

        dialog.show()
    }

}
