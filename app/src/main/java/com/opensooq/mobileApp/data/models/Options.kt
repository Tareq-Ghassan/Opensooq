package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

/**
 * `Options` class represents a selectable option within a field in the application.
 * This class is a RealmObject, making it persistable in a Realm database.
 *
 * @property id The unique identifier for the option. This is the primary key for the Realm database.
 * @property fieldId The identifier of the field this option belongs to.
 * @property label The label of the option, typically used for display purposes.
 * @property labelEn The English version of the label, used for display in English contexts.
 * @property value The value associated with the option, which could be used programmatically.
 * @property optionImg An optional image URL associated with the option.
 * @property hasChild Indicates whether this option has child options or related sub-options.
 * @property parentId The identifier of the parent option, if this is a sub-option. It is nullable.
 * @property order The order in which this option should be displayed relative to other options.
 */
open class Options : RealmObject {
    @PrimaryKey
   var id: String= ""
   var fieldId: String= ""
   var label: String= ""
   var labelEn: String= ""
   var value: String= ""
   var optionImg: String? = null
   var hasChild: String= ""
   var parentId: String? = null
   var order: String= ""
}