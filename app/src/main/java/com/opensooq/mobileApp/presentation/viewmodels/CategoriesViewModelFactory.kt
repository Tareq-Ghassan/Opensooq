package com.opensooq.mobileApp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opensooq.mobileApp.data.repositories.CategoriesRepository

/**
 * Factory class to create instances of the CategoriesViewModel.
 * This class ensures that the ViewModel is created with the required dependencies.
 *
 * @param repository The CategoriesRepository instance that provides data to the ViewModel.
 */
class CategoriesViewModelFactory(private val repository: CategoriesRepository) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the given ViewModel class.
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A newly created ViewModel instance.
     * @throws IllegalArgumentException If the ViewModel class is unknown.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}