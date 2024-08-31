package com.opensooq.mobileApp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.repositories.FilterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilterViewModel(private val repository: FilterRepository) : ViewModel() {

    private val _fieldsAndLabelsToDisplay = MutableLiveData<List<Pair<Fields, FieldLabel>>>()
    val fieldsAndLabelsToDisplay: LiveData<List<Pair<Fields, FieldLabel>>> get() = _fieldsAndLabelsToDisplay

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

                _fieldsAndLabelsToDisplay.postValue(fieldsAndLabels)
            }
        }
    }
}