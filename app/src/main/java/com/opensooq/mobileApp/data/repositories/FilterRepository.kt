package com.opensooq.mobileApp.data.repositories

import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.data.models.SearchFlow
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class FilterRepository(private val realm: Realm) {

    fun getSearchFlow(subcategoryId: Int): SearchFlow? {
        return realm.query<SearchFlow>("categoryId == $0", subcategoryId).first().find()
    }

    fun getAllFieldLabels(): List<FieldLabel> {
        return realm.query<FieldLabel>().find()
    }

    fun getAllFields(): List<Fields> {
        return realm.query<Fields>().find()
    }

    fun getOptionsByFieldId(fieldId: String): List<Options> {
        return realm.query<Options>("fieldId == $0", fieldId).find()
    }
}