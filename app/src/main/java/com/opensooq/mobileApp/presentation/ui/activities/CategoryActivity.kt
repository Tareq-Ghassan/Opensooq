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

class CategoryActivity : AppCompatActivity() {

    companion object {
        const val TYPE = "type"
        const val TYPE_CATEGORY = "category"
        const val TYPE_SUBCATEGORY = "subcategory"
        const val CATEGORY_ID = "category_id"
    }

    private val viewModel: CategoriesViewModel by viewModels {
        val realmInstance = RealmDatabase.getInstance()
        val repository = CategoriesRepository(realmInstance, this)
        CategoriesViewModelFactory(repository)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_category)
        setupActionBar()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categoriesAdapter = CategoriesAdapter { item ->
            onItemClicked(item)
        }
        recyclerView.adapter = categoriesAdapter

        val type = intent.getStringExtra(TYPE) ?: TYPE_CATEGORY
        val categoryId = intent.getIntExtra(CATEGORY_ID, 0)

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

    private fun displayCategories() {
        viewModel.categoriesLiveData.observe(this, Observer { categories ->
            categories?.let {
                categoriesAdapter.setCategories(it)
            } ?: run {
                Log.e("ListingActivity", "No categories observed")
            }
        })
    }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.category_menu, menu)
        return true
    }

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