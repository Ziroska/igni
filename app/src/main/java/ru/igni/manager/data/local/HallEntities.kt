package ru.igni.manager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_table_sessions")
data class ActiveTableSessionEntity(
    @PrimaryKey val tableNumber: Int,
    val guestName: String,
    val guestId: Long? = null,
    val bowlType: String,
    val strength: Int,
    val hookahCount: Int,
    val mixData: String,
    val startedAt: Long,
    val lastCoalAt: Long,
    val coalChanges: Int
)

@Entity(tableName = "favorite_mixes")
data class FavoriteMixEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val mixData: String,
    val createdAt: Long = System.currentTimeMillis()
)
