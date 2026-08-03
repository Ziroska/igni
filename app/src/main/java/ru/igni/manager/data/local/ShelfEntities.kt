package ru.igni.manager.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tobacco_brands")
data class TobaccoBrandEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val densityCoefficient: Double = 1.0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "tobacco_flavors",
    foreignKeys = [
        ForeignKey(
            entity = TobaccoBrandEntity::class,
            parentColumns = ["id"],
            childColumns = ["brandId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("brandId")]
)
data class TobaccoFlavorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brandId: Long,
    val name: String,
    val descriptor: String = "",
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "tobacco_containers",
    foreignKeys = [
        ForeignKey(
            entity = TobaccoFlavorEntity::class,
            parentColumns = ["id"],
            childColumns = ["flavorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("flavorId")]
)
data class TobaccoContainerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val flavorId: Long,
    val label: String,
    val remainingGrams: Double,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class FlavorWithStock(
    val flavorId: Long,
    val brandId: Long,
    val brandName: String,
    val flavorName: String,
    val descriptor: String,
    val isAvailable: Boolean,
    val totalGrams: Double,
    val containerCount: Int
)


data class MixFlavorOption(
    val flavorId: Long,
    val brandName: String,
    val flavorName: String,
    val descriptor: String,
    val densityCoefficient: Double,
    val totalGrams: Double
)


@Entity(
    tableName = "stock_movements",
    foreignKeys = [ForeignKey(
        entity = TobaccoFlavorEntity::class,
        parentColumns = ["id"],
        childColumns = ["flavorId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("flavorId"), Index("createdAt")]
)
data class StockMovementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val flavorId: Long,
    val deltaGrams: Double,
    val movementType: String,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "purchase_order_items",
    foreignKeys = [ForeignKey(
        entity = TobaccoFlavorEntity::class,
        parentColumns = ["id"],
        childColumns = ["flavorId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["flavorId"], unique = true), Index("createdAt")]
)
data class PurchaseOrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val flavorId: Long,
    val recommendedGrams: Double = 250.0,
    val createdAt: Long = System.currentTimeMillis()
)

data class OrderedFlavor(
    val orderId: Long,
    val flavorId: Long,
    val brandName: String,
    val flavorName: String,
    val currentGrams: Double,
    val recommendedGrams: Double,
    val createdAt: Long
)


@Entity(tableName = "inventory_sessions", indices = [Index("takenAt")])
data class InventorySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val takenAt: Long = System.currentTimeMillis(),
    val note: String = "Еженедельная инвентаризация"
)

@Entity(
    tableName = "brand_inventory_snapshots",
    primaryKeys = ["sessionId", "brandId"],
    foreignKeys = [
        ForeignKey(entity = InventorySessionEntity::class, parentColumns = ["id"], childColumns = ["sessionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TobaccoBrandEntity::class, parentColumns = ["id"], childColumns = ["brandId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("sessionId"), Index("brandId")]
)
data class BrandInventorySnapshotEntity(
    val sessionId: Long,
    val brandId: Long,
    val actualGrams: Double,
    val previousActualGrams: Double? = null,
    val deliveryGrams: Double = 0.0,
    val calculatedConsumptionGrams: Double = 0.0,
    val actualConsumptionGrams: Double = 0.0,
    val weeklyCoefficient: Double? = null,
    val includedInAnalytics: Boolean = true,
    val periodStartAt: Long? = null,
    val periodEndAt: Long = System.currentTimeMillis()
)

data class BrandInventoryInput(
    val brandId: Long,
    val brandName: String,
    val currentCalculatedGrams: Double,
    val actualGrams: Double
)

data class BrandConsumptionAnalytics(
    val brandId: Long,
    val brandName: String,
    val densityCoefficient: Double,
    val completedPeriods: Int,
    val totalActualConsumptionGrams: Double,
    val totalCalculatedConsumptionGrams: Double,
    val lastInventoryAt: Long?
)

data class InventorySessionSummary(
    val sessionId: Long,
    val takenAt: Long,
    val note: String,
    val brandCount: Int
)
