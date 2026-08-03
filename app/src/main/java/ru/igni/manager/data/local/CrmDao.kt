package ru.igni.manager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CrmDao {
    @Query("""
        SELECT * FROM guests
        WHERE isArchived = 0
          AND (:query = ''
            OR name LIKE '%' || :query || '%' COLLATE NOCASE
            OR COALESCE(phone, '') LIKE '%' || :query || '%'
            OR COALESCE(telegram, '') LIKE '%' || :query || '%' COLLATE NOCASE
            OR comment LIKE '%' || :query || '%' COLLATE NOCASE)
        ORDER BY lastVisitAt DESC, name COLLATE NOCASE
    """)
    fun observeGuests(query: String = ""): Flow<List<GuestEntity>>

    @Query("SELECT * FROM guests WHERE id = :guestId LIMIT 1")
    fun observeGuest(guestId: Long): Flow<GuestEntity?>

    @Query("SELECT * FROM guests WHERE phone = :phone LIMIT 1")
    suspend fun findByPhone(phone: String): GuestEntity?

    @Query("SELECT * FROM guests WHERE id = :guestId LIMIT 1")
    suspend fun getGuest(guestId: Long): GuestEntity?

    @Query("SELECT * FROM visits WHERE guestId = :guestId ORDER BY startedAt DESC")
    suspend fun getVisits(guestId: Long): List<VisitEntity>

    @Query("SELECT * FROM hookah_history WHERE visitId = :visitId ORDER BY startedAt ASC")
    suspend fun getHookahsForVisit(visitId: Long): List<HookahHistoryEntity>

    @Query("SELECT * FROM hookah_mix_items WHERE hookahId = :hookahId ORDER BY percentage DESC")
    suspend fun getMixItemsForHookah(hookahId: Long): List<HookahMixItemEntity>

    @Query("""
        SELECT brandNameSnapshot FROM hookah_mix_items m
        INNER JOIN hookah_history h ON h.id = m.hookahId
        WHERE h.guestId = :guestId
        GROUP BY brandNameSnapshot
        ORDER BY SUM(m.grams) DESC
        LIMIT 3
    """)
    suspend fun favoriteBrands(guestId: Long): List<String>

    @Query("""
        SELECT flavorNameSnapshot FROM hookah_mix_items m
        INNER JOIN hookah_history h ON h.id = m.hookahId
        WHERE h.guestId = :guestId
        GROUP BY flavorNameSnapshot
        ORDER BY SUM(m.grams) DESC
        LIMIT 5
    """)
    suspend fun favoriteFlavors(guestId: Long): List<String>

    @Query("""
        SELECT strength FROM hookah_history
        WHERE guestId = :guestId
        GROUP BY strength
        ORDER BY COUNT(*) DESC, MAX(startedAt) DESC
        LIMIT 1
    """)
    suspend fun favoriteStrength(guestId: Long): Int?

    @Query("""
        UPDATE guests SET
            visitCount = visitCount + 1,
            totalHookahs = totalHookahs + :hookahCount,
            lastVisitAt = :closedAt,
            preferredStrength = :preferredStrength,
            favoriteBrands = :brands,
            favoriteFlavors = :flavors,
            updatedAt = :closedAt
        WHERE id = :guestId
    """)
    suspend fun updateGuestAfterVisit(guestId: Long, hookahCount: Int, closedAt: Long, preferredStrength: Int?, brands: String, flavors: String)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertGuest(guest: GuestEntity): Long

    @Update
    suspend fun updateGuest(guest: GuestEntity)

    @Query("UPDATE guests SET isArchived = 1, updatedAt = :updatedAt WHERE id = :guestId")
    suspend fun archiveGuest(guestId: Long, updatedAt: Long = System.currentTimeMillis())

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertVisit(visit: VisitEntity): Long

    @Query("SELECT * FROM visits WHERE guestId = :guestId ORDER BY startedAt DESC")
    fun observeVisits(guestId: Long): Flow<List<VisitEntity>>

    @Query("UPDATE visits SET closedAt = :closedAt, status = 'CLOSED' WHERE id = :visitId")
    suspend fun closeVisit(visitId: Long, closedAt: Long = System.currentTimeMillis())

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertHookah(hookah: HookahHistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMixItems(items: List<HookahMixItemEntity>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCoalChange(change: CoalChangeEntity): Long

    @Query("SELECT * FROM hookah_history WHERE guestId = :guestId ORDER BY startedAt DESC")
    fun observeHookahHistory(guestId: Long): Flow<List<HookahHistoryEntity>>

    @Query("SELECT * FROM hookah_mix_items WHERE hookahId = :hookahId ORDER BY percentage DESC")
    fun observeMixItems(hookahId: Long): Flow<List<HookahMixItemEntity>>

    @Query("SELECT * FROM coal_changes WHERE hookahId = :hookahId ORDER BY changedAt ASC")
    fun observeCoalChanges(hookahId: Long): Flow<List<CoalChangeEntity>>

    @Transaction
    suspend fun createHookahWithMix(
        hookah: HookahHistoryEntity,
        mixItems: List<HookahMixItemEntity>
    ): Long {
        val hookahId = insertHookah(hookah)
        insertMixItems(mixItems.map { it.copy(id = 0, hookahId = hookahId) })
        return hookahId
    }
}

@Dao
interface ActionLogDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entry: ActionLogEntity): Long

    @Query("SELECT * FROM action_log ORDER BY createdAt DESC LIMIT :limit")
    fun observeRecent(limit: Int = 200): Flow<List<ActionLogEntity>>

    @Query("SELECT * FROM action_log WHERE tableNumber = :tableNumber ORDER BY createdAt DESC")
    fun observeForTable(tableNumber: Int): Flow<List<ActionLogEntity>>
}
