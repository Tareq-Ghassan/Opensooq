package com.opensooq.mobileApp.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Metadata : RealmObject {
    @PrimaryKey
    var id: String = "categories_metadata"
    var jsonHash: String = ""
}