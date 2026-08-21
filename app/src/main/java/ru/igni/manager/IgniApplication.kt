package ru.igni.manager

import android.app.Application
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.igni.manager.data.local.DatabaseBackup
import ru.igni.manager.data.local.IgniDatabase
import ru.igni.manager.data.local.ShelfSeeder

class IgniApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: IgniDatabase by lazy { IgniDatabase.create(this) }

    override fun onCreate() {
        super.onCreate()
        DatabaseBackup.backupBeforeOpen(this)
        applicationScope.launch {
            initializeShelfOnce()
        }
    }

    private suspend fun initializeShelfOnce() {
        val prefs = getSharedPreferences(DATA_SAFETY_PREFS, Context.MODE_PRIVATE)
        if (prefs.getBoolean(SHELF_INITIALIZED, false)) return

        if (database.shelfDao().countBrands() == 0) {
            ShelfSeeder.seedIfEmpty(database)
        }

        prefs.edit().putBoolean(SHELF_INITIALIZED, true).apply()
    }

    private companion object {
        const val DATA_SAFETY_PREFS = "igni_data_safety"
        const val SHELF_INITIALIZED = "shelf_initialized"
    }
}
