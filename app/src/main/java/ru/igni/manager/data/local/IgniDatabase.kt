package ru.igni.manager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        EmployeeEntity::class,
        TobaccoBrandEntity::class,
        TobaccoFlavorEntity::class,
        TobaccoContainerEntity::class,
        StockMovementEntity::class,
        PurchaseOrderItemEntity::class,
        InventorySessionEntity::class,
        BrandInventorySnapshotEntity::class,
        ActiveTableSessionEntity::class,
        FavoriteMixEntity::class,
        GuestEntity::class,
        VisitEntity::class,
        HookahHistoryEntity::class,
        HookahMixItemEntity::class,
        CoalChangeEntity::class,
        ActionLogEntity::class
    ],
    version = 7,
    exportSchema = true
)
abstract class IgniDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
    abstract fun shelfDao(): ShelfDao
    abstract fun hallDao(): HallDao
    abstract fun crmDao(): CrmDao
    abstract fun actionLogDao(): ActionLogDao

    companion object {
        fun create(context: Context): IgniDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                IgniDatabase::class.java,
                "igni_manager.db"
            )
                .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7)
                .fallbackToDestructiveMigration()
                .build()

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `active_table_sessions` (
                        `tableNumber` INTEGER NOT NULL,
                        `guestName` TEXT NOT NULL,
                        `bowlType` TEXT NOT NULL,
                        `strength` INTEGER NOT NULL,
                        `hookahCount` INTEGER NOT NULL,
                        `mixData` TEXT NOT NULL,
                        `startedAt` INTEGER NOT NULL,
                        `lastCoalAt` INTEGER NOT NULL,
                        `coalChanges` INTEGER NOT NULL,
                        PRIMARY KEY(`tableNumber`)
                    )""".trimIndent()
                )
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `favorite_mixes` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `mixData` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL
                    )""".trimIndent()
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `guests` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `phone` TEXT,
                        `birthday` INTEGER,
                        `telegram` TEXT,
                        `comment` TEXT NOT NULL,
                        `preferredStrength` INTEGER,
                        `preferredBowlType` TEXT,
                        `preferredDescriptors` TEXT NOT NULL,
                        `dislikedFlavors` TEXT NOT NULL,
                        `vipStatus` TEXT NOT NULL,
                        `firstVisitAt` INTEGER NOT NULL,
                        `lastVisitAt` INTEGER,
                        `visitCount` INTEGER NOT NULL,
                        `isArchived` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL
                    )""".trimIndent()
                )
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_guests_phone` ON `guests` (`phone`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_guests_name` ON `guests` (`name`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_guests_telegram` ON `guests` (`telegram`)")

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `visits` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `guestId` INTEGER,
                        `employeeId` INTEGER,
                        `tableNumber` INTEGER NOT NULL,
                        `startedAt` INTEGER NOT NULL,
                        `closedAt` INTEGER,
                        `status` TEXT NOT NULL,
                        `notes` TEXT NOT NULL,
                        FOREIGN KEY(`guestId`) REFERENCES `guests`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL,
                        FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_visits_guestId` ON `visits` (`guestId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_visits_employeeId` ON `visits` (`employeeId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_visits_startedAt` ON `visits` (`startedAt`)")

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `hookah_history` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `visitId` INTEGER NOT NULL,
                        `guestId` INTEGER,
                        `employeeId` INTEGER,
                        `tableNumber` INTEGER NOT NULL,
                        `bowlType` TEXT NOT NULL,
                        `strength` INTEGER NOT NULL,
                        `hookahCount` INTEGER NOT NULL,
                        `startedAt` INTEGER NOT NULL,
                        `closedAt` INTEGER,
                        `status` TEXT NOT NULL,
                        `comment` TEXT NOT NULL,
                        FOREIGN KEY(`visitId`) REFERENCES `visits`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(`guestId`) REFERENCES `guests`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL,
                        FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_visitId` ON `hookah_history` (`visitId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_guestId` ON `hookah_history` (`guestId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_employeeId` ON `hookah_history` (`employeeId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_startedAt` ON `hookah_history` (`startedAt`)")

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `hookah_mix_items` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `hookahId` INTEGER NOT NULL,
                        `flavorId` INTEGER,
                        `brandNameSnapshot` TEXT NOT NULL,
                        `flavorNameSnapshot` TEXT NOT NULL,
                        `descriptorSnapshot` TEXT NOT NULL,
                        `percentage` INTEGER NOT NULL,
                        `grams` REAL NOT NULL,
                        FOREIGN KEY(`hookahId`) REFERENCES `hookah_history`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(`flavorId`) REFERENCES `tobacco_flavors`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_mix_items_hookahId` ON `hookah_mix_items` (`hookahId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_mix_items_flavorId` ON `hookah_mix_items` (`flavorId`)")

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `coal_changes` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `hookahId` INTEGER NOT NULL,
                        `employeeId` INTEGER,
                        `changedAt` INTEGER NOT NULL,
                        `sequenceNumber` INTEGER NOT NULL,
                        `comment` TEXT NOT NULL,
                        FOREIGN KEY(`hookahId`) REFERENCES `hookah_history`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                        FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_coal_changes_hookahId` ON `coal_changes` (`hookahId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_coal_changes_employeeId` ON `coal_changes` (`employeeId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_coal_changes_changedAt` ON `coal_changes` (`changedAt`)")

                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `action_log` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `employeeId` INTEGER,
                        `actionType` TEXT NOT NULL,
                        `entityType` TEXT NOT NULL,
                        `entityId` INTEGER,
                        `tableNumber` INTEGER,
                        `details` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                    )""".trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_action_log_employeeId` ON `action_log` (`employeeId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_action_log_createdAt` ON `action_log` (`createdAt`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_action_log_entityType_entityId` ON `action_log` (`entityType`, `entityId`)")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""CREATE TABLE IF NOT EXISTS `stock_movements` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `flavorId` INTEGER NOT NULL,
                    `deltaGrams` REAL NOT NULL,
                    `movementType` TEXT NOT NULL,
                    `note` TEXT NOT NULL,
                    `createdAt` INTEGER NOT NULL,
                    FOREIGN KEY(`flavorId`) REFERENCES `tobacco_flavors`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )""".trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_stock_movements_flavorId` ON `stock_movements` (`flavorId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_stock_movements_createdAt` ON `stock_movements` (`createdAt`)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS `purchase_order_items` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `flavorId` INTEGER NOT NULL,
                    `recommendedGrams` REAL NOT NULL,
                    `createdAt` INTEGER NOT NULL,
                    FOREIGN KEY(`flavorId`) REFERENCES `tobacco_flavors`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )""".trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_purchase_order_items_flavorId` ON `purchase_order_items` (`flavorId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_purchase_order_items_createdAt` ON `purchase_order_items` (`createdAt`)")
            }
        }


        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""CREATE TABLE IF NOT EXISTS `inventory_sessions` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `takenAt` INTEGER NOT NULL,
                    `note` TEXT NOT NULL
                )""".trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_inventory_sessions_takenAt` ON `inventory_sessions` (`takenAt`)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS `brand_inventory_snapshots` (
                    `sessionId` INTEGER NOT NULL,
                    `brandId` INTEGER NOT NULL,
                    `actualGrams` REAL NOT NULL,
                    `previousActualGrams` REAL,
                    `deliveryGrams` REAL NOT NULL,
                    `calculatedConsumptionGrams` REAL NOT NULL,
                    `actualConsumptionGrams` REAL NOT NULL,
                    `weeklyCoefficient` REAL,
                    `includedInAnalytics` INTEGER NOT NULL,
                    `periodStartAt` INTEGER,
                    `periodEndAt` INTEGER NOT NULL,
                    PRIMARY KEY(`sessionId`, `brandId`),
                    FOREIGN KEY(`sessionId`) REFERENCES `inventory_sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                    FOREIGN KEY(`brandId`) REFERENCES `tobacco_brands`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )""".trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_brand_inventory_snapshots_sessionId` ON `brand_inventory_snapshots` (`sessionId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_brand_inventory_snapshots_brandId` ON `brand_inventory_snapshots` (`brandId`)")
            }
        }


        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `active_table_sessions` ADD COLUMN `guestId` INTEGER")
                db.execSQL("ALTER TABLE `guests` ADD COLUMN `totalHookahs` INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE `guests` ADD COLUMN `favoriteBrands` TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE `guests` ADD COLUMN `favoriteFlavors` TEXT NOT NULL DEFAULT ''")
            }
        }

    }
}
