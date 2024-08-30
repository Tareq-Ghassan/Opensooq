package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

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