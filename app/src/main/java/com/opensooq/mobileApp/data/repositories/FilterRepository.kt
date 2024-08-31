package com.opensooq.mobileApp.data.repositories

import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.data.models.SearchFlow
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

/**
 * Repository class that provides methods to interact with the filter-related data in the Realm database.
 * It is responsible for fetching search flow, field labels, fields, and options based on various criteria.
 *
 * @param realm The Realm instance used to interact with the database.
 */
class FilterRepository(private val realm: Realm) {

    /**
     * Retrieves the `SearchFlow` object for a given subcategory ID.
     *
     * @param subcategoryId The ID of the subcategory for which the search flow is required.
     * @return The `SearchFlow` object if found, or null if not found.
     */
    fun getSearchFlow(subcategoryId: Int): SearchFlow? {
        return realm.query<SearchFlow>("categoryId == $0", subcategoryId).first().find()
    }

    /**
     * Retrieves all `FieldLabel` objects from the Realm database.
     *
     * @return A list of `FieldLabel` objects.
     */
    fun getAllFieldLabels(): List<FieldLabel> {
        return realm.query<FieldLabel>().find()
    }

    /**
     * Retrieves all `Fields` objects from the Realm database.
     *
     * @return A list of `Fields` objects.
     */
    fun getAllFields(): List<Fields> {
        return realm.query<Fields>().find()
    }

    /**
     * Retrieves a list of `Options` objects associated with a specific field ID.
     *
     * @param fieldId The ID of the field for which options are required.
     * @return A list of `Options` objects related to the given field ID.
     */
    fun getOptionsByFieldId(fieldId: String): List<Options> {
        return realm.query<Options>("fieldId == $0", fieldId).find()
    }
}