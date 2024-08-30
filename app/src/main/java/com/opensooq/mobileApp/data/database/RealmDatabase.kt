package com.opensooq.mobileApp.data.database

import com.opensooq.mobileApp.data.models.DynamicAttributeAssignment
import com.opensooq.mobileApp.data.models.DynamicAttributeField
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import com.opensooq.mobileApp.data.models.Metadata
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmDatabase {


    private var realm: Realm? = null

    // Initialize Realm
    fun init() {
        if (realm == null) {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    MainCategory::class,
                    SubCategory::class,
                    DynamicAttributeAssignment::class,
                    DynamicAttributeField::class,
                    Metadata::class
                )
            )
                .name("opensooq.realm")
                .schemaVersion(2)
                .deleteRealmIfMigrationNeeded()
                .build()

            realm = Realm.open(config)
        }
    }

    // Get the Realm instance (call When you need to use it)
    fun getInstance(): Realm {
        return realm ?: throw IllegalStateException("RealmDatabase is not initialized. Call init() first.")
    }

    // Close the Realm instance (call this in onDestroy or onStop)
    fun close() {
        realm?.close()
        realm = null
    }
}