package com.opensooq.mobileApp.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * `MainCategory` represents a primary category in the application. This class is a RealmObject,
 * making it persistable in a Realm database.
 *
 * @property id The unique identifier for this main category. It serves as the primary key in the Realm database.
 * @property name The name of the main category.
 * @property order The display order of the category.
 * @property parentId The identifier of the parent category, if this category is a child.
 * @property label The label of the category (default language).
 * @property labelEn The label of the category in English.
 * @property hasChild Indicates whether this category has subcategories.
 * @property reportingName The name used for reporting purposes.
 * @property icon The URL or resource path of the category's icon.
 * @property labelAr The label of the category in Arabic.
 * @property subCategories A list of subcategories under this main category. Stored as a RealmList.
 */
open class MainCategory : RealmObject {
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
    var subCategories: RealmList<SubCategory> = realmListOf()
}