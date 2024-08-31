package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * `Fields` represents a field in the application, storing its metadata and relationships.
 * This class is a RealmObject, making it persistable in a Realm database.
 *
 * @property id The unique identifier for this field. It serves as the primary key in the Realm database.
 * @property name The name of the field.
 * @property dataType The type of data stored in this field (e.g., string, integer, etc.).
 * @property parentId The identifier of the parent field, if this field is a child.
 * @property parentName The name of the parent field, if applicable. This field is nullable.
 */
open class Fields : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var dataType: String = ""
    var parentId: Int = 0
    var parentName: String? = null
}