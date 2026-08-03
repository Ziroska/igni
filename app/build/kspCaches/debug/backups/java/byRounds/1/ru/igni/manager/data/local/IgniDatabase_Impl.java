package ru.igni.manager.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class IgniDatabase_Impl extends IgniDatabase {
  private volatile EmployeeDao _employeeDao;

  private volatile ShelfDao _shelfDao;

  private volatile HallDao _hallDao;

  private volatile CrmDao _crmDao;

  private volatile ActionLogDao _actionLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(7) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `employees` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `role` TEXT NOT NULL, `active` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tobacco_brands` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `densityCoefficient` REAL NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tobacco_flavors` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `brandId` INTEGER NOT NULL, `name` TEXT NOT NULL, `descriptor` TEXT NOT NULL, `isAvailable` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`brandId`) REFERENCES `tobacco_brands`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tobacco_flavors_brandId` ON `tobacco_flavors` (`brandId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tobacco_containers` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `flavorId` INTEGER NOT NULL, `label` TEXT NOT NULL, `remainingGrams` REAL NOT NULL, `isActive` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`flavorId`) REFERENCES `tobacco_flavors`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_tobacco_containers_flavorId` ON `tobacco_containers` (`flavorId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `stock_movements` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `flavorId` INTEGER NOT NULL, `deltaGrams` REAL NOT NULL, `movementType` TEXT NOT NULL, `note` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`flavorId`) REFERENCES `tobacco_flavors`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_stock_movements_flavorId` ON `stock_movements` (`flavorId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_stock_movements_createdAt` ON `stock_movements` (`createdAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `purchase_order_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `flavorId` INTEGER NOT NULL, `recommendedGrams` REAL NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`flavorId`) REFERENCES `tobacco_flavors`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_purchase_order_items_flavorId` ON `purchase_order_items` (`flavorId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_purchase_order_items_createdAt` ON `purchase_order_items` (`createdAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `inventory_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `takenAt` INTEGER NOT NULL, `note` TEXT NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_inventory_sessions_takenAt` ON `inventory_sessions` (`takenAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `brand_inventory_snapshots` (`sessionId` INTEGER NOT NULL, `brandId` INTEGER NOT NULL, `actualGrams` REAL NOT NULL, `previousActualGrams` REAL, `deliveryGrams` REAL NOT NULL, `calculatedConsumptionGrams` REAL NOT NULL, `actualConsumptionGrams` REAL NOT NULL, `weeklyCoefficient` REAL, `includedInAnalytics` INTEGER NOT NULL, `periodStartAt` INTEGER, `periodEndAt` INTEGER NOT NULL, PRIMARY KEY(`sessionId`, `brandId`), FOREIGN KEY(`sessionId`) REFERENCES `inventory_sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`brandId`) REFERENCES `tobacco_brands`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_brand_inventory_snapshots_sessionId` ON `brand_inventory_snapshots` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_brand_inventory_snapshots_brandId` ON `brand_inventory_snapshots` (`brandId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `active_table_sessions` (`tableNumber` INTEGER NOT NULL, `guestName` TEXT NOT NULL, `guestId` INTEGER, `bowlType` TEXT NOT NULL, `strength` INTEGER NOT NULL, `hookahCount` INTEGER NOT NULL, `mixData` TEXT NOT NULL, `startedAt` INTEGER NOT NULL, `lastCoalAt` INTEGER NOT NULL, `coalChanges` INTEGER NOT NULL, PRIMARY KEY(`tableNumber`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorite_mixes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `mixData` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `guests` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `phone` TEXT, `birthday` INTEGER, `telegram` TEXT, `comment` TEXT NOT NULL, `preferredStrength` INTEGER, `preferredBowlType` TEXT, `preferredDescriptors` TEXT NOT NULL, `dislikedFlavors` TEXT NOT NULL, `vipStatus` TEXT NOT NULL, `firstVisitAt` INTEGER NOT NULL, `lastVisitAt` INTEGER, `visitCount` INTEGER NOT NULL, `totalHookahs` INTEGER NOT NULL, `favoriteBrands` TEXT NOT NULL, `favoriteFlavors` TEXT NOT NULL, `isArchived` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_guests_phone` ON `guests` (`phone`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_guests_name` ON `guests` (`name`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_guests_telegram` ON `guests` (`telegram`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `visits` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `guestId` INTEGER, `employeeId` INTEGER, `tableNumber` INTEGER NOT NULL, `startedAt` INTEGER NOT NULL, `closedAt` INTEGER, `status` TEXT NOT NULL, `notes` TEXT NOT NULL, FOREIGN KEY(`guestId`) REFERENCES `guests`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_visits_guestId` ON `visits` (`guestId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_visits_employeeId` ON `visits` (`employeeId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_visits_startedAt` ON `visits` (`startedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `hookah_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `visitId` INTEGER NOT NULL, `guestId` INTEGER, `employeeId` INTEGER, `tableNumber` INTEGER NOT NULL, `bowlType` TEXT NOT NULL, `strength` INTEGER NOT NULL, `hookahCount` INTEGER NOT NULL, `startedAt` INTEGER NOT NULL, `closedAt` INTEGER, `status` TEXT NOT NULL, `comment` TEXT NOT NULL, FOREIGN KEY(`visitId`) REFERENCES `visits`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`guestId`) REFERENCES `guests`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_visitId` ON `hookah_history` (`visitId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_guestId` ON `hookah_history` (`guestId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_employeeId` ON `hookah_history` (`employeeId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_history_startedAt` ON `hookah_history` (`startedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `hookah_mix_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `hookahId` INTEGER NOT NULL, `flavorId` INTEGER, `brandNameSnapshot` TEXT NOT NULL, `flavorNameSnapshot` TEXT NOT NULL, `descriptorSnapshot` TEXT NOT NULL, `percentage` INTEGER NOT NULL, `grams` REAL NOT NULL, FOREIGN KEY(`hookahId`) REFERENCES `hookah_history`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`flavorId`) REFERENCES `tobacco_flavors`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_mix_items_hookahId` ON `hookah_mix_items` (`hookahId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_hookah_mix_items_flavorId` ON `hookah_mix_items` (`flavorId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `coal_changes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `hookahId` INTEGER NOT NULL, `employeeId` INTEGER, `changedAt` INTEGER NOT NULL, `sequenceNumber` INTEGER NOT NULL, `comment` TEXT NOT NULL, FOREIGN KEY(`hookahId`) REFERENCES `hookah_history`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_coal_changes_hookahId` ON `coal_changes` (`hookahId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_coal_changes_employeeId` ON `coal_changes` (`employeeId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_coal_changes_changedAt` ON `coal_changes` (`changedAt`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `action_log` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `employeeId` INTEGER, `actionType` TEXT NOT NULL, `entityType` TEXT NOT NULL, `entityId` INTEGER, `tableNumber` INTEGER, `details` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`employeeId`) REFERENCES `employees`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_action_log_employeeId` ON `action_log` (`employeeId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_action_log_createdAt` ON `action_log` (`createdAt`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_action_log_entityType_entityId` ON `action_log` (`entityType`, `entityId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '62772fcf23feacefd8787090892cc8a7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `employees`");
        db.execSQL("DROP TABLE IF EXISTS `tobacco_brands`");
        db.execSQL("DROP TABLE IF EXISTS `tobacco_flavors`");
        db.execSQL("DROP TABLE IF EXISTS `tobacco_containers`");
        db.execSQL("DROP TABLE IF EXISTS `stock_movements`");
        db.execSQL("DROP TABLE IF EXISTS `purchase_order_items`");
        db.execSQL("DROP TABLE IF EXISTS `inventory_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `brand_inventory_snapshots`");
        db.execSQL("DROP TABLE IF EXISTS `active_table_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `favorite_mixes`");
        db.execSQL("DROP TABLE IF EXISTS `guests`");
        db.execSQL("DROP TABLE IF EXISTS `visits`");
        db.execSQL("DROP TABLE IF EXISTS `hookah_history`");
        db.execSQL("DROP TABLE IF EXISTS `hookah_mix_items`");
        db.execSQL("DROP TABLE IF EXISTS `coal_changes`");
        db.execSQL("DROP TABLE IF EXISTS `action_log`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsEmployees = new HashMap<String, TableInfo.Column>(5);
        _columnsEmployees.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEmployees.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEmployees = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEmployees = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEmployees = new TableInfo("employees", _columnsEmployees, _foreignKeysEmployees, _indicesEmployees);
        final TableInfo _existingEmployees = TableInfo.read(db, "employees");
        if (!_infoEmployees.equals(_existingEmployees)) {
          return new RoomOpenHelper.ValidationResult(false, "employees(ru.igni.manager.data.local.EmployeeEntity).\n"
                  + " Expected:\n" + _infoEmployees + "\n"
                  + " Found:\n" + _existingEmployees);
        }
        final HashMap<String, TableInfo.Column> _columnsTobaccoBrands = new HashMap<String, TableInfo.Column>(4);
        _columnsTobaccoBrands.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoBrands.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoBrands.put("densityCoefficient", new TableInfo.Column("densityCoefficient", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoBrands.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTobaccoBrands = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTobaccoBrands = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTobaccoBrands = new TableInfo("tobacco_brands", _columnsTobaccoBrands, _foreignKeysTobaccoBrands, _indicesTobaccoBrands);
        final TableInfo _existingTobaccoBrands = TableInfo.read(db, "tobacco_brands");
        if (!_infoTobaccoBrands.equals(_existingTobaccoBrands)) {
          return new RoomOpenHelper.ValidationResult(false, "tobacco_brands(ru.igni.manager.data.local.TobaccoBrandEntity).\n"
                  + " Expected:\n" + _infoTobaccoBrands + "\n"
                  + " Found:\n" + _existingTobaccoBrands);
        }
        final HashMap<String, TableInfo.Column> _columnsTobaccoFlavors = new HashMap<String, TableInfo.Column>(6);
        _columnsTobaccoFlavors.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoFlavors.put("brandId", new TableInfo.Column("brandId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoFlavors.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoFlavors.put("descriptor", new TableInfo.Column("descriptor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoFlavors.put("isAvailable", new TableInfo.Column("isAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoFlavors.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTobaccoFlavors = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTobaccoFlavors.add(new TableInfo.ForeignKey("tobacco_brands", "CASCADE", "NO ACTION", Arrays.asList("brandId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTobaccoFlavors = new HashSet<TableInfo.Index>(1);
        _indicesTobaccoFlavors.add(new TableInfo.Index("index_tobacco_flavors_brandId", false, Arrays.asList("brandId"), Arrays.asList("ASC")));
        final TableInfo _infoTobaccoFlavors = new TableInfo("tobacco_flavors", _columnsTobaccoFlavors, _foreignKeysTobaccoFlavors, _indicesTobaccoFlavors);
        final TableInfo _existingTobaccoFlavors = TableInfo.read(db, "tobacco_flavors");
        if (!_infoTobaccoFlavors.equals(_existingTobaccoFlavors)) {
          return new RoomOpenHelper.ValidationResult(false, "tobacco_flavors(ru.igni.manager.data.local.TobaccoFlavorEntity).\n"
                  + " Expected:\n" + _infoTobaccoFlavors + "\n"
                  + " Found:\n" + _existingTobaccoFlavors);
        }
        final HashMap<String, TableInfo.Column> _columnsTobaccoContainers = new HashMap<String, TableInfo.Column>(6);
        _columnsTobaccoContainers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoContainers.put("flavorId", new TableInfo.Column("flavorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoContainers.put("label", new TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoContainers.put("remainingGrams", new TableInfo.Column("remainingGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoContainers.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTobaccoContainers.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTobaccoContainers = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTobaccoContainers.add(new TableInfo.ForeignKey("tobacco_flavors", "CASCADE", "NO ACTION", Arrays.asList("flavorId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTobaccoContainers = new HashSet<TableInfo.Index>(1);
        _indicesTobaccoContainers.add(new TableInfo.Index("index_tobacco_containers_flavorId", false, Arrays.asList("flavorId"), Arrays.asList("ASC")));
        final TableInfo _infoTobaccoContainers = new TableInfo("tobacco_containers", _columnsTobaccoContainers, _foreignKeysTobaccoContainers, _indicesTobaccoContainers);
        final TableInfo _existingTobaccoContainers = TableInfo.read(db, "tobacco_containers");
        if (!_infoTobaccoContainers.equals(_existingTobaccoContainers)) {
          return new RoomOpenHelper.ValidationResult(false, "tobacco_containers(ru.igni.manager.data.local.TobaccoContainerEntity).\n"
                  + " Expected:\n" + _infoTobaccoContainers + "\n"
                  + " Found:\n" + _existingTobaccoContainers);
        }
        final HashMap<String, TableInfo.Column> _columnsStockMovements = new HashMap<String, TableInfo.Column>(6);
        _columnsStockMovements.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockMovements.put("flavorId", new TableInfo.Column("flavorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockMovements.put("deltaGrams", new TableInfo.Column("deltaGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockMovements.put("movementType", new TableInfo.Column("movementType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockMovements.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStockMovements.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStockMovements = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysStockMovements.add(new TableInfo.ForeignKey("tobacco_flavors", "CASCADE", "NO ACTION", Arrays.asList("flavorId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesStockMovements = new HashSet<TableInfo.Index>(2);
        _indicesStockMovements.add(new TableInfo.Index("index_stock_movements_flavorId", false, Arrays.asList("flavorId"), Arrays.asList("ASC")));
        _indicesStockMovements.add(new TableInfo.Index("index_stock_movements_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        final TableInfo _infoStockMovements = new TableInfo("stock_movements", _columnsStockMovements, _foreignKeysStockMovements, _indicesStockMovements);
        final TableInfo _existingStockMovements = TableInfo.read(db, "stock_movements");
        if (!_infoStockMovements.equals(_existingStockMovements)) {
          return new RoomOpenHelper.ValidationResult(false, "stock_movements(ru.igni.manager.data.local.StockMovementEntity).\n"
                  + " Expected:\n" + _infoStockMovements + "\n"
                  + " Found:\n" + _existingStockMovements);
        }
        final HashMap<String, TableInfo.Column> _columnsPurchaseOrderItems = new HashMap<String, TableInfo.Column>(4);
        _columnsPurchaseOrderItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchaseOrderItems.put("flavorId", new TableInfo.Column("flavorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchaseOrderItems.put("recommendedGrams", new TableInfo.Column("recommendedGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPurchaseOrderItems.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPurchaseOrderItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPurchaseOrderItems.add(new TableInfo.ForeignKey("tobacco_flavors", "CASCADE", "NO ACTION", Arrays.asList("flavorId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPurchaseOrderItems = new HashSet<TableInfo.Index>(2);
        _indicesPurchaseOrderItems.add(new TableInfo.Index("index_purchase_order_items_flavorId", true, Arrays.asList("flavorId"), Arrays.asList("ASC")));
        _indicesPurchaseOrderItems.add(new TableInfo.Index("index_purchase_order_items_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        final TableInfo _infoPurchaseOrderItems = new TableInfo("purchase_order_items", _columnsPurchaseOrderItems, _foreignKeysPurchaseOrderItems, _indicesPurchaseOrderItems);
        final TableInfo _existingPurchaseOrderItems = TableInfo.read(db, "purchase_order_items");
        if (!_infoPurchaseOrderItems.equals(_existingPurchaseOrderItems)) {
          return new RoomOpenHelper.ValidationResult(false, "purchase_order_items(ru.igni.manager.data.local.PurchaseOrderItemEntity).\n"
                  + " Expected:\n" + _infoPurchaseOrderItems + "\n"
                  + " Found:\n" + _existingPurchaseOrderItems);
        }
        final HashMap<String, TableInfo.Column> _columnsInventorySessions = new HashMap<String, TableInfo.Column>(3);
        _columnsInventorySessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventorySessions.put("takenAt", new TableInfo.Column("takenAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInventorySessions.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInventorySessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInventorySessions = new HashSet<TableInfo.Index>(1);
        _indicesInventorySessions.add(new TableInfo.Index("index_inventory_sessions_takenAt", false, Arrays.asList("takenAt"), Arrays.asList("ASC")));
        final TableInfo _infoInventorySessions = new TableInfo("inventory_sessions", _columnsInventorySessions, _foreignKeysInventorySessions, _indicesInventorySessions);
        final TableInfo _existingInventorySessions = TableInfo.read(db, "inventory_sessions");
        if (!_infoInventorySessions.equals(_existingInventorySessions)) {
          return new RoomOpenHelper.ValidationResult(false, "inventory_sessions(ru.igni.manager.data.local.InventorySessionEntity).\n"
                  + " Expected:\n" + _infoInventorySessions + "\n"
                  + " Found:\n" + _existingInventorySessions);
        }
        final HashMap<String, TableInfo.Column> _columnsBrandInventorySnapshots = new HashMap<String, TableInfo.Column>(11);
        _columnsBrandInventorySnapshots.put("sessionId", new TableInfo.Column("sessionId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("brandId", new TableInfo.Column("brandId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("actualGrams", new TableInfo.Column("actualGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("previousActualGrams", new TableInfo.Column("previousActualGrams", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("deliveryGrams", new TableInfo.Column("deliveryGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("calculatedConsumptionGrams", new TableInfo.Column("calculatedConsumptionGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("actualConsumptionGrams", new TableInfo.Column("actualConsumptionGrams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("weeklyCoefficient", new TableInfo.Column("weeklyCoefficient", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("includedInAnalytics", new TableInfo.Column("includedInAnalytics", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("periodStartAt", new TableInfo.Column("periodStartAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBrandInventorySnapshots.put("periodEndAt", new TableInfo.Column("periodEndAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBrandInventorySnapshots = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysBrandInventorySnapshots.add(new TableInfo.ForeignKey("inventory_sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        _foreignKeysBrandInventorySnapshots.add(new TableInfo.ForeignKey("tobacco_brands", "CASCADE", "NO ACTION", Arrays.asList("brandId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBrandInventorySnapshots = new HashSet<TableInfo.Index>(2);
        _indicesBrandInventorySnapshots.add(new TableInfo.Index("index_brand_inventory_snapshots_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        _indicesBrandInventorySnapshots.add(new TableInfo.Index("index_brand_inventory_snapshots_brandId", false, Arrays.asList("brandId"), Arrays.asList("ASC")));
        final TableInfo _infoBrandInventorySnapshots = new TableInfo("brand_inventory_snapshots", _columnsBrandInventorySnapshots, _foreignKeysBrandInventorySnapshots, _indicesBrandInventorySnapshots);
        final TableInfo _existingBrandInventorySnapshots = TableInfo.read(db, "brand_inventory_snapshots");
        if (!_infoBrandInventorySnapshots.equals(_existingBrandInventorySnapshots)) {
          return new RoomOpenHelper.ValidationResult(false, "brand_inventory_snapshots(ru.igni.manager.data.local.BrandInventorySnapshotEntity).\n"
                  + " Expected:\n" + _infoBrandInventorySnapshots + "\n"
                  + " Found:\n" + _existingBrandInventorySnapshots);
        }
        final HashMap<String, TableInfo.Column> _columnsActiveTableSessions = new HashMap<String, TableInfo.Column>(10);
        _columnsActiveTableSessions.put("tableNumber", new TableInfo.Column("tableNumber", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("guestName", new TableInfo.Column("guestName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("guestId", new TableInfo.Column("guestId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("bowlType", new TableInfo.Column("bowlType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("strength", new TableInfo.Column("strength", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("hookahCount", new TableInfo.Column("hookahCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("mixData", new TableInfo.Column("mixData", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("startedAt", new TableInfo.Column("startedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("lastCoalAt", new TableInfo.Column("lastCoalAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActiveTableSessions.put("coalChanges", new TableInfo.Column("coalChanges", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysActiveTableSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesActiveTableSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoActiveTableSessions = new TableInfo("active_table_sessions", _columnsActiveTableSessions, _foreignKeysActiveTableSessions, _indicesActiveTableSessions);
        final TableInfo _existingActiveTableSessions = TableInfo.read(db, "active_table_sessions");
        if (!_infoActiveTableSessions.equals(_existingActiveTableSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "active_table_sessions(ru.igni.manager.data.local.ActiveTableSessionEntity).\n"
                  + " Expected:\n" + _infoActiveTableSessions + "\n"
                  + " Found:\n" + _existingActiveTableSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsFavoriteMixes = new HashMap<String, TableInfo.Column>(4);
        _columnsFavoriteMixes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteMixes.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteMixes.put("mixData", new TableInfo.Column("mixData", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavoriteMixes.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavoriteMixes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavoriteMixes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavoriteMixes = new TableInfo("favorite_mixes", _columnsFavoriteMixes, _foreignKeysFavoriteMixes, _indicesFavoriteMixes);
        final TableInfo _existingFavoriteMixes = TableInfo.read(db, "favorite_mixes");
        if (!_infoFavoriteMixes.equals(_existingFavoriteMixes)) {
          return new RoomOpenHelper.ValidationResult(false, "favorite_mixes(ru.igni.manager.data.local.FavoriteMixEntity).\n"
                  + " Expected:\n" + _infoFavoriteMixes + "\n"
                  + " Found:\n" + _existingFavoriteMixes);
        }
        final HashMap<String, TableInfo.Column> _columnsGuests = new HashMap<String, TableInfo.Column>(20);
        _columnsGuests.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("birthday", new TableInfo.Column("birthday", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("telegram", new TableInfo.Column("telegram", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("comment", new TableInfo.Column("comment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("preferredStrength", new TableInfo.Column("preferredStrength", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("preferredBowlType", new TableInfo.Column("preferredBowlType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("preferredDescriptors", new TableInfo.Column("preferredDescriptors", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("dislikedFlavors", new TableInfo.Column("dislikedFlavors", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("vipStatus", new TableInfo.Column("vipStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("firstVisitAt", new TableInfo.Column("firstVisitAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("lastVisitAt", new TableInfo.Column("lastVisitAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("visitCount", new TableInfo.Column("visitCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("totalHookahs", new TableInfo.Column("totalHookahs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("favoriteBrands", new TableInfo.Column("favoriteBrands", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("favoriteFlavors", new TableInfo.Column("favoriteFlavors", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("isArchived", new TableInfo.Column("isArchived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGuests.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGuests = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGuests = new HashSet<TableInfo.Index>(3);
        _indicesGuests.add(new TableInfo.Index("index_guests_phone", true, Arrays.asList("phone"), Arrays.asList("ASC")));
        _indicesGuests.add(new TableInfo.Index("index_guests_name", false, Arrays.asList("name"), Arrays.asList("ASC")));
        _indicesGuests.add(new TableInfo.Index("index_guests_telegram", false, Arrays.asList("telegram"), Arrays.asList("ASC")));
        final TableInfo _infoGuests = new TableInfo("guests", _columnsGuests, _foreignKeysGuests, _indicesGuests);
        final TableInfo _existingGuests = TableInfo.read(db, "guests");
        if (!_infoGuests.equals(_existingGuests)) {
          return new RoomOpenHelper.ValidationResult(false, "guests(ru.igni.manager.data.local.GuestEntity).\n"
                  + " Expected:\n" + _infoGuests + "\n"
                  + " Found:\n" + _existingGuests);
        }
        final HashMap<String, TableInfo.Column> _columnsVisits = new HashMap<String, TableInfo.Column>(8);
        _columnsVisits.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVisits.put("guestId", new TableInfo.Column("guestId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVisits.put("employeeId", new TableInfo.Column("employeeId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVisits.put("tableNumber", new TableInfo.Column("tableNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVisits.put("startedAt", new TableInfo.Column("startedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVisits.put("closedAt", new TableInfo.Column("closedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVisits.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVisits.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVisits = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysVisits.add(new TableInfo.ForeignKey("guests", "SET NULL", "NO ACTION", Arrays.asList("guestId"), Arrays.asList("id")));
        _foreignKeysVisits.add(new TableInfo.ForeignKey("employees", "SET NULL", "NO ACTION", Arrays.asList("employeeId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesVisits = new HashSet<TableInfo.Index>(3);
        _indicesVisits.add(new TableInfo.Index("index_visits_guestId", false, Arrays.asList("guestId"), Arrays.asList("ASC")));
        _indicesVisits.add(new TableInfo.Index("index_visits_employeeId", false, Arrays.asList("employeeId"), Arrays.asList("ASC")));
        _indicesVisits.add(new TableInfo.Index("index_visits_startedAt", false, Arrays.asList("startedAt"), Arrays.asList("ASC")));
        final TableInfo _infoVisits = new TableInfo("visits", _columnsVisits, _foreignKeysVisits, _indicesVisits);
        final TableInfo _existingVisits = TableInfo.read(db, "visits");
        if (!_infoVisits.equals(_existingVisits)) {
          return new RoomOpenHelper.ValidationResult(false, "visits(ru.igni.manager.data.local.VisitEntity).\n"
                  + " Expected:\n" + _infoVisits + "\n"
                  + " Found:\n" + _existingVisits);
        }
        final HashMap<String, TableInfo.Column> _columnsHookahHistory = new HashMap<String, TableInfo.Column>(12);
        _columnsHookahHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("visitId", new TableInfo.Column("visitId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("guestId", new TableInfo.Column("guestId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("employeeId", new TableInfo.Column("employeeId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("tableNumber", new TableInfo.Column("tableNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("bowlType", new TableInfo.Column("bowlType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("strength", new TableInfo.Column("strength", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("hookahCount", new TableInfo.Column("hookahCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("startedAt", new TableInfo.Column("startedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("closedAt", new TableInfo.Column("closedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahHistory.put("comment", new TableInfo.Column("comment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHookahHistory = new HashSet<TableInfo.ForeignKey>(3);
        _foreignKeysHookahHistory.add(new TableInfo.ForeignKey("visits", "CASCADE", "NO ACTION", Arrays.asList("visitId"), Arrays.asList("id")));
        _foreignKeysHookahHistory.add(new TableInfo.ForeignKey("guests", "SET NULL", "NO ACTION", Arrays.asList("guestId"), Arrays.asList("id")));
        _foreignKeysHookahHistory.add(new TableInfo.ForeignKey("employees", "SET NULL", "NO ACTION", Arrays.asList("employeeId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesHookahHistory = new HashSet<TableInfo.Index>(4);
        _indicesHookahHistory.add(new TableInfo.Index("index_hookah_history_visitId", false, Arrays.asList("visitId"), Arrays.asList("ASC")));
        _indicesHookahHistory.add(new TableInfo.Index("index_hookah_history_guestId", false, Arrays.asList("guestId"), Arrays.asList("ASC")));
        _indicesHookahHistory.add(new TableInfo.Index("index_hookah_history_employeeId", false, Arrays.asList("employeeId"), Arrays.asList("ASC")));
        _indicesHookahHistory.add(new TableInfo.Index("index_hookah_history_startedAt", false, Arrays.asList("startedAt"), Arrays.asList("ASC")));
        final TableInfo _infoHookahHistory = new TableInfo("hookah_history", _columnsHookahHistory, _foreignKeysHookahHistory, _indicesHookahHistory);
        final TableInfo _existingHookahHistory = TableInfo.read(db, "hookah_history");
        if (!_infoHookahHistory.equals(_existingHookahHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "hookah_history(ru.igni.manager.data.local.HookahHistoryEntity).\n"
                  + " Expected:\n" + _infoHookahHistory + "\n"
                  + " Found:\n" + _existingHookahHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsHookahMixItems = new HashMap<String, TableInfo.Column>(8);
        _columnsHookahMixItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahMixItems.put("hookahId", new TableInfo.Column("hookahId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahMixItems.put("flavorId", new TableInfo.Column("flavorId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahMixItems.put("brandNameSnapshot", new TableInfo.Column("brandNameSnapshot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahMixItems.put("flavorNameSnapshot", new TableInfo.Column("flavorNameSnapshot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahMixItems.put("descriptorSnapshot", new TableInfo.Column("descriptorSnapshot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahMixItems.put("percentage", new TableInfo.Column("percentage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHookahMixItems.put("grams", new TableInfo.Column("grams", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHookahMixItems = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysHookahMixItems.add(new TableInfo.ForeignKey("hookah_history", "CASCADE", "NO ACTION", Arrays.asList("hookahId"), Arrays.asList("id")));
        _foreignKeysHookahMixItems.add(new TableInfo.ForeignKey("tobacco_flavors", "SET NULL", "NO ACTION", Arrays.asList("flavorId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesHookahMixItems = new HashSet<TableInfo.Index>(2);
        _indicesHookahMixItems.add(new TableInfo.Index("index_hookah_mix_items_hookahId", false, Arrays.asList("hookahId"), Arrays.asList("ASC")));
        _indicesHookahMixItems.add(new TableInfo.Index("index_hookah_mix_items_flavorId", false, Arrays.asList("flavorId"), Arrays.asList("ASC")));
        final TableInfo _infoHookahMixItems = new TableInfo("hookah_mix_items", _columnsHookahMixItems, _foreignKeysHookahMixItems, _indicesHookahMixItems);
        final TableInfo _existingHookahMixItems = TableInfo.read(db, "hookah_mix_items");
        if (!_infoHookahMixItems.equals(_existingHookahMixItems)) {
          return new RoomOpenHelper.ValidationResult(false, "hookah_mix_items(ru.igni.manager.data.local.HookahMixItemEntity).\n"
                  + " Expected:\n" + _infoHookahMixItems + "\n"
                  + " Found:\n" + _existingHookahMixItems);
        }
        final HashMap<String, TableInfo.Column> _columnsCoalChanges = new HashMap<String, TableInfo.Column>(6);
        _columnsCoalChanges.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoalChanges.put("hookahId", new TableInfo.Column("hookahId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoalChanges.put("employeeId", new TableInfo.Column("employeeId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoalChanges.put("changedAt", new TableInfo.Column("changedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoalChanges.put("sequenceNumber", new TableInfo.Column("sequenceNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCoalChanges.put("comment", new TableInfo.Column("comment", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCoalChanges = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysCoalChanges.add(new TableInfo.ForeignKey("hookah_history", "CASCADE", "NO ACTION", Arrays.asList("hookahId"), Arrays.asList("id")));
        _foreignKeysCoalChanges.add(new TableInfo.ForeignKey("employees", "SET NULL", "NO ACTION", Arrays.asList("employeeId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCoalChanges = new HashSet<TableInfo.Index>(3);
        _indicesCoalChanges.add(new TableInfo.Index("index_coal_changes_hookahId", false, Arrays.asList("hookahId"), Arrays.asList("ASC")));
        _indicesCoalChanges.add(new TableInfo.Index("index_coal_changes_employeeId", false, Arrays.asList("employeeId"), Arrays.asList("ASC")));
        _indicesCoalChanges.add(new TableInfo.Index("index_coal_changes_changedAt", false, Arrays.asList("changedAt"), Arrays.asList("ASC")));
        final TableInfo _infoCoalChanges = new TableInfo("coal_changes", _columnsCoalChanges, _foreignKeysCoalChanges, _indicesCoalChanges);
        final TableInfo _existingCoalChanges = TableInfo.read(db, "coal_changes");
        if (!_infoCoalChanges.equals(_existingCoalChanges)) {
          return new RoomOpenHelper.ValidationResult(false, "coal_changes(ru.igni.manager.data.local.CoalChangeEntity).\n"
                  + " Expected:\n" + _infoCoalChanges + "\n"
                  + " Found:\n" + _existingCoalChanges);
        }
        final HashMap<String, TableInfo.Column> _columnsActionLog = new HashMap<String, TableInfo.Column>(8);
        _columnsActionLog.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActionLog.put("employeeId", new TableInfo.Column("employeeId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActionLog.put("actionType", new TableInfo.Column("actionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActionLog.put("entityType", new TableInfo.Column("entityType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActionLog.put("entityId", new TableInfo.Column("entityId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActionLog.put("tableNumber", new TableInfo.Column("tableNumber", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActionLog.put("details", new TableInfo.Column("details", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsActionLog.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysActionLog = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysActionLog.add(new TableInfo.ForeignKey("employees", "SET NULL", "NO ACTION", Arrays.asList("employeeId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesActionLog = new HashSet<TableInfo.Index>(3);
        _indicesActionLog.add(new TableInfo.Index("index_action_log_employeeId", false, Arrays.asList("employeeId"), Arrays.asList("ASC")));
        _indicesActionLog.add(new TableInfo.Index("index_action_log_createdAt", false, Arrays.asList("createdAt"), Arrays.asList("ASC")));
        _indicesActionLog.add(new TableInfo.Index("index_action_log_entityType_entityId", false, Arrays.asList("entityType", "entityId"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoActionLog = new TableInfo("action_log", _columnsActionLog, _foreignKeysActionLog, _indicesActionLog);
        final TableInfo _existingActionLog = TableInfo.read(db, "action_log");
        if (!_infoActionLog.equals(_existingActionLog)) {
          return new RoomOpenHelper.ValidationResult(false, "action_log(ru.igni.manager.data.local.ActionLogEntity).\n"
                  + " Expected:\n" + _infoActionLog + "\n"
                  + " Found:\n" + _existingActionLog);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "62772fcf23feacefd8787090892cc8a7", "67a8e0a419940c80354cc80b49bb24d3");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "employees","tobacco_brands","tobacco_flavors","tobacco_containers","stock_movements","purchase_order_items","inventory_sessions","brand_inventory_snapshots","active_table_sessions","favorite_mixes","guests","visits","hookah_history","hookah_mix_items","coal_changes","action_log");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `employees`");
      _db.execSQL("DELETE FROM `tobacco_brands`");
      _db.execSQL("DELETE FROM `tobacco_flavors`");
      _db.execSQL("DELETE FROM `tobacco_containers`");
      _db.execSQL("DELETE FROM `stock_movements`");
      _db.execSQL("DELETE FROM `purchase_order_items`");
      _db.execSQL("DELETE FROM `inventory_sessions`");
      _db.execSQL("DELETE FROM `brand_inventory_snapshots`");
      _db.execSQL("DELETE FROM `active_table_sessions`");
      _db.execSQL("DELETE FROM `favorite_mixes`");
      _db.execSQL("DELETE FROM `guests`");
      _db.execSQL("DELETE FROM `visits`");
      _db.execSQL("DELETE FROM `hookah_history`");
      _db.execSQL("DELETE FROM `hookah_mix_items`");
      _db.execSQL("DELETE FROM `coal_changes`");
      _db.execSQL("DELETE FROM `action_log`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(EmployeeDao.class, EmployeeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ShelfDao.class, ShelfDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HallDao.class, HallDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CrmDao.class, CrmDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ActionLogDao.class, ActionLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public EmployeeDao employeeDao() {
    if (_employeeDao != null) {
      return _employeeDao;
    } else {
      synchronized(this) {
        if(_employeeDao == null) {
          _employeeDao = new EmployeeDao_Impl(this);
        }
        return _employeeDao;
      }
    }
  }

  @Override
  public ShelfDao shelfDao() {
    if (_shelfDao != null) {
      return _shelfDao;
    } else {
      synchronized(this) {
        if(_shelfDao == null) {
          _shelfDao = new ShelfDao_Impl(this);
        }
        return _shelfDao;
      }
    }
  }

  @Override
  public HallDao hallDao() {
    if (_hallDao != null) {
      return _hallDao;
    } else {
      synchronized(this) {
        if(_hallDao == null) {
          _hallDao = new HallDao_Impl(this);
        }
        return _hallDao;
      }
    }
  }

  @Override
  public CrmDao crmDao() {
    if (_crmDao != null) {
      return _crmDao;
    } else {
      synchronized(this) {
        if(_crmDao == null) {
          _crmDao = new CrmDao_Impl(this);
        }
        return _crmDao;
      }
    }
  }

  @Override
  public ActionLogDao actionLogDao() {
    if (_actionLogDao != null) {
      return _actionLogDao;
    } else {
      synchronized(this) {
        if(_actionLogDao == null) {
          _actionLogDao = new ActionLogDao_Impl(this);
        }
        return _actionLogDao;
      }
    }
  }
}
