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

        fun bind(field: Fields, labelText: String, options: List<Options>, customText: String) {
            // Set the title label
            label.text = labelText

            // Set up the RecyclerView with horizontal layout
            recyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.adapter = ListStringIconAdapter(options) // Pass the list of Options

            // Handle the click to show the multi-selection dialog
            selectedItemsContainer.setOnClickListener {
                showMultiSelectDialog(itemView.context, options)
            }
        }

        private fun showMultiSelectDialog(context: Context, options: List<Options>) {
            val dialog = Dialog(context)
            dialog.setContentView(R.layout.list_string_icon_dialog)

            val searchView: SearchView = dialog.findViewById(R.id.search_view)
            val recyclerView: RecyclerView = dialog.findViewById(R.id.recycler_view)
            val cancelButton: Button = dialog.findViewById(R.id.btn_cancel)
            val applyButton: Button = dialog.findViewById(R.id.btn_apply)

            // Set up RecyclerView with checkable items
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = MultiSelectAdapter(options)
            recyclerView.adapter = adapter

            // Search functionality
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filteredOptions = options.filter {
                        it.labelEn.contains(newText ?: "", true)
                    }
                    adapter.updateList(filteredOptions)
                    return true
                }
            })

            // Handle apply button click
            applyButton.setOnClickListener {
                val selectedOptions = adapter.getSelectedOptions()
                // Handle the selected options (e.g., update UI or pass the result)
                dialog.dismiss()
            }

            // Handle cancel button click
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
}