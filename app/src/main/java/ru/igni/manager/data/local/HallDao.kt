package ru.igni.manager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HallDao {
    @Query("SELECT * FROM active_table_sessions ORDER BY tableNumber")
    fun observeActiveSessions(): Flow<List<ActiveTableSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSession(session: ActiveTableSessionEntity)

    @Query("DELETE FROM active_table_sessions WHERE tableNumber = :tableNumber")
    suspend fun deleteSession(tableNumber: Int)

    @Query("SELECT * FROM favorite_mixes ORDER BY createdAt DESC")
    fun observeFavoriteMixes(): Flow<List<FavoriteMixEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertFavorite(favorite: FavoriteMixEntity): Long

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteMixEntity)
}
