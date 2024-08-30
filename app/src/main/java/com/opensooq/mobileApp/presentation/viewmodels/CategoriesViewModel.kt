package com.opensooq.mobileApp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesViewModel(private val repository: CategoriesRepository) : ViewModel(){
    val categoriesLiveData: MutableLiveData<MutableList<MainCategory>> = MutableLiveData()
    val subCategoriesLiveData: MutableLiveData<MutableList<SubCategory>> = MutableLiveData()


    init {
        getCategories()
    }

    private fun getCategories(){
        categoriesLiveData.value = getCategoriesFromRealm()
    }

    suspend fun checkAndCacheJson(categoriesJson: String) {
        withContext(Dispatchers.IO) {
            repository.checkAndUpdateCategories(categoriesJson)
            // After updating categories, fetch the latest data
            fetchCategoriesFromRealm()
        }
    }
    private fun fetchCategoriesFromRealm() {
        categoriesLiveData.postValue(repository.getCategories())
    }

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

    override fun onCleared() {
        super.onCleared()
        RealmDatabase.close()
    }

    private fun getCategoriesFromRealm() :MutableList<MainCategory> {
        return repository.getCategories();
    }
}