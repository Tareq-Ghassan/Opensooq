package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject

/**
 * `FieldLabel` represents a label for a specific field in the application.
 * It is used to store localized labels for fields in both Arabic and English.
 * This class is a RealmObject, meaning it can be persisted in a Realm database.
 *
 * @property fieldName The name of the field to which this label is associated.
 * @property labelAr The Arabic label for the field.
 * @property labelEn The English label for the field.
 */
open class FieldLabel : RealmObject {
    var fieldName: String = ""
    var labelAr: String = ""
    var labelEn: String = ""
}