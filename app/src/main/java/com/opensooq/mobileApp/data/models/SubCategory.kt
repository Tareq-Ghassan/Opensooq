package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * The `SubCategory` class represents a subcategory within a larger category structure.
 * This class is a RealmObject, allowing it to be stored and retrieved from a Realm database.
 *
 * @property id The unique identifier for the subcategory. This serves as the primary key in the Realm database.
 * @property name The name of the subcategory.
 * @property order The order or position of the subcategory relative to other subcategories in the same category.
 * @property parentId The unique identifier of the parent category that this subcategory belongs to.
 * @property label The label associated with the subcategory.
 * @property labelEn The English label of the subcategory.
 * @property hasChild Indicates whether this subcategory has child categories (true if it has, false otherwise).
 * @property reportingName The reporting name used for analytics or tracking purposes.
 * @property icon The URL or path to the icon representing this subcategory.
 * @property labelAr The Arabic label of the subcategory.
 */
open class SubCategory : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var order: Int = 0
    var parentId: Int = 0
    var label: String = ""
    var labelEn: String = ""
    var hasChild: Boolean = false
    var reportingName: String = ""
    var icon: String = ""
    var labelAr: String = ""
}

