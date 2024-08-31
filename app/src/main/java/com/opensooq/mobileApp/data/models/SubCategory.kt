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

    /**
     * Compares this `SubCategory` object to another object for equality.
     * The comparison checks if all properties of the `SubCategory` objects are identical.
     *
     * @param other The object to compare this `SubCategory` against.
     * @return `true` if the specified object is equal to this `SubCategory`; `false` otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SubCategory) return false

        return id == other.id &&
                name == other.name &&
                order == other.order &&
                parentId == other.parentId &&
                label == other.label &&
                labelEn == other.labelEn &&
                hasChild == other.hasChild &&
                reportingName == other.reportingName &&
                icon == other.icon &&
                labelAr == other.labelAr
    }

    /**
     * Returns a hash code value for this `SubCategory` object.
     * The hash code is generated based on the values of the properties.
     * This is important for ensuring that `SubCategory` objects can be used in hash-based collections.
     *
     * @return The hash code value for this `SubCategory`.
     */
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + order
        result = 31 * result + parentId
        result = 31 * result + label.hashCode()
        result = 31 * result + labelEn.hashCode()
        result = 31 * result + hasChild.hashCode()
        result = 31 * result + reportingName.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + labelAr.hashCode()
        return result
    }
}

