package ru.igni.manager

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.igni.manager.data.local.IgniDatabase
import ru.igni.manager.data.local.ShelfSeeder

class IgniApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: IgniDatabase by lazy { IgniDatabase.create(this) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            ShelfSeeder.seedIfEmpty(database)
        }
    }
}
