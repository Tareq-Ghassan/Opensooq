package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * `Metadata` class represents metadata information for categories in the application.
 * This class is a RealmObject, making it persistable in a Realm database.
 *
 * @property id The unique identifier for this metadata object. It is used as the primary key in the Realm database.
 * @property jsonHash A string representing the hash of the JSON data associated with the categories.
 */
class Metadata : RealmObject {
    @PrimaryKey
    var id: String = "categories_metadata"
    var jsonHash: String = ""
}