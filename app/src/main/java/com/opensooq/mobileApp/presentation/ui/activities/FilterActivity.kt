package com.opensooq.mobileApp.presentation.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.repositories.FilterRepository
import com.opensooq.mobileApp.presentation.ui.adapters.FilterAdapter
import com.opensooq.mobileApp.presentation.viewmodels.FilterViewModel
import com.opensooq.mobileApp.presentation.viewmodels.FilterViewModelFactory

/**
 * FilterActivity handles the display of filters based on a selected subcategory.
 * It loads filter fields and their corresponding options from a Realm database and displays them in a RecyclerView.
 */
class FilterActivity : AppCompatActivity() {

    // Initialize ViewModel with a custom ViewModelFactory
    private val viewModel: FilterViewModel by viewModels {
        val realmInstance = RealmDatabase.getInstance()
        val repository = FilterRepository(realmInstance)
        FilterViewModelFactory(repository)
    }

    /**
     * Called when the activity is starting. This is where most initialization should go.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        // Setup RecyclerView with a LinearLayoutManager
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Retrieve the subcategory ID passed from the previous activity
        val subcategoryId = intent.getIntExtra("SUBCATEGORY_ID", 0)
        Log.e("FilterActivity", "SUBCATEGORY_ID $subcategoryId")

        // Load fields and options based on the subcategory ID
        viewModel.loadFieldsForSubcategory(subcategoryId)


        viewModel.loadFieldsForSubcategory(subcategoryId)

        // Observe LiveData from ViewModel to update UI when data changes
        viewModel.fieldsAndLabelsToDisplay.observe(this, Observer { fieldsAndLabels ->
            fieldsAndLabels?.let { fieldsAndLabelsList ->
                val nestedItems = fieldsAndLabelsList.map { pair ->
                    viewModel.getOptionsForField(pair.first.id.toString())
                }

                // Set the adapter for the RecyclerView
                val adapter = FilterAdapter(fieldsAndLabelsList, nestedItems)
                recyclerView.adapter = adapter
            }
        })

        // Setup the action bar with a title and back button
        setupActionBar()
    }

    /**
     * Sets up the action bar with a back button and a title.
     */
    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.filter)
    }

    /**
     * Called when a menu item is selected.
     * @param item The selected menu item.
     * @return Boolean indicating whether the event was handled.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Called to create the options menu.
     * @param menu The options menu in which you place your items.
     * @return Boolean indicating whether the menu was created.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return true
    }
}