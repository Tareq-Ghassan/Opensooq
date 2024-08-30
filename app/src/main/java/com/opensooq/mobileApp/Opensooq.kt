package com.opensooq.mobileApp

import android.app.Application
import com.opensooq.mobileApp.data.database.RealmDatabase

class Opensooq  : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Realm using the RealmDatabase object
        RealmDatabase.init()
    }

    override fun onTerminate() {
        super.onTerminate()

        // Close Realm instance when the application is terminated
        RealmDatabase.close()
    }
}