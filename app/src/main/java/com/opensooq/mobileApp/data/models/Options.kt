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
   var id: String = ""
   var fieldId: String = ""
   var label: String = ""
   var labelEn: String = ""
   var value: String = ""
   var optionImg: String? = null
   var hasChild: String = ""
   var parentId: String? = null
   var order: String = ""

   /**
    * Checks whether this `Options` object is equal to another object.
    *
    * This method overrides the default `equals` implementation to compare the properties
    * of two `Options` objects. It ensures that two `Options` objects are considered equal
    * if and only if all their respective properties are equal.
    *
    * @param other The object to compare with this `Options` instance.
    * @return `true` if the specified object is equal to this `Options` object; `false` otherwise.
    */
   override fun equals(other: Any?): Boolean {
      // Check if the current instance is the same as the other object
      if (this === other) return true

      // Check if the other object is of the same class type
      if (javaClass != other?.javaClass) return false

      // Cast the other object to Options type
      other as Options

      // Compare each property of the two Options objects
      if (id != other.id) return false
      if (fieldId != other.fieldId) return false
      if (label != other.label) return false
      if (labelEn != other.labelEn) return false
      if (value != other.value) return false
      if (optionImg != other.optionImg) return false
      if (hasChild != other.hasChild) return false
      if (parentId != other.parentId) return false
      if (order != other.order) return false

      return true
   }

   /**
    * Generates a hash code for this `Options` object.
    *
    * This method overrides the default `hashCode` implementation to generate a hash code
    * based on the properties of the `Options` object. The hash code is computed using the
    * values of all the properties, ensuring that two equal `Options` objects (as determined
    * by the `equals` method) will produce the same hash code.
    *
    * @return The hash code value for this `Options` object.
    */
   override fun hashCode(): Int {
      // Initialize the result with the hash code of the id
      var result = id.hashCode()

      // Incorporate the hash codes of other properties into the result
      result = 31 * result + fieldId.hashCode()
      result = 31 * result + label.hashCode()
      result = 31 * result + labelEn.hashCode()
      result = 31 * result + value.hashCode()
      result = 31 * result + (optionImg?.hashCode() ?: 0)
      result = 31 * result + hasChild.hashCode()
      result = 31 * result + (parentId?.hashCode() ?: 0)
      result = 31 * result + order.hashCode()

      return result
   }

}
