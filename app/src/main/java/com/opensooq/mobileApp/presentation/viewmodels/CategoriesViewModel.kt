package com.opensooq.mobileApp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ViewModel class to manage and store UI-related data for categories and subcategories.
 * This class interacts with the CategoriesRepository to fetch data from the database
 * and manages the data for the UI using LiveData.
 */
class CategoriesViewModel(private val repository: CategoriesRepository) : ViewModel(){
    // LiveData to hold the list of main categories
    val categoriesLiveData: MutableLiveData<MutableList<MainCategory>> = MutableLiveData()
    // LiveData to hold the list of subcategories based on the selected category
    val subCategoriesLiveData: MutableLiveData<MutableList<SubCategory>> = MutableLiveData()

    // Initializer block to fetch categories when the ViewModel is created
    init {
        getCategories()
    }

    /**
     * Fetches categories from the local Realm database and updates the categoriesLiveData.
     */
    private fun getCategories(){
        categoriesLiveData.value = getCategoriesFromRealm()
    }

    /**
     * Checks if the given JSON data has changed and updates the local Realm database accordingly.
     * This method runs in the IO thread as it involves database operations.
     *
     * @param categoriesJson The JSON data for categories.
     * @param assignJson The JSON data for category assignments.
     * @param attributesJson The JSON data for category attributes.
     */
    suspend fun checkAndCacheJson(categoriesJson: String,assignJson: String,attributesJson: String) {
        withContext(Dispatchers.IO) {
            repository.checkAndUpdateCategories(categoriesJson,assignJson,attributesJson)
            // After updating categories, fetch the latest data
            fetchCategoriesFromRealm()
        }
    }

    /**
     * Fetches categories from the repository and posts the data to categoriesLiveData.
     */
    private fun fetchCategoriesFromRealm() {
        categoriesLiveData.postValue(repository.getCategories())
    }

    /**
     * Fetches subcategories based on the provided category ID.
     * Updates subCategoriesLiveData with the subcategories of the corresponding category.
     *
     * @param categoryId The ID of the main category whose subcategories are to be fetched.
     * @throws IllegalArgumentException If no category is found for the given ID.
     */
    fun getSubCategories(categoryId: Int) {
        categoryId.let {
            // Find the category with the given ID
            val category = categoriesLiveData.value?.find { it.id == categoryId }

            if (category != null) {
                // Update subCategoriesLiveData with the subcategories of the found category
                subCategoriesLiveData.value = category.subCategories
            } else {
                // Handle case where category is not found
                throw IllegalArgumentException("Category not found for ID: $categoryId")
            }
        }
    }

    /**
     * Closes the Realm database connection when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        RealmDatabase.close()
    }

    /**
     * Helper method to get categories from the Realm database through the repository.
     *
     * @return A list of MainCategory objects.
     */
    private fun getCategoriesFromRealm() :MutableList<MainCategory> {
        return repository.getCategories();
    }
}