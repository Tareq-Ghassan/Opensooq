package com.opensooq.mobileApp.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModel
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModelFactory

class SubCategoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private val viewModel: CategoriesViewModel by viewModels {
        val realmInstance = RealmDatabase.getInstance()
        val repository = CategoriesRepository(realmInstance, this)
        CategoriesViewModelFactory(repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sub_category)
        setupActionBar()

        val categoryId = intent.getIntExtra("CATEGORY_ID",0)

        displaySubCategories(categoryId)

    }

    private fun displaySubCategories(categoryId: Int) {
        categoryId.let { id ->
            recyclerView = findViewById(R.id.recyclerView)
            categoriesAdapter = CategoriesAdapter { subCategory ->
                // Handle subcategory click if needed
            }

            recyclerView.adapter = categoriesAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)

            viewModel.getSubCategories(id)

            viewModel.subCategoriesLiveData.observe(this) { subCategories ->
                subCategories?.let {
                    Log.d("SubCategoryActivity", "Subcategories loaded: ${it.size}")
                    categoriesAdapter.setSubCategories(it)
                }?: Log.e("SubCategoryActivity", "No subcategories found")
            }
        }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the Up button (Back button)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = getString(R.string.Categories)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_common, menu)
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