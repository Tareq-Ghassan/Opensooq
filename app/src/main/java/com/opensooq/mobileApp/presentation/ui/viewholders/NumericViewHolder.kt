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
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.presentation.ui.adapters.DialogNumericAdapter

/**
 * ViewHolder for handling numeric field selections in the filter screen.
 * This ViewHolder manages the logic for displaying a dialog where users
 * can select values from a list, with support for both "From" and "To" tabs.
 */
class NumericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val label: TextView = itemView.findViewById(R.id.list_Numeric_label)
    private val fromRowContainer: LinearLayout = itemView.findViewById(R.id.from_row_container)
    private val toRowContainer: LinearLayout = itemView.findViewById(R.id.to_row_container)
    private val fromValueTextView: TextView = itemView.findViewById(R.id.from_value)
    private val toValueTextView: TextView = itemView.findViewById(R.id.to_value)

    /**
     * Binds the ViewHolder with data and sets up click listeners.
     *
     * @param labelText The label text to display.
     * @param options The list of options available for selection.
     */
    fun bind(labelText: String, options: List<Options>) {
        label.text = labelText
        fromRowContainer.setOnClickListener {
            showRangeSelectDialog(itemView.context, options, true)
        }

        toRowContainer.setOnClickListener {
            showRangeSelectDialog(itemView.context, options, false)
        }
    }

    /**
     * Displays the range selection dialog with "From" and "To" tabs.
     *
     * @param context The context used to create the dialog.
     * @param options The list of options available for selection.
     * @param isFromTab Indicates whether the "From" tab should be selected initially.
     */
    private fun showRangeSelectDialog(context: Context, options: List<Options>, isFromTab: Boolean) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.list_numeric_dialog)

        // Set the dialog layout parameters to match parent width
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val tabLayout: TabLayout = dialog.findViewById(R.id.tab_layout)
        val searchView: SearchView = dialog.findViewById(R.id.search_view)
        val recyclerView: RecyclerView = dialog.findViewById(R.id.recycler_view)
        val cancelButton: Button = dialog.findViewById(R.id.btn_cancel)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Add the "Any" option at the top of the list
        val optionsWithAny = listOf(Options().apply { labelEn = "Any" }) + options

        // Initialize the adapters for the "From" and "To" tabs
        val fromAdapter = createAdapter(optionsWithAny) { selectedOption ->
            fromValueTextView.text = selectedOption.labelEn
            tabLayout.getTabAt(1)?.select() // Switch to "To" tab after selection in "From" tab
        }

        val toAdapter = createAdapter(optionsWithAny) { selectedOption ->
            toValueTextView.text = selectedOption.labelEn
            dialog.dismiss() // Close dialog after selection in "To" tab
        }

        // Set the initial adapter and tab
        recyclerView.adapter = if (isFromTab) fromAdapter else toAdapter
        tabLayout.addTab(tabLayout.newTab().setText("From"))
        tabLayout.addTab(tabLayout.newTab().setText("To"))
        tabLayout.getTabAt(if (isFromTab) 0 else 1)?.select()

        // Handle tab selection and switch adapters accordingly
        handleTabSelection(tabLayout, recyclerView, fromAdapter, toAdapter, searchView)

        // Implement search functionality to filter options
        implementSearchFunctionality(searchView, tabLayout, optionsWithAny, recyclerView, fromAdapter, toAdapter, dialog)

        // Handle the cancel button click
        cancelButton.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    /**
     * Creates the adapter for the RecyclerView in the dialog.
     *
     * @param options The list of options to display.
     * @param onItemSelected The action to perform when an option is selected.
     * @return The created DialogNumericAdapter.
     */
    private fun createAdapter(options: List<Options>, onItemSelected: (Options) -> Unit): DialogNumericAdapter {
        return DialogNumericAdapter(options, onItemSelected)
    }

    /**
     * Handles the tab selection and adapter switching logic.
     *
     * @param tabLayout The TabLayout used for switching between tabs.
     * @param recyclerView The RecyclerView to set the adapter on.
     * @param fromAdapter The adapter for the "From" tab.
     * @param toAdapter The adapter for the "To" tab.
     * @param searchView The SearchView used for filtering options.
     */
    private fun handleTabSelection(
        tabLayout: TabLayout,
        recyclerView: RecyclerView,
        fromAdapter: DialogNumericAdapter,
        toAdapter: DialogNumericAdapter,
        searchView: SearchView
    ) {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                recyclerView.adapter = if (tab?.position == 0) fromAdapter else toAdapter
                searchView.setQuery("", false) // Clear search on tab switch
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * Implements the search functionality to filter the options based on user input.
     *
     * @param searchView The SearchView used for filtering options.
     * @param tabLayout The TabLayout used for switching between tabs.
     * @param optionsWithAny The list of options including the "Any" option.
     * @param recyclerView The RecyclerView to display the filtered options.
     * @param fromAdapter The adapter for the "From" tab.
     * @param toAdapter The adapter for the "To" tab.
     * @param dialog The dialog to manage visibility.
     */
    private fun implementSearchFunctionality(
        searchView: SearchView,
        tabLayout: TabLayout,
        optionsWithAny: List<Options>,
        recyclerView: RecyclerView,
        fromAdapter: DialogNumericAdapter,
        toAdapter: DialogNumericAdapter,
        dialog: Dialog
    ) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredOptions = optionsWithAny.filter {
                    it.labelEn.contains(newText ?: "", ignoreCase = true)
                }
                recyclerView.adapter = if (tabLayout.selectedTabPosition == 0) {
                    createAdapter(filteredOptions) { selectedOption ->
                        fromValueTextView.text = selectedOption.labelEn
                        tabLayout.getTabAt(1)?.select()
                    }
                } else {
                    createAdapter(filteredOptions) { selectedOption ->
                        toValueTextView.text = selectedOption.labelEn
                        dialog.dismiss()
                    }
                }
                return true
            }
        })
    }
}
