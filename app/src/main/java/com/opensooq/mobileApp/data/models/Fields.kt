package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class Fields : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
    var dataType: String = ""
    var parentId: Int = 0
    var parentName: String? = null
}