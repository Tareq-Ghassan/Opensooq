package com.opensooq.mobileApp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.data.repositories.FilterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for managing UI-related data for filtering options in the app.
 * This ViewModel interacts with the FilterRepository to fetch and process data required for
 * displaying fields and options based on a selected subcategory.
 *
 * @param repository The repository that handles data operations related to filters.
 */
class FilterViewModel(private val repository: FilterRepository) : ViewModel() {

    // LiveData that holds the list of fields and their corresponding labels to display.
    private val _fieldsAndLabelsToDisplay = MutableLiveData<List<Pair<Fields, FieldLabel>>>()
    val fieldsAndLabelsToDisplay: LiveData<List<Pair<Fields, FieldLabel>>> get() = _fieldsAndLabelsToDisplay

    /**
     * Loads the fields and their corresponding labels for the given subcategory.
     * This method is executed in the background using the IO dispatcher.
     *
     * @param subcategoryId The ID of the subcategory for which to load the fields and labels.
     */
    fun loadFieldsForSubcategory(subcategoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchFlow = repository.getSearchFlow(subcategoryId)

            if (searchFlow != null) {
                val orderList = searchFlow.order

                val allFieldLabels = repository.getAllFieldLabels()
                val fieldsToMap = repository.getAllFields()

                val fieldsAndLabels = mutableListOf<Pair<Fields, FieldLabel>>()

                orderList.forEach { orderItem ->
                    val fieldLabel = allFieldLabels.find { it.fieldName == orderItem }
                    val field = fieldsToMap.find { it.name == orderItem }

                    if (field != null && fieldLabel != null) {
                        fieldsAndLabels.add(Pair(field, fieldLabel))
                    }
                }
                // Post the list of fields and labels to the LiveData
                _fieldsAndLabelsToDisplay.postValue(fieldsAndLabels)
            }
        }
    }

    /**
     * Retrieves the list of options for a specific field.
     *
     * @param fieldId The ID of the field for which to retrieve the options.
     * @return A list of Options objects related to the specified field.
     */
    fun getOptionsForField(fieldId: String): List<Options> {
        return repository.getOptionsByFieldId(fieldId)
    }
}