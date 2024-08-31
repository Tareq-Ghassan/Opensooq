package com.opensooq.mobileApp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opensooq.mobileApp.data.repositories.FilterRepository

/**
 * A factory class for creating instances of [FilterViewModel].
 * This factory takes a [FilterRepository] as a parameter, which is used to initialize the ViewModel.
 *
 * @param repository The repository used by the ViewModel to handle data operations related to filters.
 */
class FilterViewModelFactory(private val repository: FilterRepository) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the specified ViewModel class.
     * This method is responsible for constructing the ViewModel with the provided repository.
     *
     * @param modelClass The class of the ViewModel to be created.
     * @return A newly created ViewModel instance.
     * @throws IllegalArgumentException If the specified modelClass is not assignable from [FilterViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}