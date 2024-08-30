package com.opensooq.mobileApp.presentation.ui

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
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModel
import com.opensooq.mobileApp.presentation.viewmodels.CategoriesViewModelFactory
import com.opensooq.mobileApp.utils.JsonUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CategoriesActivity : AppCompatActivity() {

    private val viewModel: CategoriesViewModel by viewModels {
        val realmInstance = RealmDatabase.getInstance()
        val repository = CategoriesRepository(realmInstance, this)
        CategoriesViewModelFactory(repository)
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter : CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categories)
        setupActionBar()
        lifecycleScope.launch {
            val categoriesJson = JsonUtils.loadJsonFromAsset(this@CategoriesActivity, "categoriesAndsubCategories.json") ?: return@launch
//            val attributesJson = JsonUtils.loadJsonFromAsset(this@CategoriesActivity, "dynamic-attributes-and-options-raw.json") ?: return@launch
//            val assignJson = JsonUtils.loadJsonFromAsset(this@CategoriesActivity, "dynamic-attributes-assign-raw.json") ?: return@launch
            viewModel.checkAndCacheJson(categoriesJson)
            displayCategories()
        }

    }

    private fun displayCategories() {


        recyclerView = findViewById(R.id.recyclerView)

        categoriesAdapter = CategoriesAdapter { category ->
            onCategoryClicked(category)
        }
        recyclerView.adapter = categoriesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observe the LiveData
        viewModel.categoriesLiveData.observe(this, Observer { categories ->
            categories?.let {
                categoriesAdapter.setCategories(it)
            } ?: run {
                Log.e("CategoriesActivity", "No categories observed")
            }
        })
    }

    private fun onCategoryClicked(category: MainCategory) {
        val intent = Intent(this, SubCategoryActivity::class.java)
        intent.putExtra("CATEGORY_ID", category.id)
        intent.putExtra("CATEGORY_NAME", category.labelEn)
        startActivity(intent)
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