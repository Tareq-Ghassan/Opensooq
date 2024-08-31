package com.opensooq.mobileApp.presentation.ui.activities.helpers

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.models.CategoryItem
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import com.opensooq.mobileApp.presentation.ui.activities.CategoryActivity
import com.opensooq.mobileApp.presentation.ui.activities.FilterActivity
import com.opensooq.mobileApp.presentation.ui.adapters.CategoriesAdapter
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModel
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModelFactory
import com.opensooq.mobileApp.utils.JsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Provides helper functionalities to the CategoryActivity for managing user interactions,
 * data loading, and UI updates. This class helps in keeping the activity code clean and focused
 * on lifecycle management.
 */
class CategoryActivityHelper(private val activity: CategoryActivity) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter

    // ViewModel instance managed by the host activity to keep data handling separate from UI logic.
    private val viewModel: CategoriesViewModel by activity.viewModels {
        val realmInstance = RealmDatabase.getInstance()
        val repository = CategoriesRepository(realmInstance, activity)
        CategoriesViewModelFactory(repository)
    }

    /**
     * Initializes UI components and sets up data bindings and event listeners.
     * @return A Triple containing type, categoryId, and the SearchView instance.
     */
    fun setupActivity(): Triple<String, Int, SearchView> {
        activity.setupActionBar()

        val type = activity.intent.getStringExtra(CategoryActivity.TYPE) ?: CategoryActivity.TYPE_CATEGORY
        val categoryId = activity.intent.getIntExtra(CategoryActivity.CATEGORY_ID, 0)

        val searchView = activity.findViewById<SearchView>(R.id.search_view_cat)
        val textView: TextView = activity.findViewById(R.id.selected_category)

        setupTextViewAndSearchView(type, textView, searchView)

        recyclerView = activity.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        categoriesAdapter = CategoriesAdapter { item ->
            onItemClicked(item) // Handle item clicks
        }
        recyclerView.adapter = categoriesAdapter
        return Triple(type, categoryId, searchView)
    }

    /**
     * Sets up the TextView and SearchView based on the type of categories being displayed.
     * @param type The type of category or subcategory.
     * @param textView TextView to be updated.
     * @param searchView SearchView to set the query hint.
     */
    private fun setupTextViewAndSearchView(type: String, textView: TextView, searchView: SearchView) {
        if (type == CategoryActivity.TYPE_CATEGORY) {
            textView.visibility = View.GONE
            searchView.queryHint = activity.getString(R.string.search_for_category_or_subcategory)
        } else {
            val selectedCategoryName = activity.intent.getStringExtra("SELECTED_CATEGORY_NAME")
            textView.visibility = View.VISIBLE
            textView.text = selectedCategoryName
            searchView.queryHint = activity.getString(R.string.search_for_subcategory)
        }
    }

    /**
     * Loads categories or subcategories data asynchronously using coroutines.
     * @param type Specifies whether to load categories or subcategories.
     * @param categoryId The ID of the category for subcategory loading.
     * @param scope The CoroutineScope in which to launch the coroutines.
     */
    fun loadCategoriesAndSub(type: String, categoryId: Int, scope: CoroutineScope) {
        scope.launch {
            if (type == CategoryActivity.TYPE_CATEGORY) {
                val categoriesJson = JsonUtils.loadJsonFromAsset(activity, "categoriesAndsubCategories.json")
                categoriesJson?.let {
                    viewModel.checkAndCacheJson(it, "", "")
                    displayCategories()

                    // Launch parallel tasks for the other JSON files after categories are processed
                    launch(Dispatchers.IO) {
                        val assignJson = JsonUtils.loadJsonFromAsset(activity, "dynamic-attributes-assign-raw.json")
                        val attributesJson = JsonUtils.loadJsonFromAsset(activity, "dynamic-attributes-and-options-raw.json")

                        if (assignJson != null && attributesJson != null) {
                            viewModel.checkAndCacheJson(it, assignJson, attributesJson)
                        }
                    }
                }
            } else if (type == CategoryActivity.TYPE_SUBCATEGORY) {
                displaySubCategories(categoryId)
            }
        }
    }

    /**
     * Loads categories or subcategories data asynchronously using coroutines.
     * @param type Specifies whether to load categories or subcategories.
     * @param categoryId The ID of the category for subcategory loading.
     * @param scope The CoroutineScope in which to launch the coroutines.
     */
    fun performSearch(searchView: SearchView, type: String) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (type == CategoryActivity.TYPE_CATEGORY) {
                        viewModel.searchCategories(it)
                    } else if (type == CategoryActivity.TYPE_SUBCATEGORY) {
                        viewModel.searchSubCategories(it)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (type == CategoryActivity.TYPE_CATEGORY) {
                        viewModel.searchCategories(it)
                    } else if (type == CategoryActivity.TYPE_SUBCATEGORY) {
                        viewModel.searchSubCategories(it)
                    }
                }
                return true
            }
        })
    }

    /**
     * Observes and displays categories from the ViewModel.
     */
    private fun displayCategories() {
        viewModel.categoriesLiveData.observe(activity) { categories ->
            categories?.let {
                categoriesAdapter.setCategories(it)
            } ?: run {
                Log.e("ListingActivity", "No categories observed")
            }
        }

        viewModel.filteredCategoriesLiveData.observe(activity) { filteredCategories ->
            filteredCategories?.let {
                categoriesAdapter.setCategories(it)
            } ?: run {
                Log.e("ListingActivity", "No filtered categories observed")
            }
        }
    }

    /**
     * Observes and displays subcategories for a specific category ID.
     * @param categoryId The ID of the category whose subcategories need to be displayed.
     */
    private fun displaySubCategories(categoryId: Int) {
        viewModel.getSubCategories(categoryId)
        viewModel.subCategoriesLiveData.observe(activity) { subCategories ->
            subCategories?.let {
                categoriesAdapter.setSubCategories(it)
            } ?: run {
                Log.e("ListingActivity", "No subcategories observed")
            }
        }

        viewModel.filteredSubCategoriesLiveData.observe(activity) { filteredSubCategories ->
            filteredSubCategories?.let {
                categoriesAdapter.setSubCategories(it)
            } ?: run {
                Log.e("ListingActivity", "No filtered subcategories observed")
            }
        }
    }

    /**
     * Handles user interactions with category items, navigating to the appropriate activity based on the selection.
     * @param item The category item that was clicked.
     */
    private fun onItemClicked(item: CategoryItem) {
        when (item) {
            is CategoryItem.MainCategoryItem -> {
                val intent = Intent(activity, CategoryActivity::class.java).apply {
                    putExtra(CategoryActivity.TYPE, CategoryActivity.TYPE_SUBCATEGORY)
                    putExtra(CategoryActivity.CATEGORY_ID, item.category.id)
                    putExtra("SELECTED_CATEGORY_NAME", item.category.labelEn) // Pass the selected category name
                }
                activity.startActivity(intent)
            }
            is CategoryItem.SubCategoryItem -> {
                val intent = Intent(activity, FilterActivity::class.java).apply {
                    putExtra("SUBCATEGORY_ID", item.subCategory.id)
                }
                activity.startActivity(intent)
            }
        }
    }

    /**
     * Clears the search view text and hides the keyboard, ensuring clean UI transitions.
     */
    fun clearAndCloseKeyboard() {
        // Clear the SearchView text
        val searchView = activity.findViewById<SearchView>(R.id.search_view_cat)
        searchView.setQuery("", false)
        searchView.clearFocus()

        // Close the keyboard
        val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    /**
     * Updates the UI elements upon resuming the activity to ensure they reflect the current state.
     */
    fun updateUIOnResume() {
        val textView: TextView = activity.findViewById(R.id.selected_category)
        val type = activity.intent.getStringExtra(CategoryActivity.TYPE) ?: CategoryActivity.TYPE_CATEGORY

        if (type == CategoryActivity.TYPE_CATEGORY) {
            textView.visibility = View.GONE // Ensure the TextView is hidden on category screen
        } else {
            textView.visibility = View.VISIBLE // Ensure the TextView is visible on subcategory screen
        }
    }
}