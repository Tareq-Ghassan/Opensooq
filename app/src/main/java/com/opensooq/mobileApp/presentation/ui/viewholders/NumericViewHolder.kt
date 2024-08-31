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

class NumericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val label: TextView = itemView.findViewById(R.id.list_Numeric_label)
    private val fromRowContainer: LinearLayout = itemView.findViewById(R.id.from_row_container)
    private val toRowContainer: LinearLayout = itemView.findViewById(R.id.to_row_container)
    private val fromValueTextView: TextView = itemView.findViewById(R.id.from_value)
    private val toValueTextView: TextView = itemView.findViewById(R.id.to_value)

    fun bind(labelText: String, options: List<Options>) {
        label.text = labelText

        fromRowContainer.setOnClickListener {
            showRangeSelectDialog(itemView.context, options, true)
        }

        toRowContainer.setOnClickListener {
            showRangeSelectDialog(itemView.context, options, false)
        }
    }

    private fun showRangeSelectDialog(context: Context, options: List<Options>, isFromTab: Boolean) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.list_numeric_dialog)

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

        // Define the initial adapter based on the selected tab
        val initialAdapter = DialogNumericAdapter(options) { selectedOption ->
            if (tabLayout.selectedTabPosition == 0) {
                fromValueTextView.text = selectedOption.labelEn
            } else {
                toValueTextView.text = selectedOption.labelEn
            }
            dialog.dismiss()
        }
        recyclerView.adapter = initialAdapter

        tabLayout.addTab(tabLayout.newTab().setText("From"))
        tabLayout.addTab(tabLayout.newTab().setText("To"))

        // Set initial tab
        val initialTabIndex = if (isFromTab) 0 else 1
        tabLayout.getTabAt(initialTabIndex)?.select()

        // Handle tab selection
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                recyclerView.adapter = initialAdapter
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

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
                    if (tabLayout.selectedTabPosition == 0) {
                        fromValueTextView.text = selectedOption.labelEn
                    } else {
                        toValueTextView.text = selectedOption.labelEn
                    }
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
