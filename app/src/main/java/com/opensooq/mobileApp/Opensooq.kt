package com.opensooq.mobileApp

import android.app.Application
import com.opensooq.mobileApp.data.database.RealmDatabase

/**
 * The `Opensooq` class is the main Application class for the app.
 * It initializes and terminates resources needed throughout the app's lifecycle.
 */
class Opensooq  : Application() {

    /**
     * Called when the application is starting, before any other application objects have been created.
     * This is where the Realm database is initialized.
     */
    override fun onCreate() {
        super.onCreate()

        // Initialize Realm using the RealmDatabase object
        RealmDatabase.init()
    }

    /**
     * Called when the application is terminating.
     * This is where the Realm database is closed to free up resources.
     */
    override fun onTerminate() {
        super.onTerminate()

        // Close Realm instance when the application is terminated
        RealmDatabase.close()
    }
}