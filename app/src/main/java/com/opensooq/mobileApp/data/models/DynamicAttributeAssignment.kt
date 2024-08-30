package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class DynamicAttributeAssignment : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var categoryId: Int = 0
    var order: String = ""
}
