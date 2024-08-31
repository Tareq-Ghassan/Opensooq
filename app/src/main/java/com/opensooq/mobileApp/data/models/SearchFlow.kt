package com.opensooq.mobileApp.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * The `SearchFlow` class represents the flow or order of fields for a specific category in a search process.
 * This class is a RealmObject, allowing it to be stored and retrieved from a Realm database.
 *
 * @property categoryId The unique identifier for the category associated with this search flow. This is the primary key for the Realm database.
 * @property order A list that defines the order of fields or attributes that should be displayed during a search within the associated category.
 */
open class SearchFlow : RealmObject {
    @PrimaryKey
    var categoryId: Int = 0

    // This is a list of strings that represent the order of fields for a particular category.
    var order: RealmList<String> = realmListOf()

    /**
     * Checks if this `SearchFlow` object is equal to another object.
     *
     * Two `SearchFlow` objects are considered equal if they have the same `categoryId` and `order` properties.
     *
     * @param other The other object to compare this `SearchFlow` to.
     * @return `true` if the objects are considered equal, `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SearchFlow) return false

        if (categoryId != other.categoryId) return false
        if (order != other.order) return false

        return true
    }

    /**
     * Computes the hash code for this `SearchFlow` object.
     *
     * The hash code is computed based on the `categoryId` and `order` properties.
     *
     * @return The computed hash code.
     */
    override fun hashCode(): Int {
        var result = categoryId
        result = 31 * result + order.hashCode()
        return result
    }
}