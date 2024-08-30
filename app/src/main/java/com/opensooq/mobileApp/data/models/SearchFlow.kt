package com.opensooq.mobileApp.data.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class SearchFlow : RealmObject {
    @PrimaryKey
    var categoryId: Int = 0
    var order: RealmList<String> = realmListOf()
}