package com.opensooq.mobileApp

import android.app.Application
import com.opensooq.mobileApp.data.database.RealmDatabase
import io.realm.kotlin.Realm

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