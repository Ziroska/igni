package ru.igni.manager.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "guests",
    indices = [
        Index(value = ["phone"], unique = true),
        Index("name"),
        Index("telegram")
    ]
)
data class GuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String? = null,
    val birthday: Long? = null,
    val telegram: String? = null,
    val comment: String = "",
    val preferredStrength: Int? = null,
    val preferredBowlType: String? = null,
    val preferredDescriptors: String = "",
    val dislikedFlavors: String = "",
    val vipStatus: String = "STANDARD",
    val firstVisitAt: Long = System.currentTimeMillis(),
    val lastVisitAt: Long? = null,
    val visitCount: Int = 0,
    val totalHookahs: Int = 0,
    val favoriteBrands: String = "",
    val favoriteFlavors: String = "",
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "visits",
    foreignKeys = [
        ForeignKey(
            entity = GuestEntity::class,
            parentColumns = ["id"],
            childColumns = ["guestId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = EmployeeEntity::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("guestId"), Index("employeeId"), Index("startedAt")]
)
data class VisitEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val guestId: Long? = null,
    val employeeId: Long? = null,
    val tableNumber: Int,
    val startedAt: Long = System.currentTimeMillis(),
    val closedAt: Long? = null,
    val status: String = "ACTIVE",
    val notes: String = ""
)

@Entity(
    tableName = "hookah_history",
    foreignKeys = [
        ForeignKey(
            entity = VisitEntity::class,
            parentColumns = ["id"],
            childColumns = ["visitId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GuestEntity::class,
            parentColumns = ["id"],
            childColumns = ["guestId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = EmployeeEntity::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("visitId"), Index("guestId"), Index("employeeId"), Index("startedAt")]
)
data class HookahHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val visitId: Long,
    val guestId: Long? = null,
    val employeeId: Long? = null,
    val tableNumber: Int,
    val bowlType: String,
    val strength: Int,
    val hookahCount: Int = 1,
    val startedAt: Long = System.currentTimeMillis(),
    val closedAt: Long? = null,
    val status: String = "ACTIVE",
    val comment: String = ""
)

@Entity(
    tableName = "hookah_mix_items",
    foreignKeys = [
        ForeignKey(
            entity = HookahHistoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["hookahId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TobaccoFlavorEntity::class,
            parentColumns = ["id"],
            childColumns = ["flavorId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("hookahId"), Index("flavorId")]
)
data class HookahMixItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hookahId: Long,
    val flavorId: Long? = null,
    val brandNameSnapshot: String,
    val flavorNameSnapshot: String,
    val descriptorSnapshot: String = "",
    val percentage: Int,
    val grams: Double
)

@Entity(
    tableName = "coal_changes",
    foreignKeys = [
        ForeignKey(
            entity = HookahHistoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["hookahId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EmployeeEntity::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("hookahId"), Index("employeeId"), Index("changedAt")]
)
data class CoalChangeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hookahId: Long,
    val employeeId: Long? = null,
    val changedAt: Long = System.currentTimeMillis(),
    val sequenceNumber: Int,
    val comment: String = ""
)

@Entity(
    tableName = "action_log",
    foreignKeys = [
        ForeignKey(
            entity = EmployeeEntity::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("employeeId"), Index("createdAt"), Index("entityType", "entityId")]
)
data class ActionLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val employeeId: Long? = null,
    val actionType: String,
    val entityType: String,
    val entityId: Long? = null,
    val tableNumber: Int? = null,
    val details: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
