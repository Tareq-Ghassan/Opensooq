package com.opensooq.mobileApp.presentation.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.presentation.ui.activities.helpers.CategoryActivityHelper

/**
 * CategoryActivity is the main activity for handling category and subcategory displays.
 * It utilizes a helper class to manage most of the business logic and UI interactions,
 * allowing the activity to maintain a clean lifecycle method implementation.
 */
class CategoryActivity : AppCompatActivity() {
    private lateinit var helper: CategoryActivityHelper

    companion object {
        const val TYPE = "type"
        const val TYPE_CATEGORY = "category"
        const val TYPE_SUBCATEGORY = "subcategory"
        const val CATEGORY_ID = "category_id"
    }

    /**
     * Initializes the activity with necessary setup and loads UI components.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display
        setContentView(R.layout.activity_category)

        helper = CategoryActivityHelper(this)
        val (type, categoryId, searchView) = helper.setupActivity()

        // Load categories or subcategories based on the intent extras
        helper.loadCategoriesAndSub(type, categoryId, lifecycleScope)

        // Set up the search functionality
        helper.performSearch(searchView, type)
    }

    /**
     * Ensures the keyboard is closed when the activity is paused to prevent leaking windows.
     */
    override fun onPause() {
        super.onPause()
        helper.clearAndCloseKeyboard()
    }

    /**
     * Updates the UI based on the current state when the activity is resumed.
     */
    override fun onResume() {
        super.onResume()
        helper.updateUIOnResume()
    }

    /**
     * Sets up the action bar with a back button and title.
     */
    fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (intent.getStringExtra(TYPE) == TYPE_CATEGORY) {
            getString(R.string.Categories)
        } else {
            getString(R.string.select_subcategory)
        }
    }

    /**
     * Handles selection of menu items.
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
     * Inflates the options menu in the action bar.
     * @param menu The menu to be inflated.
     * @return Boolean indicating whether the menu was created.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val type = intent.getStringExtra(TYPE) ?: TYPE_CATEGORY
        if (type == TYPE_CATEGORY) {
            menuInflater.inflate(R.menu.category_menu, menu)
            return true
        }else{
            return true
        }
    }
}
