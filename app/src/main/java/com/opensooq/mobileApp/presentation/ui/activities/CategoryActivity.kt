package com.opensooq.mobileApp.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.models.CategoryItem
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import com.opensooq.mobileApp.presentation.ui.adapters.CategoriesAdapter
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModel
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModelFactory
import com.opensooq.mobileApp.utils.JsonUtils
import kotlinx.coroutines.launch

/**
 * CategoryActivity is responsible for displaying a list of categories or subcategories.
 * It allows navigation between categories and subcategories and handles loading of category data from JSON assets.
 */
class CategoryActivity : AppCompatActivity() {

    companion object {
        const val TYPE = "type"
        const val TYPE_CATEGORY = "category"
        const val TYPE_SUBCATEGORY = "subcategory"
        const val CATEGORY_ID = "category_id"
    }

    // ViewModel initialization using a custom ViewModelFactory
    private val viewModel: CategoriesViewModel by viewModels {
        val realmInstance = RealmDatabase.getInstance()
        val repository = CategoriesRepository(realmInstance, this)
        CategoriesViewModelFactory(repository)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display
        setContentView(R.layout.activity_category)
        setupActionBar()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categoriesAdapter = CategoriesAdapter { item ->
            onItemClicked(item) // Handle item clicks
        }
        recyclerView.adapter = categoriesAdapter

        val type = intent.getStringExtra(TYPE) ?: TYPE_CATEGORY
        val categoryId = intent.getIntExtra(CATEGORY_ID, 0)

        // Load categories or subcategories based on the intent extras
        lifecycleScope.launch {
            if (type == TYPE_CATEGORY) {
                val categoriesJson = JsonUtils.loadJsonFromAsset(this@CategoryActivity, "categoriesAndsubCategories.json") ?: return@launch
                val assignJson = JsonUtils.loadJsonFromAsset(this@CategoryActivity, "dynamic-attributes-assign-raw.json") ?: return@launch
                val attributesJson = JsonUtils.loadJsonFromAsset(this@CategoryActivity, "dynamic-attributes-and-options-raw.json") ?: return@launch
                viewModel.checkAndCacheJson(categoriesJson,assignJson,attributesJson)
                displayCategories()
            } else if (type == TYPE_SUBCATEGORY) {
                displaySubCategories(categoryId)
            }
        }
    }

    /**
     * Observes the categories from the ViewModel and updates the UI accordingly.
     */
    private fun displayCategories() {
        viewModel.categoriesLiveData.observe(this, Observer { categories ->
            categories?.let {
                categoriesAdapter.setCategories(it)
            } ?: run {
                Log.e("ListingActivity", "No categories observed")
            }
        })
    }

    /**
     * Observes the subcategories from the ViewModel and updates the UI accordingly.
     * @param categoryId The ID of the category for which to load subcategories.
     */
    private fun displaySubCategories(categoryId: Int) {
        viewModel.getSubCategories(categoryId)
        viewModel.subCategoriesLiveData.observe(this, Observer { subCategories ->
            subCategories?.let {
                categoriesAdapter.setSubCategories(it)
            } ?: run {
                Log.e("ListingActivity", "No subcategories observed")
            }
        })
    }

    /**
     * Handles clicks on category or subcategory items.
     * @param item The clicked category item.
     */
    private fun onItemClicked(item: CategoryItem) {
        when (item) {
            is CategoryItem.MainCategoryItem -> {
                val intent = Intent(this, CategoryActivity::class.java).apply {
                    putExtra(TYPE, TYPE_SUBCATEGORY)
                    putExtra(CATEGORY_ID, item.category.id)
                }
                startActivity(intent)
            }
            is CategoryItem.SubCategoryItem -> {
                val intent = Intent(this, FilterActivity::class.java).apply {
                    putExtra("SUBCATEGORY_ID", item.subCategory.id)
                }
                startActivity(intent)
            }
        }
    }

    /**
     * Sets up the action bar with a back button and title.
     */
    private fun setupActionBar() {
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
     * Inflates the options menu in the action bar.
     * @param menu The menu to be inflated.
     * @return Boolean indicating whether the menu was created.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.category_menu, menu)
        return true
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
}