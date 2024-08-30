package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


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

