package ru.igni.manager.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ShelfDao_Impl implements ShelfDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TobaccoBrandEntity> __insertionAdapterOfTobaccoBrandEntity;

  private final EntityInsertionAdapter<TobaccoFlavorEntity> __insertionAdapterOfTobaccoFlavorEntity;

  private final EntityInsertionAdapter<TobaccoContainerEntity> __insertionAdapterOfTobaccoContainerEntity;

  private final EntityInsertionAdapter<PurchaseOrderItemEntity> __insertionAdapterOfPurchaseOrderItemEntity;

  private final EntityInsertionAdapter<StockMovementEntity> __insertionAdapterOfStockMovementEntity;

  private final EntityInsertionAdapter<InventorySessionEntity> __insertionAdapterOfInventorySessionEntity;

  private final EntityInsertionAdapter<BrandInventorySnapshotEntity> __insertionAdapterOfBrandInventorySnapshotEntity;

  private final EntityDeletionOrUpdateAdapter<TobaccoFlavorEntity> __updateAdapterOfTobaccoFlavorEntity;

  private final EntityDeletionOrUpdateAdapter<TobaccoContainerEntity> __updateAdapterOfTobaccoContainerEntity;

  private final SharedSQLiteStatement __preparedStmtOfSetFlavorAvailability;

  private final SharedSQLiteStatement __preparedStmtOfUpdateContainerStock;

  private final SharedSQLiteStatement __preparedStmtOfRemoveOrderItem;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBrandCoefficient;

  public ShelfDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTobaccoBrandEntity = new EntityInsertionAdapter<TobaccoBrandEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `tobacco_brands` (`id`,`name`,`densityCoefficient`,`createdAt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TobaccoBrandEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getDensityCoefficient());
        statement.bindLong(4, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfTobaccoFlavorEntity = new EntityInsertionAdapter<TobaccoFlavorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `tobacco_flavors` (`id`,`brandId`,`name`,`descriptor`,`isAvailable`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TobaccoFlavorEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBrandId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getDescriptor());
        final int _tmp = entity.isAvailable() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfTobaccoContainerEntity = new EntityInsertionAdapter<TobaccoContainerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `tobacco_containers` (`id`,`flavorId`,`label`,`remainingGrams`,`isActive`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TobaccoContainerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFlavorId());
        statement.bindString(3, entity.getLabel());
        statement.bindDouble(4, entity.getRemainingGrams());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfPurchaseOrderItemEntity = new EntityInsertionAdapter<PurchaseOrderItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `purchase_order_items` (`id`,`flavorId`,`recommendedGrams`,`createdAt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PurchaseOrderItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFlavorId());
        statement.bindDouble(3, entity.getRecommendedGrams());
        statement.bindLong(4, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfStockMovementEntity = new EntityInsertionAdapter<StockMovementEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `stock_movements` (`id`,`flavorId`,`deltaGrams`,`movementType`,`note`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StockMovementEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFlavorId());
        statement.bindDouble(3, entity.getDeltaGrams());
        statement.bindString(4, entity.getMovementType());
        statement.bindString(5, entity.getNote());
        statement.bindLong(6, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfInventorySessionEntity = new EntityInsertionAdapter<InventorySessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `inventory_sessions` (`id`,`takenAt`,`note`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final InventorySessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTakenAt());
        statement.bindString(3, entity.getNote());
      }
    };
    this.__insertionAdapterOfBrandInventorySnapshotEntity = new EntityInsertionAdapter<BrandInventorySnapshotEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `brand_inventory_snapshots` (`sessionId`,`brandId`,`actualGrams`,`previousActualGrams`,`deliveryGrams`,`calculatedConsumptionGrams`,`actualConsumptionGrams`,`weeklyCoefficient`,`includedInAnalytics`,`periodStartAt`,`periodEndAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BrandInventorySnapshotEntity entity) {
        statement.bindLong(1, entity.getSessionId());
        statement.bindLong(2, entity.getBrandId());
        statement.bindDouble(3, entity.getActualGrams());
        if (entity.getPreviousActualGrams() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getPreviousActualGrams());
        }
        statement.bindDouble(5, entity.getDeliveryGrams());
        statement.bindDouble(6, entity.getCalculatedConsumptionGrams());
        statement.bindDouble(7, entity.getActualConsumptionGrams());
        if (entity.getWeeklyCoefficient() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getWeeklyCoefficient());
        }
        final int _tmp = entity.getIncludedInAnalytics() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getPeriodStartAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getPeriodStartAt());
        }
        statement.bindLong(11, entity.getPeriodEndAt());
      }
    };
    this.__updateAdapterOfTobaccoFlavorEntity = new EntityDeletionOrUpdateAdapter<TobaccoFlavorEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tobacco_flavors` SET `id` = ?,`brandId` = ?,`name` = ?,`descriptor` = ?,`isAvailable` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TobaccoFlavorEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBrandId());
        statement.bindString(3, entity.getName());
        statement.bindString(4, entity.getDescriptor());
        final int _tmp = entity.isAvailable() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__updateAdapterOfTobaccoContainerEntity = new EntityDeletionOrUpdateAdapter<TobaccoContainerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tobacco_containers` SET `id` = ?,`flavorId` = ?,`label` = ?,`remainingGrams` = ?,`isActive` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TobaccoContainerEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getFlavorId());
        statement.bindString(3, entity.getLabel());
        statement.bindDouble(4, entity.getRemainingGrams());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfSetFlavorAvailability = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tobacco_flavors SET isAvailable = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateContainerStock = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tobacco_containers SET remainingGrams = ?, isActive = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveOrderItem = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM purchase_order_items WHERE flavorId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBrandCoefficient = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE tobacco_brands SET densityCoefficient = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertBrand(final TobaccoBrandEntity brand,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTobaccoBrandEntity.insertAndReturnId(brand);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertFlavor(final TobaccoFlavorEntity flavor,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTobaccoFlavorEntity.insertAndReturnId(flavor);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertContainer(final TobaccoContainerEntity container,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTobaccoContainerEntity.insertAndReturnId(container);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertOrderItem(final PurchaseOrderItemEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPurchaseOrderItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMovement(final StockMovementEntity movement,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfStockMovementEntity.insertAndReturnId(movement);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertInventorySession(final InventorySessionEntity session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfInventorySessionEntity.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBrandSnapshots(final List<BrandInventorySnapshotEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBrandInventorySnapshotEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFlavor(final TobaccoFlavorEntity flavor,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTobaccoFlavorEntity.handle(flavor);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateContainer(final TobaccoContainerEntity container,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTobaccoContainerEntity.handle(container);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deductFlavorOrThrow(final long flavorId, final double grams, final String note,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ShelfDao.DefaultImpls.deductFlavorOrThrow(ShelfDao_Impl.this, flavorId, grams, note, __cont), $completion);
  }

  @Override
  public Object receiveDelivery(final long flavorId, final double grams, final String label,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ShelfDao.DefaultImpls.receiveDelivery(ShelfDao_Impl.this, flavorId, grams, label, __cont), $completion);
  }

  @Override
  public Object completeWeeklyInventory(final List<BrandInventoryInput> inputs, final String note,
      final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ShelfDao.DefaultImpls.completeWeeklyInventory(ShelfDao_Impl.this, inputs, note, __cont), $completion);
  }

  @Override
  public Object setFlavorAvailability(final long flavorId, final boolean available,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetFlavorAvailability.acquire();
        int _argIndex = 1;
        final int _tmp = available ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, flavorId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetFlavorAvailability.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateContainerStock(final long containerId, final double grams,
      final boolean active, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateContainerStock.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, grams);
        _argIndex = 2;
        final int _tmp = active ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, containerId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateContainerStock.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object removeOrderItem(final long flavorId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveOrderItem.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, flavorId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRemoveOrderItem.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBrandCoefficient(final long brandId, final double coefficient,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBrandCoefficient.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, coefficient);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, brandId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateBrandCoefficient.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object countBrands(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM tobacco_brands";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TobaccoBrandEntity>> observeBrands() {
    final String _sql = "SELECT * FROM tobacco_brands ORDER BY name COLLATE NOCASE";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tobacco_brands"}, new Callable<List<TobaccoBrandEntity>>() {
      @Override
      @NonNull
      public List<TobaccoBrandEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDensityCoefficient = CursorUtil.getColumnIndexOrThrow(_cursor, "densityCoefficient");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TobaccoBrandEntity> _result = new ArrayList<TobaccoBrandEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TobaccoBrandEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpDensityCoefficient;
            _tmpDensityCoefficient = _cursor.getDouble(_cursorIndexOfDensityCoefficient);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TobaccoBrandEntity(_tmpId,_tmpName,_tmpDensityCoefficient,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<FlavorWithStock>> observeFlavorStock() {
    final String _sql = "\n"
            + "        SELECT f.id AS flavorId, f.brandId AS brandId, b.name AS brandName,\n"
            + "               f.name AS flavorName, f.descriptor AS descriptor, f.isAvailable AS isAvailable,\n"
            + "               COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) AS totalGrams,\n"
            + "               COUNT(CASE WHEN c.isActive = 1 THEN 1 END) AS containerCount\n"
            + "        FROM tobacco_flavors f JOIN tobacco_brands b ON b.id = f.brandId\n"
            + "        LEFT JOIN tobacco_containers c ON c.flavorId = f.id\n"
            + "        GROUP BY f.id ORDER BY b.name COLLATE NOCASE, f.name COLLATE NOCASE\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tobacco_flavors", "tobacco_brands",
        "tobacco_containers"}, new Callable<List<FlavorWithStock>>() {
      @Override
      @NonNull
      public List<FlavorWithStock> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFlavorId = 0;
          final int _cursorIndexOfBrandId = 1;
          final int _cursorIndexOfBrandName = 2;
          final int _cursorIndexOfFlavorName = 3;
          final int _cursorIndexOfDescriptor = 4;
          final int _cursorIndexOfIsAvailable = 5;
          final int _cursorIndexOfTotalGrams = 6;
          final int _cursorIndexOfContainerCount = 7;
          final List<FlavorWithStock> _result = new ArrayList<FlavorWithStock>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FlavorWithStock _item;
            final long _tmpFlavorId;
            _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            final long _tmpBrandId;
            _tmpBrandId = _cursor.getLong(_cursorIndexOfBrandId);
            final String _tmpBrandName;
            _tmpBrandName = _cursor.getString(_cursorIndexOfBrandName);
            final String _tmpFlavorName;
            _tmpFlavorName = _cursor.getString(_cursorIndexOfFlavorName);
            final String _tmpDescriptor;
            _tmpDescriptor = _cursor.getString(_cursorIndexOfDescriptor);
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final double _tmpTotalGrams;
            _tmpTotalGrams = _cursor.getDouble(_cursorIndexOfTotalGrams);
            final int _tmpContainerCount;
            _tmpContainerCount = _cursor.getInt(_cursorIndexOfContainerCount);
            _item = new FlavorWithStock(_tmpFlavorId,_tmpBrandId,_tmpBrandName,_tmpFlavorName,_tmpDescriptor,_tmpIsAvailable,_tmpTotalGrams,_tmpContainerCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MixFlavorOption>> observeMixOptions() {
    final String _sql = "\n"
            + "        SELECT f.id AS flavorId, b.name AS brandName, f.name AS flavorName,\n"
            + "               f.descriptor AS descriptor, b.densityCoefficient AS densityCoefficient,\n"
            + "               COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) AS totalGrams\n"
            + "        FROM tobacco_flavors f JOIN tobacco_brands b ON b.id = f.brandId\n"
            + "        LEFT JOIN tobacco_containers c ON c.flavorId = f.id\n"
            + "        WHERE f.isAvailable = 1 GROUP BY f.id\n"
            + "        HAVING COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) > 5.0\n"
            + "        ORDER BY b.name COLLATE NOCASE, f.name COLLATE NOCASE\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tobacco_flavors", "tobacco_brands",
        "tobacco_containers"}, new Callable<List<MixFlavorOption>>() {
      @Override
      @NonNull
      public List<MixFlavorOption> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFlavorId = 0;
          final int _cursorIndexOfBrandName = 1;
          final int _cursorIndexOfFlavorName = 2;
          final int _cursorIndexOfDescriptor = 3;
          final int _cursorIndexOfDensityCoefficient = 4;
          final int _cursorIndexOfTotalGrams = 5;
          final List<MixFlavorOption> _result = new ArrayList<MixFlavorOption>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MixFlavorOption _item;
            final long _tmpFlavorId;
            _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            final String _tmpBrandName;
            _tmpBrandName = _cursor.getString(_cursorIndexOfBrandName);
            final String _tmpFlavorName;
            _tmpFlavorName = _cursor.getString(_cursorIndexOfFlavorName);
            final String _tmpDescriptor;
            _tmpDescriptor = _cursor.getString(_cursorIndexOfDescriptor);
            final double _tmpDensityCoefficient;
            _tmpDensityCoefficient = _cursor.getDouble(_cursorIndexOfDensityCoefficient);
            final double _tmpTotalGrams;
            _tmpTotalGrams = _cursor.getDouble(_cursorIndexOfTotalGrams);
            _item = new MixFlavorOption(_tmpFlavorId,_tmpBrandName,_tmpFlavorName,_tmpDescriptor,_tmpDensityCoefficient,_tmpTotalGrams);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getActiveContainers(final long flavorId,
      final Continuation<? super List<TobaccoContainerEntity>> $completion) {
    final String _sql = "SELECT * FROM tobacco_containers WHERE flavorId = ? AND isActive = 1 AND remainingGrams > 0 ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, flavorId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TobaccoContainerEntity>>() {
      @Override
      @NonNull
      public List<TobaccoContainerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFlavorId = CursorUtil.getColumnIndexOrThrow(_cursor, "flavorId");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfRemainingGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "remainingGrams");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TobaccoContainerEntity> _result = new ArrayList<TobaccoContainerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TobaccoContainerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFlavorId;
            _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final double _tmpRemainingGrams;
            _tmpRemainingGrams = _cursor.getDouble(_cursorIndexOfRemainingGrams);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TobaccoContainerEntity(_tmpId,_tmpFlavorId,_tmpLabel,_tmpRemainingGrams,_tmpIsActive,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TobaccoContainerEntity>> observeContainers(final long flavorId) {
    final String _sql = "SELECT * FROM tobacco_containers WHERE flavorId = ? ORDER BY isActive DESC, createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, flavorId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tobacco_containers"}, new Callable<List<TobaccoContainerEntity>>() {
      @Override
      @NonNull
      public List<TobaccoContainerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFlavorId = CursorUtil.getColumnIndexOrThrow(_cursor, "flavorId");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfRemainingGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "remainingGrams");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TobaccoContainerEntity> _result = new ArrayList<TobaccoContainerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TobaccoContainerEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFlavorId;
            _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final double _tmpRemainingGrams;
            _tmpRemainingGrams = _cursor.getDouble(_cursorIndexOfRemainingGrams);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TobaccoContainerEntity(_tmpId,_tmpFlavorId,_tmpLabel,_tmpRemainingGrams,_tmpIsActive,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<StockMovementEntity>> observeMovements(final long flavorId) {
    final String _sql = "SELECT * FROM stock_movements WHERE flavorId = ? ORDER BY createdAt DESC LIMIT 100";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, flavorId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stock_movements"}, new Callable<List<StockMovementEntity>>() {
      @Override
      @NonNull
      public List<StockMovementEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFlavorId = CursorUtil.getColumnIndexOrThrow(_cursor, "flavorId");
          final int _cursorIndexOfDeltaGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "deltaGrams");
          final int _cursorIndexOfMovementType = CursorUtil.getColumnIndexOrThrow(_cursor, "movementType");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<StockMovementEntity> _result = new ArrayList<StockMovementEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StockMovementEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpFlavorId;
            _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            final double _tmpDeltaGrams;
            _tmpDeltaGrams = _cursor.getDouble(_cursorIndexOfDeltaGrams);
            final String _tmpMovementType;
            _tmpMovementType = _cursor.getString(_cursorIndexOfMovementType);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new StockMovementEntity(_tmpId,_tmpFlavorId,_tmpDeltaGrams,_tmpMovementType,_tmpNote,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<OrderedFlavor>> observeOrderedItems() {
    final String _sql = "\n"
            + "        SELECT p.id AS orderId, p.flavorId AS flavorId, b.name AS brandName, f.name AS flavorName,\n"
            + "               COALESCE(SUM(CASE WHEN c.isActive = 1 THEN c.remainingGrams ELSE 0 END), 0) AS currentGrams,\n"
            + "               p.recommendedGrams AS recommendedGrams, p.createdAt AS createdAt\n"
            + "        FROM purchase_order_items p JOIN tobacco_flavors f ON f.id = p.flavorId\n"
            + "        JOIN tobacco_brands b ON b.id = f.brandId LEFT JOIN tobacco_containers c ON c.flavorId = f.id\n"
            + "        GROUP BY p.id ORDER BY b.name COLLATE NOCASE, f.name COLLATE NOCASE\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"purchase_order_items",
        "tobacco_flavors", "tobacco_brands",
        "tobacco_containers"}, new Callable<List<OrderedFlavor>>() {
      @Override
      @NonNull
      public List<OrderedFlavor> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrderId = 0;
          final int _cursorIndexOfFlavorId = 1;
          final int _cursorIndexOfBrandName = 2;
          final int _cursorIndexOfFlavorName = 3;
          final int _cursorIndexOfCurrentGrams = 4;
          final int _cursorIndexOfRecommendedGrams = 5;
          final int _cursorIndexOfCreatedAt = 6;
          final List<OrderedFlavor> _result = new ArrayList<OrderedFlavor>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderedFlavor _item;
            final long _tmpOrderId;
            _tmpOrderId = _cursor.getLong(_cursorIndexOfOrderId);
            final long _tmpFlavorId;
            _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            final String _tmpBrandName;
            _tmpBrandName = _cursor.getString(_cursorIndexOfBrandName);
            final String _tmpFlavorName;
            _tmpFlavorName = _cursor.getString(_cursorIndexOfFlavorName);
            final double _tmpCurrentGrams;
            _tmpCurrentGrams = _cursor.getDouble(_cursorIndexOfCurrentGrams);
            final double _tmpRecommendedGrams;
            _tmpRecommendedGrams = _cursor.getDouble(_cursorIndexOfRecommendedGrams);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new OrderedFlavor(_tmpOrderId,_tmpFlavorId,_tmpBrandName,_tmpFlavorName,_tmpCurrentGrams,_tmpRecommendedGrams,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BrandConsumptionAnalytics>> observeBrandAnalytics() {
    final String _sql = "\n"
            + "        SELECT b.id AS brandId, b.name AS brandName, b.densityCoefficient AS densityCoefficient,\n"
            + "               CAST(COUNT(CASE WHEN s.previousActualGrams IS NOT NULL AND s.includedInAnalytics = 1 AND s.calculatedConsumptionGrams > 0 THEN 1 END) AS INTEGER) AS completedPeriods,\n"
            + "               COALESCE(SUM(CASE WHEN s.previousActualGrams IS NOT NULL AND s.includedInAnalytics = 1 THEN s.actualConsumptionGrams ELSE 0 END), 0) AS totalActualConsumptionGrams,\n"
            + "               COALESCE(SUM(CASE WHEN s.previousActualGrams IS NOT NULL AND s.includedInAnalytics = 1 THEN s.calculatedConsumptionGrams ELSE 0 END), 0) AS totalCalculatedConsumptionGrams,\n"
            + "               MAX(i.takenAt) AS lastInventoryAt\n"
            + "        FROM tobacco_brands b\n"
            + "        LEFT JOIN brand_inventory_snapshots s ON s.brandId = b.id\n"
            + "        LEFT JOIN inventory_sessions i ON i.id = s.sessionId\n"
            + "        GROUP BY b.id\n"
            + "        ORDER BY b.name COLLATE NOCASE\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tobacco_brands",
        "brand_inventory_snapshots",
        "inventory_sessions"}, new Callable<List<BrandConsumptionAnalytics>>() {
      @Override
      @NonNull
      public List<BrandConsumptionAnalytics> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBrandId = 0;
          final int _cursorIndexOfBrandName = 1;
          final int _cursorIndexOfDensityCoefficient = 2;
          final int _cursorIndexOfCompletedPeriods = 3;
          final int _cursorIndexOfTotalActualConsumptionGrams = 4;
          final int _cursorIndexOfTotalCalculatedConsumptionGrams = 5;
          final int _cursorIndexOfLastInventoryAt = 6;
          final List<BrandConsumptionAnalytics> _result = new ArrayList<BrandConsumptionAnalytics>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BrandConsumptionAnalytics _item;
            final long _tmpBrandId;
            _tmpBrandId = _cursor.getLong(_cursorIndexOfBrandId);
            final String _tmpBrandName;
            _tmpBrandName = _cursor.getString(_cursorIndexOfBrandName);
            final double _tmpDensityCoefficient;
            _tmpDensityCoefficient = _cursor.getDouble(_cursorIndexOfDensityCoefficient);
            final int _tmpCompletedPeriods;
            _tmpCompletedPeriods = _cursor.getInt(_cursorIndexOfCompletedPeriods);
            final double _tmpTotalActualConsumptionGrams;
            _tmpTotalActualConsumptionGrams = _cursor.getDouble(_cursorIndexOfTotalActualConsumptionGrams);
            final double _tmpTotalCalculatedConsumptionGrams;
            _tmpTotalCalculatedConsumptionGrams = _cursor.getDouble(_cursorIndexOfTotalCalculatedConsumptionGrams);
            final Long _tmpLastInventoryAt;
            if (_cursor.isNull(_cursorIndexOfLastInventoryAt)) {
              _tmpLastInventoryAt = null;
            } else {
              _tmpLastInventoryAt = _cursor.getLong(_cursorIndexOfLastInventoryAt);
            }
            _item = new BrandConsumptionAnalytics(_tmpBrandId,_tmpBrandName,_tmpDensityCoefficient,_tmpCompletedPeriods,_tmpTotalActualConsumptionGrams,_tmpTotalCalculatedConsumptionGrams,_tmpLastInventoryAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<InventorySessionSummary>> observeInventoryHistory() {
    final String _sql = "\n"
            + "        SELECT i.id AS sessionId, i.takenAt AS takenAt, i.note AS note, COUNT(s.brandId) AS brandCount\n"
            + "        FROM inventory_sessions i LEFT JOIN brand_inventory_snapshots s ON s.sessionId = i.id\n"
            + "        GROUP BY i.id ORDER BY i.takenAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"inventory_sessions",
        "brand_inventory_snapshots"}, new Callable<List<InventorySessionSummary>>() {
      @Override
      @NonNull
      public List<InventorySessionSummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSessionId = 0;
          final int _cursorIndexOfTakenAt = 1;
          final int _cursorIndexOfNote = 2;
          final int _cursorIndexOfBrandCount = 3;
          final List<InventorySessionSummary> _result = new ArrayList<InventorySessionSummary>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final InventorySessionSummary _item;
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final int _tmpBrandCount;
            _tmpBrandCount = _cursor.getInt(_cursorIndexOfBrandCount);
            _item = new InventorySessionSummary(_tmpSessionId,_tmpTakenAt,_tmpNote,_tmpBrandCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLastInventorySession(
      final Continuation<? super InventorySessionEntity> $completion) {
    final String _sql = "SELECT * FROM inventory_sessions ORDER BY takenAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<InventorySessionEntity>() {
      @Override
      @Nullable
      public InventorySessionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTakenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "takenAt");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final InventorySessionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTakenAt;
            _tmpTakenAt = _cursor.getLong(_cursorIndexOfTakenAt);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            _result = new InventorySessionEntity(_tmpId,_tmpTakenAt,_tmpNote);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSnapshotsForSession(final long sessionId,
      final Continuation<? super List<BrandInventorySnapshotEntity>> $completion) {
    final String _sql = "SELECT * FROM brand_inventory_snapshots WHERE sessionId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BrandInventorySnapshotEntity>>() {
      @Override
      @NonNull
      public List<BrandInventorySnapshotEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfBrandId = CursorUtil.getColumnIndexOrThrow(_cursor, "brandId");
          final int _cursorIndexOfActualGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "actualGrams");
          final int _cursorIndexOfPreviousActualGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "previousActualGrams");
          final int _cursorIndexOfDeliveryGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveryGrams");
          final int _cursorIndexOfCalculatedConsumptionGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "calculatedConsumptionGrams");
          final int _cursorIndexOfActualConsumptionGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "actualConsumptionGrams");
          final int _cursorIndexOfWeeklyCoefficient = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyCoefficient");
          final int _cursorIndexOfIncludedInAnalytics = CursorUtil.getColumnIndexOrThrow(_cursor, "includedInAnalytics");
          final int _cursorIndexOfPeriodStartAt = CursorUtil.getColumnIndexOrThrow(_cursor, "periodStartAt");
          final int _cursorIndexOfPeriodEndAt = CursorUtil.getColumnIndexOrThrow(_cursor, "periodEndAt");
          final List<BrandInventorySnapshotEntity> _result = new ArrayList<BrandInventorySnapshotEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BrandInventorySnapshotEntity _item;
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final long _tmpBrandId;
            _tmpBrandId = _cursor.getLong(_cursorIndexOfBrandId);
            final double _tmpActualGrams;
            _tmpActualGrams = _cursor.getDouble(_cursorIndexOfActualGrams);
            final Double _tmpPreviousActualGrams;
            if (_cursor.isNull(_cursorIndexOfPreviousActualGrams)) {
              _tmpPreviousActualGrams = null;
            } else {
              _tmpPreviousActualGrams = _cursor.getDouble(_cursorIndexOfPreviousActualGrams);
            }
            final double _tmpDeliveryGrams;
            _tmpDeliveryGrams = _cursor.getDouble(_cursorIndexOfDeliveryGrams);
            final double _tmpCalculatedConsumptionGrams;
            _tmpCalculatedConsumptionGrams = _cursor.getDouble(_cursorIndexOfCalculatedConsumptionGrams);
            final double _tmpActualConsumptionGrams;
            _tmpActualConsumptionGrams = _cursor.getDouble(_cursorIndexOfActualConsumptionGrams);
            final Double _tmpWeeklyCoefficient;
            if (_cursor.isNull(_cursorIndexOfWeeklyCoefficient)) {
              _tmpWeeklyCoefficient = null;
            } else {
              _tmpWeeklyCoefficient = _cursor.getDouble(_cursorIndexOfWeeklyCoefficient);
            }
            final boolean _tmpIncludedInAnalytics;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIncludedInAnalytics);
            _tmpIncludedInAnalytics = _tmp != 0;
            final Long _tmpPeriodStartAt;
            if (_cursor.isNull(_cursorIndexOfPeriodStartAt)) {
              _tmpPeriodStartAt = null;
            } else {
              _tmpPeriodStartAt = _cursor.getLong(_cursorIndexOfPeriodStartAt);
            }
            final long _tmpPeriodEndAt;
            _tmpPeriodEndAt = _cursor.getLong(_cursorIndexOfPeriodEndAt);
            _item = new BrandInventorySnapshotEntity(_tmpSessionId,_tmpBrandId,_tmpActualGrams,_tmpPreviousActualGrams,_tmpDeliveryGrams,_tmpCalculatedConsumptionGrams,_tmpActualConsumptionGrams,_tmpWeeklyCoefficient,_tmpIncludedInAnalytics,_tmpPeriodStartAt,_tmpPeriodEndAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBrandDeliveries(final long brandId, final long fromAt, final long toAt,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(sm.deltaGrams), 0) FROM stock_movements sm\n"
            + "        JOIN tobacco_flavors f ON f.id = sm.flavorId\n"
            + "        WHERE f.brandId = ? AND sm.deltaGrams > 0 AND sm.createdAt > ? AND sm.createdAt <= ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, brandId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, fromAt);
    _argIndex = 3;
    _statement.bindLong(_argIndex, toAt);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBrandCalculatedConsumption(final long brandId, final long fromAt,
      final long toAt, final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(-SUM(sm.deltaGrams), 0) FROM stock_movements sm\n"
            + "        JOIN tobacco_flavors f ON f.id = sm.flavorId\n"
            + "        WHERE f.brandId = ? AND sm.movementType = 'WRITE_OFF'\n"
            + "          AND sm.deltaGrams < 0 AND sm.createdAt > ? AND sm.createdAt <= ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, brandId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, fromAt);
    _argIndex = 3;
    _statement.bindLong(_argIndex, toAt);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAccumulatedActualConsumption(final long brandId,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(actualConsumptionGrams), 0) FROM brand_inventory_snapshots\n"
            + "        WHERE brandId = ? AND previousActualGrams IS NOT NULL AND includedInAnalytics = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, brandId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAccumulatedCalculatedConsumption(final long brandId,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(calculatedConsumptionGrams), 0) FROM brand_inventory_snapshots\n"
            + "        WHERE brandId = ? AND previousActualGrams IS NOT NULL AND includedInAnalytics = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, brandId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCompletedInventoryPeriods(final long brandId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM brand_inventory_snapshots\n"
            + "        WHERE brandId = ? AND previousActualGrams IS NOT NULL AND includedInAnalytics = 1 AND calculatedConsumptionGrams > 0\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, brandId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
