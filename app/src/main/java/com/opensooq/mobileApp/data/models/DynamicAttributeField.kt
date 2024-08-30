package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class DynamicAttributeField : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var options: String = ""
}