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
}