package ru.igni.manager.data.local

import android.content.Context
import java.io.File

object DatabaseBackup {
    private const val DATABASE_NAME = "igni_manager.db"
    private const val PREFS_NAME = "igni_data_safety"
    private const val PREF_LAST_UPDATE = "last_backup_update_time"
    private const val MAX_BACKUPS = 5

    fun backupBeforeOpen(context: Context) {
        runCatching {
            val appContext = context.applicationContext
            val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
            val updateTime = packageInfo.lastUpdateTime
            val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            if (prefs.getLong(PREF_LAST_UPDATE, -1L) == updateTime) return

            val databaseFile = appContext.getDatabasePath(DATABASE_NAME)
            if (!databaseFile.exists()) {
                prefs.edit().putLong(PREF_LAST_UPDATE, updateTime).apply()
                return
            }

            val root = File(appContext.filesDir, "backups/$DATABASE_NAME")
            val target = File(root, updateTime.toString())
            target.mkdirs()

            copyIfExists(databaseFile, File(target, DATABASE_NAME))
            copyIfExists(File(databaseFile.path + "-wal"), File(target, "$DATABASE_NAME-wal"))
            copyIfExists(File(databaseFile.path + "-shm"), File(target, "$DATABASE_NAME-shm"))

            root.listFiles()
                ?.filter { it.isDirectory }
                ?.sortedByDescending { it.name.toLongOrNull() ?: 0L }
                ?.drop(MAX_BACKUPS)
                ?.forEach { it.deleteRecursively() }

            prefs.edit().putLong(PREF_LAST_UPDATE, updateTime).apply()
        }
    }

    private fun copyIfExists(source: File, target: File) {
        if (source.exists()) source.copyTo(target, overwrite = true)
    }
}
