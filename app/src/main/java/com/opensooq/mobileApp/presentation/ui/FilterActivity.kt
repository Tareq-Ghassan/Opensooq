package com.opensooq.mobileApp.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opensooq.mobileApp.R
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.repositories.FilterRepository
import com.opensooq.mobileApp.presentation.viewmodels.FilterViewModel
import com.opensooq.mobileApp.presentation.viewmodels.FilterViewModelFactory
import com.squareup.picasso.Picasso

class FilterActivity : AppCompatActivity() {

    private val viewModel: FilterViewModel by viewModels {
        val realmInstance = RealmDatabase.getInstance()
        val repository = FilterRepository(realmInstance)
        FilterViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val subcategoryId = intent.getIntExtra("SUBCATEGORY_ID", 0)
        Log.e("FilterActivity", "SUBCATEGORY_ID $subcategoryId")

        viewModel.loadFieldsForSubcategory(subcategoryId)

        viewModel.fieldsAndLabelsToDisplay.observe(this, Observer { fieldsAndLabels ->
            fieldsAndLabels?.let {
                val adapter = FilterAdapter(it)
                recyclerView.adapter = adapter
            }
        })

        setupActionBar()
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.filter)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return true
    }
}