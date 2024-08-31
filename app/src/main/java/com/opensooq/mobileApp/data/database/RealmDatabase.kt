package com.opensooq.mobileApp.data.database

import com.opensooq.mobileApp.data.models.FieldLabel
import com.opensooq.mobileApp.data.models.Fields
import com.opensooq.mobileApp.data.models.MainCategory
import com.opensooq.mobileApp.data.models.SubCategory
import com.opensooq.mobileApp.data.models.Metadata
import com.opensooq.mobileApp.data.models.Options
import com.opensooq.mobileApp.data.models.SearchFlow
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

/**
 * RealmDatabase is an object that manages the initialization and lifecycle of the Realm database.
 * It provides methods to initialize the database, get the instance, and close it when no longer needed.
 */
object RealmDatabase {

    // Private variable to hold the Realm instance
    private var realm: Realm? = null

    /**
     * Initializes the Realm database with the specified configuration.
     * This method should be called once in the application lifecycle, typically in the Application class.
     */
    fun init() {
        if (realm == null) {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    MainCategory::class,
                    SubCategory::class,
                    SearchFlow::class,
                    FieldLabel::class,
                    Fields::class,
                    Options::class,
                    Metadata::class
                )
            ).name("opensooq.realm") // Set the name of the Realm file
                .schemaVersion(2) // Define the schema version
                .deleteRealmIfMigrationNeeded() // Automatically delete Realm file if migration is needed
                .build()

            // Open the Realm instance with the specified configuration
            realm = Realm.open(config)
        }
    }

    /**
     * Returns the current Realm instance.
     * This method should be called whenever you need to access the Realm database.
     * If Realm has not been initialized, it throws an IllegalStateException.
     *
     * @return Realm instance
     * @throws IllegalStateException if Realm is not initialized
     */
    fun getInstance(): Realm {
        return realm ?: throw IllegalStateException("RealmDatabase is not initialized. Call init() first.")
    }

    /**
     * Closes the current Realm instance and sets the reference to null.
     * This method should be called in lifecycle methods such as onDestroy or onStop
     * to properly clean up the Realm instance.
     */
    fun close() {
        realm?.close()
        realm = null
    }
}