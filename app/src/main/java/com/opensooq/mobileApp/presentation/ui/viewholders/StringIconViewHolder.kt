package com.opensooq.mobileApp.presentation.ui.viewholders

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.presentation.ui.adapters.DialogNumericAdapter
import com.opensooq.mobileApp.presentation.ui.adapters.ListStringIconAdapter

class StringIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val label: TextView = itemView.findViewById(R.id.item_title)
    private val recyclerView: RecyclerView = itemView.findViewById(R.id.horizontal_recycler_view)
    private val selectedItems: TextView = itemView.findViewById(R.id.selected_items)
    private val selectedItemsContainer: LinearLayout = itemView.findViewById(R.id.selected_items_container)

    fun bind(field: Fields, labelText: String, options: List<Options>, customText: String) {
        // Set the title label
        label.text = labelText
        selectedItems.text = labelText

        // Set up the RecyclerView with horizontal layout
        recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ListStringIconAdapter(options)

        // Set the customizable text at the bottom
        selectedItems.text = customText

        // Set click listener for the entire row
        selectedItemsContainer.setOnClickListener {
            // Show dialog or handle click
            showOptionsDialog(itemView.context, options)
        }
    }

    private fun showOptionsDialog(context: Context, options: List<Options>) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.list_numeric_dialog)  // You may create a separate dialog layout if needed

        // Get the dialog window and set the layout parameters
        val window = dialog.window
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val tabLayout: TabLayout = dialog.findViewById(R.id.tab_layout)
        val searchView: SearchView = dialog.findViewById(R.id.search_view)
        val recyclerView: RecyclerView = dialog.findViewById(R.id.recycler_view)
        val cancelButton: Button = dialog.findViewById(R.id.btn_cancel)

        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = DialogNumericAdapter(options) { selectedOption ->
            selectedItems.text = selectedOption.labelEn
            dialog.dismiss()
        }
        recyclerView.adapter = adapter

        tabLayout.addTab(tabLayout.newTab().setText("Options"))  // Example tab, adjust as needed

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredOptions = options.filter {
                    it.labelEn.contains(newText ?: "", true)
                }
                recyclerView.adapter = DialogNumericAdapter(filteredOptions) { selectedOption ->
                    selectedItems.text = selectedOption.labelEn
                    dialog.dismiss()
                }
                return true
            }
        })

        // Handle cancel button click
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}