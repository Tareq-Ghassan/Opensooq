package com.opensooq.mobileApp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opensooq.mobileApp.data.database.RealmDatabase
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.repositories.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoriesViewModel(private val repository: CategoriesRepository) : ViewModel(){
    val categoriesLiveData: MutableLiveData<MutableList<MainCategory>> = MutableLiveData()

    init {
        getCategories()
    }

    private fun getCategories(){
        categoriesLiveData.value = getCategoriesFromRealm()
    }

    suspend fun checkAndCacheJson(categoriesJson: String) {
        withContext(Dispatchers.IO) {
            repository.checkAndUpdateCategories(categoriesJson)
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