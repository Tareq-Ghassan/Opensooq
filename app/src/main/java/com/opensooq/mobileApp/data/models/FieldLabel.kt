package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject


open class FieldLabel : RealmObject {
    var fieldName: String = ""
    var labelAr: String = ""
    var labelEn: String = ""
}