package ru.igni.manager.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ShelfDao {
    @Query("SELECT COUNT(*) FROM tobacco_brands") suspend fun countBrands(): Int
    @Query("SELECT * FROM tobacco_brands ORDER BY name COLLATE NOCASE") fun observeBrands(): Flow<List<TobaccoBrandEntity>>

    @Query("""
        SELECT f.id AS flavorId, f.brandId AS brandId, b.name AS brandName,
               f.name AS flavorName, f.descriptor AS descriptor, f.isAvailable AS isAvailable,
               COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) AS totalGrams,
               COUNT(CASE WHEN c.isActive = 1 THEN 1 END) AS containerCount
        FROM tobacco_flavors f JOIN tobacco_brands b ON b.id = f.brandId
        LEFT JOIN tobacco_containers c ON c.flavorId = f.id
        GROUP BY f.id ORDER BY b.name COLLATE NOCASE, f.name COLLATE NOCASE
    """) fun observeFlavorStock(): Flow<List<FlavorWithStock>>

    @Query("""
        SELECT f.id AS flavorId, b.name AS brandName, f.name AS flavorName,
               f.descriptor AS descriptor, b.densityCoefficient AS densityCoefficient,
               COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) AS totalGrams
        FROM tobacco_flavors f JOIN tobacco_brands b ON b.id = f.brandId
        LEFT JOIN tobacco_containers c ON c.flavorId = f.id
        WHERE f.isAvailable = 1 GROUP BY f.id
        HAVING COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) > 5.0
        ORDER BY b.name COLLATE NOCASE, f.name COLLATE NOCASE
    """) fun observeMixOptions(): Flow<List<MixFlavorOption>>

    @Query("SELECT * FROM tobacco_containers WHERE flavorId = :flavorId AND isActive = 1 AND remainingGrams > 0 ORDER BY createdAt ASC")
    suspend fun getActiveContainers(flavorId: Long): List<TobaccoContainerEntity>

    @Transaction
    suspend fun deductFlavorOrThrow(flavorId: Long, grams: Double, note: String = "Списание кальяна") {
        require(grams >= 0)
        val containers = getActiveContainers(flavorId)
        if (containers.sumOf { it.remainingGrams } + 0.0001 < grams) throw IllegalStateException("Недостаточно табака на полке")
        var remaining = grams
        for (container in containers) {
            if (remaining <= 0.0001) break
            val used = minOf(container.remainingGrams, remaining)
            val newStock = (container.remainingGrams - used).coerceAtLeast(0.0)
            updateContainerStock(container.id, newStock, newStock > 0.0001)
            remaining -= used
        }
        if (grams > 0.0001) insertMovement(StockMovementEntity(flavorId = flavorId, deltaGrams = -grams, movementType = "WRITE_OFF", note = note))
    }

    @Query("SELECT * FROM tobacco_containers WHERE flavorId = :flavorId ORDER BY isActive DESC, createdAt DESC")
    fun observeContainers(flavorId: Long): Flow<List<TobaccoContainerEntity>>

    @Query("SELECT * FROM stock_movements WHERE flavorId = :flavorId ORDER BY createdAt DESC LIMIT 100")
    fun observeMovements(flavorId: Long): Flow<List<StockMovementEntity>>

    @Query("""
        SELECT p.id AS orderId, p.flavorId AS flavorId, b.name AS brandName, f.name AS flavorName,
               COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) AS currentGrams,
               p.recommendedGrams AS recommendedGrams, p.createdAt AS createdAt
        FROM purchase_order_items p JOIN tobacco_flavors f ON f.id = p.flavorId
        JOIN tobacco_brands b ON b.id = f.brandId LEFT JOIN tobacco_containers c ON c.flavorId = f.id
        GROUP BY p.id ORDER BY b.name COLLATE NOCASE, f.name COLLATE NOCASE
    """) fun observeOrderedItems(): Flow<List<OrderedFlavor>>

    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertBrand(brand: TobaccoBrandEntity): Long
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertFlavor(flavor: TobaccoFlavorEntity): Long
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insertContainer(container: TobaccoContainerEntity): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertOrderItem(item: PurchaseOrderItemEntity): Long
    @Insert suspend fun insertMovement(movement: StockMovementEntity): Long
    @Update suspend fun updateFlavor(flavor: TobaccoFlavorEntity)
    @Update suspend fun updateContainer(container: TobaccoContainerEntity)
    @Query("UPDATE tobacco_flavors SET isAvailable = :available WHERE id = :flavorId") suspend fun setFlavorAvailability(flavorId: Long, available: Boolean)
    @Query("UPDATE tobacco_containers SET remainingGrams = :grams, isActive = :active WHERE id = :containerId") suspend fun updateContainerStock(containerId: Long, grams: Double, active: Boolean)
    @Query("DELETE FROM purchase_order_items WHERE flavorId = :flavorId") suspend fun removeOrderItem(flavorId: Long)

    @Transaction
    suspend fun receiveDelivery(flavorId: Long, grams: Double, label: String = "Поставка") {
        require(grams > 0)
        insertContainer(TobaccoContainerEntity(flavorId = flavorId, label = label, remainingGrams = grams))
        insertMovement(StockMovementEntity(flavorId = flavorId, deltaGrams = grams, movementType = "DELIVERY", note = label))
        removeOrderItem(flavorId)
    }

    @Query("""
        SELECT b.id AS brandId, b.name AS brandName, b.densityCoefficient AS densityCoefficient,
               CAST(COUNT(CASE WHEN s.previousActualGrams IS NOT NULL AND s.includedInAnalytics = 1 AND s.calculatedConsumptionGrams > 0 THEN 1 END) AS INTEGER) AS completedPeriods,
               COALESCE(SUM(CASE WHEN s.previousActualGrams IS NOT NULL AND s.includedInAnalytics = 1 THEN s.actualConsumptionGrams ELSE 0 END), 0) AS totalActualConsumptionGrams,
               COALESCE(SUM(CASE WHEN s.previousActualGrams IS NOT NULL AND s.includedInAnalytics = 1 THEN s.calculatedConsumptionGrams ELSE 0 END), 0) AS totalCalculatedConsumptionGrams,
               MAX(i.takenAt) AS lastInventoryAt
        FROM tobacco_brands b
        LEFT JOIN brand_inventory_snapshots s ON s.brandId = b.id
        LEFT JOIN inventory_sessions i ON i.id = s.sessionId
        GROUP BY b.id
        ORDER BY b.name COLLATE NOCASE
    """)
    fun observeBrandAnalytics(): Flow<List<BrandConsumptionAnalytics>>

    @Query("""
        SELECT i.id AS sessionId, i.takenAt AS takenAt, i.note AS note, COUNT(s.brandId) AS brandCount
        FROM inventory_sessions i LEFT JOIN brand_inventory_snapshots s ON s.sessionId = i.id
        GROUP BY i.id ORDER BY i.takenAt DESC
    """)
    fun observeInventoryHistory(): Flow<List<InventorySessionSummary>>

    @Query("SELECT * FROM inventory_sessions ORDER BY takenAt DESC LIMIT 1")
    suspend fun getLastInventorySession(): InventorySessionEntity?

    @Query("SELECT * FROM brand_inventory_snapshots WHERE sessionId = :sessionId")
    suspend fun getSnapshotsForSession(sessionId: Long): List<BrandInventorySnapshotEntity>

    @Query("""
        SELECT COALESCE(SUM(sm.deltaGrams), 0) FROM stock_movements sm
        JOIN tobacco_flavors f ON f.id = sm.flavorId
        WHERE f.brandId = :brandId AND sm.deltaGrams > 0 AND sm.createdAt > :fromAt AND sm.createdAt <= :toAt
    """)
    suspend fun getBrandDeliveries(brandId: Long, fromAt: Long, toAt: Long): Double

    @Query("""
        SELECT COALESCE(-SUM(sm.deltaGrams), 0) FROM stock_movements sm
        JOIN tobacco_flavors f ON f.id = sm.flavorId
        WHERE f.brandId = :brandId AND sm.movementType = 'WRITE_OFF'
          AND sm.deltaGrams < 0 AND sm.createdAt > :fromAt AND sm.createdAt <= :toAt
    """)
    suspend fun getBrandCalculatedConsumption(brandId: Long, fromAt: Long, toAt: Long): Double

    @Insert suspend fun insertInventorySession(session: InventorySessionEntity): Long
    @Insert suspend fun insertBrandSnapshots(items: List<BrandInventorySnapshotEntity>)

    @Query("UPDATE tobacco_brands SET densityCoefficient = :coefficient WHERE id = :brandId")
    suspend fun updateBrandCoefficient(brandId: Long, coefficient: Double)

    @Query("""
        SELECT COALESCE(SUM(actualConsumptionGrams), 0) FROM brand_inventory_snapshots
        WHERE brandId = :brandId AND previousActualGrams IS NOT NULL AND includedInAnalytics = 1
    """)
    suspend fun getAccumulatedActualConsumption(brandId: Long): Double

    @Query("""
        SELECT COALESCE(SUM(calculatedConsumptionGrams), 0) FROM brand_inventory_snapshots
        WHERE brandId = :brandId AND previousActualGrams IS NOT NULL AND includedInAnalytics = 1
    """)
    suspend fun getAccumulatedCalculatedConsumption(brandId: Long): Double

    @Query("""
        SELECT COUNT(*) FROM brand_inventory_snapshots
        WHERE brandId = :brandId AND previousActualGrams IS NOT NULL AND includedInAnalytics = 1 AND calculatedConsumptionGrams > 0
    """)
    suspend fun getCompletedInventoryPeriods(brandId: Long): Int

    @Transaction
    suspend fun completeWeeklyInventory(inputs: List<BrandInventoryInput>, note: String = "Еженедельная инвентаризация"): Long {
        require(inputs.isNotEmpty())
        val now = System.currentTimeMillis()
        val previousSession = getLastInventorySession()
        val previousByBrand = previousSession?.let { getSnapshotsForSession(it.id).associateBy { snap -> snap.brandId } }.orEmpty()
        val sessionId = insertInventorySession(InventorySessionEntity(takenAt = now, note = note))
        val snapshots = inputs.map { input ->
            val previous = previousByBrand[input.brandId]
            val fromAt = previousSession?.takenAt ?: now
            val deliveries = if (previous != null) getBrandDeliveries(input.brandId, fromAt, now) else 0.0
            val calculated = if (previous != null) getBrandCalculatedConsumption(input.brandId, fromAt, now) else 0.0
            val actual = if (previous != null) (previous.actualGrams + deliveries - input.actualGrams).coerceAtLeast(0.0) else 0.0
            val weekly = if (previous != null && calculated > 0.0001) actual / calculated else null
            BrandInventorySnapshotEntity(
                sessionId = sessionId,
                brandId = input.brandId,
                actualGrams = input.actualGrams.coerceAtLeast(0.0),
                previousActualGrams = previous?.actualGrams,
                deliveryGrams = deliveries,
                calculatedConsumptionGrams = calculated,
                actualConsumptionGrams = actual,
                weeklyCoefficient = weekly,
                periodStartAt = previousSession?.takenAt,
                periodEndAt = now
            )
        }
        insertBrandSnapshots(snapshots)
        inputs.forEach { input ->
            val periods = getCompletedInventoryPeriods(input.brandId)
            val calculatedTotal = getAccumulatedCalculatedConsumption(input.brandId)
            if (periods >= 4 && calculatedTotal > 0.0001) {
                val actualTotal = getAccumulatedActualConsumption(input.brandId)
                updateBrandCoefficient(input.brandId, (actualTotal / calculatedTotal).coerceIn(0.50, 1.80))
            }
        }
        return sessionId
    }

}
