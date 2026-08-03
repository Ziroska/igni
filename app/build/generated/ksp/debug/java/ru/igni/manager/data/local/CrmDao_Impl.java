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
public final class CrmDao_Impl implements CrmDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GuestEntity> __insertionAdapterOfGuestEntity;

  private final EntityInsertionAdapter<VisitEntity> __insertionAdapterOfVisitEntity;

  private final EntityInsertionAdapter<HookahHistoryEntity> __insertionAdapterOfHookahHistoryEntity;

  private final EntityInsertionAdapter<HookahMixItemEntity> __insertionAdapterOfHookahMixItemEntity;

  private final EntityInsertionAdapter<CoalChangeEntity> __insertionAdapterOfCoalChangeEntity;

  private final EntityDeletionOrUpdateAdapter<GuestEntity> __updateAdapterOfGuestEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateGuestAfterVisit;

  private final SharedSQLiteStatement __preparedStmtOfArchiveGuest;

  private final SharedSQLiteStatement __preparedStmtOfCloseVisit;

  public CrmDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGuestEntity = new EntityInsertionAdapter<GuestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `guests` (`id`,`name`,`phone`,`birthday`,`telegram`,`comment`,`preferredStrength`,`preferredBowlType`,`preferredDescriptors`,`dislikedFlavors`,`vipStatus`,`firstVisitAt`,`lastVisitAt`,`visitCount`,`totalHookahs`,`favoriteBrands`,`favoriteFlavors`,`isArchived`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GuestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getPhone() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPhone());
        }
        if (entity.getBirthday() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getBirthday());
        }
        if (entity.getTelegram() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelegram());
        }
        statement.bindString(6, entity.getComment());
        if (entity.getPreferredStrength() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPreferredStrength());
        }
        if (entity.getPreferredBowlType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPreferredBowlType());
        }
        statement.bindString(9, entity.getPreferredDescriptors());
        statement.bindString(10, entity.getDislikedFlavors());
        statement.bindString(11, entity.getVipStatus());
        statement.bindLong(12, entity.getFirstVisitAt());
        if (entity.getLastVisitAt() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getLastVisitAt());
        }
        statement.bindLong(14, entity.getVisitCount());
        statement.bindLong(15, entity.getTotalHookahs());
        statement.bindString(16, entity.getFavoriteBrands());
        statement.bindString(17, entity.getFavoriteFlavors());
        final int _tmp = entity.isArchived() ? 1 : 0;
        statement.bindLong(18, _tmp);
        statement.bindLong(19, entity.getCreatedAt());
        statement.bindLong(20, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfVisitEntity = new EntityInsertionAdapter<VisitEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `visits` (`id`,`guestId`,`employeeId`,`tableNumber`,`startedAt`,`closedAt`,`status`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final VisitEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getGuestId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getGuestId());
        }
        if (entity.getEmployeeId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getEmployeeId());
        }
        statement.bindLong(4, entity.getTableNumber());
        statement.bindLong(5, entity.getStartedAt());
        if (entity.getClosedAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getClosedAt());
        }
        statement.bindString(7, entity.getStatus());
        statement.bindString(8, entity.getNotes());
      }
    };
    this.__insertionAdapterOfHookahHistoryEntity = new EntityInsertionAdapter<HookahHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `hookah_history` (`id`,`visitId`,`guestId`,`employeeId`,`tableNumber`,`bowlType`,`strength`,`hookahCount`,`startedAt`,`closedAt`,`status`,`comment`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HookahHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVisitId());
        if (entity.getGuestId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getGuestId());
        }
        if (entity.getEmployeeId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEmployeeId());
        }
        statement.bindLong(5, entity.getTableNumber());
        statement.bindString(6, entity.getBowlType());
        statement.bindLong(7, entity.getStrength());
        statement.bindLong(8, entity.getHookahCount());
        statement.bindLong(9, entity.getStartedAt());
        if (entity.getClosedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getClosedAt());
        }
        statement.bindString(11, entity.getStatus());
        statement.bindString(12, entity.getComment());
      }
    };
    this.__insertionAdapterOfHookahMixItemEntity = new EntityInsertionAdapter<HookahMixItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `hookah_mix_items` (`id`,`hookahId`,`flavorId`,`brandNameSnapshot`,`flavorNameSnapshot`,`descriptorSnapshot`,`percentage`,`grams`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HookahMixItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHookahId());
        if (entity.getFlavorId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getFlavorId());
        }
        statement.bindString(4, entity.getBrandNameSnapshot());
        statement.bindString(5, entity.getFlavorNameSnapshot());
        statement.bindString(6, entity.getDescriptorSnapshot());
        statement.bindLong(7, entity.getPercentage());
        statement.bindDouble(8, entity.getGrams());
      }
    };
    this.__insertionAdapterOfCoalChangeEntity = new EntityInsertionAdapter<CoalChangeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `coal_changes` (`id`,`hookahId`,`employeeId`,`changedAt`,`sequenceNumber`,`comment`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CoalChangeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHookahId());
        if (entity.getEmployeeId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getEmployeeId());
        }
        statement.bindLong(4, entity.getChangedAt());
        statement.bindLong(5, entity.getSequenceNumber());
        statement.bindString(6, entity.getComment());
      }
    };
    this.__updateAdapterOfGuestEntity = new EntityDeletionOrUpdateAdapter<GuestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `guests` SET `id` = ?,`name` = ?,`phone` = ?,`birthday` = ?,`telegram` = ?,`comment` = ?,`preferredStrength` = ?,`preferredBowlType` = ?,`preferredDescriptors` = ?,`dislikedFlavors` = ?,`vipStatus` = ?,`firstVisitAt` = ?,`lastVisitAt` = ?,`visitCount` = ?,`totalHookahs` = ?,`favoriteBrands` = ?,`favoriteFlavors` = ?,`isArchived` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GuestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getPhone() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPhone());
        }
        if (entity.getBirthday() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getBirthday());
        }
        if (entity.getTelegram() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTelegram());
        }
        statement.bindString(6, entity.getComment());
        if (entity.getPreferredStrength() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPreferredStrength());
        }
        if (entity.getPreferredBowlType() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getPreferredBowlType());
        }
        statement.bindString(9, entity.getPreferredDescriptors());
        statement.bindString(10, entity.getDislikedFlavors());
        statement.bindString(11, entity.getVipStatus());
        statement.bindLong(12, entity.getFirstVisitAt());
        if (entity.getLastVisitAt() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getLastVisitAt());
        }
        statement.bindLong(14, entity.getVisitCount());
        statement.bindLong(15, entity.getTotalHookahs());
        statement.bindString(16, entity.getFavoriteBrands());
        statement.bindString(17, entity.getFavoriteFlavors());
        final int _tmp = entity.isArchived() ? 1 : 0;
        statement.bindLong(18, _tmp);
        statement.bindLong(19, entity.getCreatedAt());
        statement.bindLong(20, entity.getUpdatedAt());
        statement.bindLong(21, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateGuestAfterVisit = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE guests SET\n"
                + "            visitCount = visitCount + 1,\n"
                + "            totalHookahs = totalHookahs + ?,\n"
                + "            lastVisitAt = ?,\n"
                + "            preferredStrength = ?,\n"
                + "            favoriteBrands = ?,\n"
                + "            favoriteFlavors = ?,\n"
                + "            updatedAt = ?\n"
                + "        WHERE id = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfArchiveGuest = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE guests SET isArchived = 1, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfCloseVisit = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE visits SET closedAt = ?, status = 'CLOSED' WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertGuest(final GuestEntity guest, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGuestEntity.insertAndReturnId(guest);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertVisit(final VisitEntity visit, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVisitEntity.insertAndReturnId(visit);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertHookah(final HookahHistoryEntity hookah,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHookahHistoryEntity.insertAndReturnId(hookah);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertMixItems(final List<HookahMixItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHookahMixItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCoalChange(final CoalChangeEntity change,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCoalChangeEntity.insertAndReturnId(change);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateGuest(final GuestEntity guest, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGuestEntity.handle(guest);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object createHookahWithMix(final HookahHistoryEntity hookah,
      final List<HookahMixItemEntity> mixItems, final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> CrmDao.DefaultImpls.createHookahWithMix(CrmDao_Impl.this, hookah, mixItems, __cont), $completion);
  }

  @Override
  public Object updateGuestAfterVisit(final long guestId, final int hookahCount,
      final long closedAt, final Integer preferredStrength, final String brands,
      final String flavors, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateGuestAfterVisit.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, hookahCount);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, closedAt);
        _argIndex = 3;
        if (preferredStrength == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, preferredStrength);
        }
        _argIndex = 4;
        _stmt.bindString(_argIndex, brands);
        _argIndex = 5;
        _stmt.bindString(_argIndex, flavors);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, closedAt);
        _argIndex = 7;
        _stmt.bindLong(_argIndex, guestId);
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
          __preparedStmtOfUpdateGuestAfterVisit.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object archiveGuest(final long guestId, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfArchiveGuest.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, guestId);
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
          __preparedStmtOfArchiveGuest.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object closeVisit(final long visitId, final long closedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCloseVisit.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, closedAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, visitId);
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
          __preparedStmtOfCloseVisit.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GuestEntity>> observeGuests(final String query) {
    final String _sql = "\n"
            + "        SELECT * FROM guests\n"
            + "        WHERE isArchived = 0\n"
            + "          AND (? = ''\n"
            + "            OR name LIKE '%' || ? || '%' COLLATE NOCASE\n"
            + "            OR COALESCE(phone, '') LIKE '%' || ? || '%'\n"
            + "            OR COALESCE(telegram, '') LIKE '%' || ? || '%' COLLATE NOCASE\n"
            + "            OR comment LIKE '%' || ? || '%' COLLATE NOCASE)\n"
            + "        ORDER BY lastVisitAt DESC, name COLLATE NOCASE\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    _argIndex = 3;
    _statement.bindString(_argIndex, query);
    _argIndex = 4;
    _statement.bindString(_argIndex, query);
    _argIndex = 5;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"guests"}, new Callable<List<GuestEntity>>() {
      @Override
      @NonNull
      public List<GuestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfTelegram = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final int _cursorIndexOfPreferredStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredStrength");
          final int _cursorIndexOfPreferredBowlType = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredBowlType");
          final int _cursorIndexOfPreferredDescriptors = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredDescriptors");
          final int _cursorIndexOfDislikedFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "dislikedFlavors");
          final int _cursorIndexOfVipStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "vipStatus");
          final int _cursorIndexOfFirstVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstVisitAt");
          final int _cursorIndexOfLastVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastVisitAt");
          final int _cursorIndexOfVisitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "visitCount");
          final int _cursorIndexOfTotalHookahs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalHookahs");
          final int _cursorIndexOfFavoriteBrands = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteBrands");
          final int _cursorIndexOfFavoriteFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteFlavors");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<GuestEntity> _result = new ArrayList<GuestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GuestEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final Long _tmpBirthday;
            if (_cursor.isNull(_cursorIndexOfBirthday)) {
              _tmpBirthday = null;
            } else {
              _tmpBirthday = _cursor.getLong(_cursorIndexOfBirthday);
            }
            final String _tmpTelegram;
            if (_cursor.isNull(_cursorIndexOfTelegram)) {
              _tmpTelegram = null;
            } else {
              _tmpTelegram = _cursor.getString(_cursorIndexOfTelegram);
            }
            final String _tmpComment;
            _tmpComment = _cursor.getString(_cursorIndexOfComment);
            final Integer _tmpPreferredStrength;
            if (_cursor.isNull(_cursorIndexOfPreferredStrength)) {
              _tmpPreferredStrength = null;
            } else {
              _tmpPreferredStrength = _cursor.getInt(_cursorIndexOfPreferredStrength);
            }
            final String _tmpPreferredBowlType;
            if (_cursor.isNull(_cursorIndexOfPreferredBowlType)) {
              _tmpPreferredBowlType = null;
            } else {
              _tmpPreferredBowlType = _cursor.getString(_cursorIndexOfPreferredBowlType);
            }
            final String _tmpPreferredDescriptors;
            _tmpPreferredDescriptors = _cursor.getString(_cursorIndexOfPreferredDescriptors);
            final String _tmpDislikedFlavors;
            _tmpDislikedFlavors = _cursor.getString(_cursorIndexOfDislikedFlavors);
            final String _tmpVipStatus;
            _tmpVipStatus = _cursor.getString(_cursorIndexOfVipStatus);
            final long _tmpFirstVisitAt;
            _tmpFirstVisitAt = _cursor.getLong(_cursorIndexOfFirstVisitAt);
            final Long _tmpLastVisitAt;
            if (_cursor.isNull(_cursorIndexOfLastVisitAt)) {
              _tmpLastVisitAt = null;
            } else {
              _tmpLastVisitAt = _cursor.getLong(_cursorIndexOfLastVisitAt);
            }
            final int _tmpVisitCount;
            _tmpVisitCount = _cursor.getInt(_cursorIndexOfVisitCount);
            final int _tmpTotalHookahs;
            _tmpTotalHookahs = _cursor.getInt(_cursorIndexOfTotalHookahs);
            final String _tmpFavoriteBrands;
            _tmpFavoriteBrands = _cursor.getString(_cursorIndexOfFavoriteBrands);
            final String _tmpFavoriteFlavors;
            _tmpFavoriteFlavors = _cursor.getString(_cursorIndexOfFavoriteFlavors);
            final boolean _tmpIsArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new GuestEntity(_tmpId,_tmpName,_tmpPhone,_tmpBirthday,_tmpTelegram,_tmpComment,_tmpPreferredStrength,_tmpPreferredBowlType,_tmpPreferredDescriptors,_tmpDislikedFlavors,_tmpVipStatus,_tmpFirstVisitAt,_tmpLastVisitAt,_tmpVisitCount,_tmpTotalHookahs,_tmpFavoriteBrands,_tmpFavoriteFlavors,_tmpIsArchived,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<GuestEntity> observeGuest(final long guestId) {
    final String _sql = "SELECT * FROM guests WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"guests"}, new Callable<GuestEntity>() {
      @Override
      @Nullable
      public GuestEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfTelegram = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final int _cursorIndexOfPreferredStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredStrength");
          final int _cursorIndexOfPreferredBowlType = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredBowlType");
          final int _cursorIndexOfPreferredDescriptors = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredDescriptors");
          final int _cursorIndexOfDislikedFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "dislikedFlavors");
          final int _cursorIndexOfVipStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "vipStatus");
          final int _cursorIndexOfFirstVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstVisitAt");
          final int _cursorIndexOfLastVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastVisitAt");
          final int _cursorIndexOfVisitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "visitCount");
          final int _cursorIndexOfTotalHookahs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalHookahs");
          final int _cursorIndexOfFavoriteBrands = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteBrands");
          final int _cursorIndexOfFavoriteFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteFlavors");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final GuestEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final Long _tmpBirthday;
            if (_cursor.isNull(_cursorIndexOfBirthday)) {
              _tmpBirthday = null;
            } else {
              _tmpBirthday = _cursor.getLong(_cursorIndexOfBirthday);
            }
            final String _tmpTelegram;
            if (_cursor.isNull(_cursorIndexOfTelegram)) {
              _tmpTelegram = null;
            } else {
              _tmpTelegram = _cursor.getString(_cursorIndexOfTelegram);
            }
            final String _tmpComment;
            _tmpComment = _cursor.getString(_cursorIndexOfComment);
            final Integer _tmpPreferredStrength;
            if (_cursor.isNull(_cursorIndexOfPreferredStrength)) {
              _tmpPreferredStrength = null;
            } else {
              _tmpPreferredStrength = _cursor.getInt(_cursorIndexOfPreferredStrength);
            }
            final String _tmpPreferredBowlType;
            if (_cursor.isNull(_cursorIndexOfPreferredBowlType)) {
              _tmpPreferredBowlType = null;
            } else {
              _tmpPreferredBowlType = _cursor.getString(_cursorIndexOfPreferredBowlType);
            }
            final String _tmpPreferredDescriptors;
            _tmpPreferredDescriptors = _cursor.getString(_cursorIndexOfPreferredDescriptors);
            final String _tmpDislikedFlavors;
            _tmpDislikedFlavors = _cursor.getString(_cursorIndexOfDislikedFlavors);
            final String _tmpVipStatus;
            _tmpVipStatus = _cursor.getString(_cursorIndexOfVipStatus);
            final long _tmpFirstVisitAt;
            _tmpFirstVisitAt = _cursor.getLong(_cursorIndexOfFirstVisitAt);
            final Long _tmpLastVisitAt;
            if (_cursor.isNull(_cursorIndexOfLastVisitAt)) {
              _tmpLastVisitAt = null;
            } else {
              _tmpLastVisitAt = _cursor.getLong(_cursorIndexOfLastVisitAt);
            }
            final int _tmpVisitCount;
            _tmpVisitCount = _cursor.getInt(_cursorIndexOfVisitCount);
            final int _tmpTotalHookahs;
            _tmpTotalHookahs = _cursor.getInt(_cursorIndexOfTotalHookahs);
            final String _tmpFavoriteBrands;
            _tmpFavoriteBrands = _cursor.getString(_cursorIndexOfFavoriteBrands);
            final String _tmpFavoriteFlavors;
            _tmpFavoriteFlavors = _cursor.getString(_cursorIndexOfFavoriteFlavors);
            final boolean _tmpIsArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new GuestEntity(_tmpId,_tmpName,_tmpPhone,_tmpBirthday,_tmpTelegram,_tmpComment,_tmpPreferredStrength,_tmpPreferredBowlType,_tmpPreferredDescriptors,_tmpDislikedFlavors,_tmpVipStatus,_tmpFirstVisitAt,_tmpLastVisitAt,_tmpVisitCount,_tmpTotalHookahs,_tmpFavoriteBrands,_tmpFavoriteFlavors,_tmpIsArchived,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Object findByPhone(final String phone,
      final Continuation<? super GuestEntity> $completion) {
    final String _sql = "SELECT * FROM guests WHERE phone = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, phone);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GuestEntity>() {
      @Override
      @Nullable
      public GuestEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfTelegram = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final int _cursorIndexOfPreferredStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredStrength");
          final int _cursorIndexOfPreferredBowlType = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredBowlType");
          final int _cursorIndexOfPreferredDescriptors = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredDescriptors");
          final int _cursorIndexOfDislikedFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "dislikedFlavors");
          final int _cursorIndexOfVipStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "vipStatus");
          final int _cursorIndexOfFirstVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstVisitAt");
          final int _cursorIndexOfLastVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastVisitAt");
          final int _cursorIndexOfVisitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "visitCount");
          final int _cursorIndexOfTotalHookahs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalHookahs");
          final int _cursorIndexOfFavoriteBrands = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteBrands");
          final int _cursorIndexOfFavoriteFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteFlavors");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final GuestEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final Long _tmpBirthday;
            if (_cursor.isNull(_cursorIndexOfBirthday)) {
              _tmpBirthday = null;
            } else {
              _tmpBirthday = _cursor.getLong(_cursorIndexOfBirthday);
            }
            final String _tmpTelegram;
            if (_cursor.isNull(_cursorIndexOfTelegram)) {
              _tmpTelegram = null;
            } else {
              _tmpTelegram = _cursor.getString(_cursorIndexOfTelegram);
            }
            final String _tmpComment;
            _tmpComment = _cursor.getString(_cursorIndexOfComment);
            final Integer _tmpPreferredStrength;
            if (_cursor.isNull(_cursorIndexOfPreferredStrength)) {
              _tmpPreferredStrength = null;
            } else {
              _tmpPreferredStrength = _cursor.getInt(_cursorIndexOfPreferredStrength);
            }
            final String _tmpPreferredBowlType;
            if (_cursor.isNull(_cursorIndexOfPreferredBowlType)) {
              _tmpPreferredBowlType = null;
            } else {
              _tmpPreferredBowlType = _cursor.getString(_cursorIndexOfPreferredBowlType);
            }
            final String _tmpPreferredDescriptors;
            _tmpPreferredDescriptors = _cursor.getString(_cursorIndexOfPreferredDescriptors);
            final String _tmpDislikedFlavors;
            _tmpDislikedFlavors = _cursor.getString(_cursorIndexOfDislikedFlavors);
            final String _tmpVipStatus;
            _tmpVipStatus = _cursor.getString(_cursorIndexOfVipStatus);
            final long _tmpFirstVisitAt;
            _tmpFirstVisitAt = _cursor.getLong(_cursorIndexOfFirstVisitAt);
            final Long _tmpLastVisitAt;
            if (_cursor.isNull(_cursorIndexOfLastVisitAt)) {
              _tmpLastVisitAt = null;
            } else {
              _tmpLastVisitAt = _cursor.getLong(_cursorIndexOfLastVisitAt);
            }
            final int _tmpVisitCount;
            _tmpVisitCount = _cursor.getInt(_cursorIndexOfVisitCount);
            final int _tmpTotalHookahs;
            _tmpTotalHookahs = _cursor.getInt(_cursorIndexOfTotalHookahs);
            final String _tmpFavoriteBrands;
            _tmpFavoriteBrands = _cursor.getString(_cursorIndexOfFavoriteBrands);
            final String _tmpFavoriteFlavors;
            _tmpFavoriteFlavors = _cursor.getString(_cursorIndexOfFavoriteFlavors);
            final boolean _tmpIsArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new GuestEntity(_tmpId,_tmpName,_tmpPhone,_tmpBirthday,_tmpTelegram,_tmpComment,_tmpPreferredStrength,_tmpPreferredBowlType,_tmpPreferredDescriptors,_tmpDislikedFlavors,_tmpVipStatus,_tmpFirstVisitAt,_tmpLastVisitAt,_tmpVisitCount,_tmpTotalHookahs,_tmpFavoriteBrands,_tmpFavoriteFlavors,_tmpIsArchived,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getGuest(final long guestId, final Continuation<? super GuestEntity> $completion) {
    final String _sql = "SELECT * FROM guests WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GuestEntity>() {
      @Override
      @Nullable
      public GuestEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfTelegram = CursorUtil.getColumnIndexOrThrow(_cursor, "telegram");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final int _cursorIndexOfPreferredStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredStrength");
          final int _cursorIndexOfPreferredBowlType = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredBowlType");
          final int _cursorIndexOfPreferredDescriptors = CursorUtil.getColumnIndexOrThrow(_cursor, "preferredDescriptors");
          final int _cursorIndexOfDislikedFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "dislikedFlavors");
          final int _cursorIndexOfVipStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "vipStatus");
          final int _cursorIndexOfFirstVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "firstVisitAt");
          final int _cursorIndexOfLastVisitAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastVisitAt");
          final int _cursorIndexOfVisitCount = CursorUtil.getColumnIndexOrThrow(_cursor, "visitCount");
          final int _cursorIndexOfTotalHookahs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalHookahs");
          final int _cursorIndexOfFavoriteBrands = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteBrands");
          final int _cursorIndexOfFavoriteFlavors = CursorUtil.getColumnIndexOrThrow(_cursor, "favoriteFlavors");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final GuestEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final Long _tmpBirthday;
            if (_cursor.isNull(_cursorIndexOfBirthday)) {
              _tmpBirthday = null;
            } else {
              _tmpBirthday = _cursor.getLong(_cursorIndexOfBirthday);
            }
            final String _tmpTelegram;
            if (_cursor.isNull(_cursorIndexOfTelegram)) {
              _tmpTelegram = null;
            } else {
              _tmpTelegram = _cursor.getString(_cursorIndexOfTelegram);
            }
            final String _tmpComment;
            _tmpComment = _cursor.getString(_cursorIndexOfComment);
            final Integer _tmpPreferredStrength;
            if (_cursor.isNull(_cursorIndexOfPreferredStrength)) {
              _tmpPreferredStrength = null;
            } else {
              _tmpPreferredStrength = _cursor.getInt(_cursorIndexOfPreferredStrength);
            }
            final String _tmpPreferredBowlType;
            if (_cursor.isNull(_cursorIndexOfPreferredBowlType)) {
              _tmpPreferredBowlType = null;
            } else {
              _tmpPreferredBowlType = _cursor.getString(_cursorIndexOfPreferredBowlType);
            }
            final String _tmpPreferredDescriptors;
            _tmpPreferredDescriptors = _cursor.getString(_cursorIndexOfPreferredDescriptors);
            final String _tmpDislikedFlavors;
            _tmpDislikedFlavors = _cursor.getString(_cursorIndexOfDislikedFlavors);
            final String _tmpVipStatus;
            _tmpVipStatus = _cursor.getString(_cursorIndexOfVipStatus);
            final long _tmpFirstVisitAt;
            _tmpFirstVisitAt = _cursor.getLong(_cursorIndexOfFirstVisitAt);
            final Long _tmpLastVisitAt;
            if (_cursor.isNull(_cursorIndexOfLastVisitAt)) {
              _tmpLastVisitAt = null;
            } else {
              _tmpLastVisitAt = _cursor.getLong(_cursorIndexOfLastVisitAt);
            }
            final int _tmpVisitCount;
            _tmpVisitCount = _cursor.getInt(_cursorIndexOfVisitCount);
            final int _tmpTotalHookahs;
            _tmpTotalHookahs = _cursor.getInt(_cursorIndexOfTotalHookahs);
            final String _tmpFavoriteBrands;
            _tmpFavoriteBrands = _cursor.getString(_cursorIndexOfFavoriteBrands);
            final String _tmpFavoriteFlavors;
            _tmpFavoriteFlavors = _cursor.getString(_cursorIndexOfFavoriteFlavors);
            final boolean _tmpIsArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new GuestEntity(_tmpId,_tmpName,_tmpPhone,_tmpBirthday,_tmpTelegram,_tmpComment,_tmpPreferredStrength,_tmpPreferredBowlType,_tmpPreferredDescriptors,_tmpDislikedFlavors,_tmpVipStatus,_tmpFirstVisitAt,_tmpLastVisitAt,_tmpVisitCount,_tmpTotalHookahs,_tmpFavoriteBrands,_tmpFavoriteFlavors,_tmpIsArchived,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getVisits(final long guestId,
      final Continuation<? super List<VisitEntity>> $completion) {
    final String _sql = "SELECT * FROM visits WHERE guestId = ? ORDER BY startedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<VisitEntity>>() {
      @Override
      @NonNull
      public List<VisitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGuestId = CursorUtil.getColumnIndexOrThrow(_cursor, "guestId");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAt");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<VisitEntity> _result = new ArrayList<VisitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VisitEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpGuestId;
            if (_cursor.isNull(_cursorIndexOfGuestId)) {
              _tmpGuestId = null;
            } else {
              _tmpGuestId = _cursor.getLong(_cursorIndexOfGuestId);
            }
            final Long _tmpEmployeeId;
            if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
              _tmpEmployeeId = null;
            } else {
              _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            }
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new VisitEntity(_tmpId,_tmpGuestId,_tmpEmployeeId,_tmpTableNumber,_tmpStartedAt,_tmpClosedAt,_tmpStatus,_tmpNotes);
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
  public Object getHookahsForVisit(final long visitId,
      final Continuation<? super List<HookahHistoryEntity>> $completion) {
    final String _sql = "SELECT * FROM hookah_history WHERE visitId = ? ORDER BY startedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, visitId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HookahHistoryEntity>>() {
      @Override
      @NonNull
      public List<HookahHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVisitId = CursorUtil.getColumnIndexOrThrow(_cursor, "visitId");
          final int _cursorIndexOfGuestId = CursorUtil.getColumnIndexOrThrow(_cursor, "guestId");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfBowlType = CursorUtil.getColumnIndexOrThrow(_cursor, "bowlType");
          final int _cursorIndexOfStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "strength");
          final int _cursorIndexOfHookahCount = CursorUtil.getColumnIndexOrThrow(_cursor, "hookahCount");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAt");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final List<HookahHistoryEntity> _result = new ArrayList<HookahHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HookahHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVisitId;
            _tmpVisitId = _cursor.getLong(_cursorIndexOfVisitId);
            final Long _tmpGuestId;
            if (_cursor.isNull(_cursorIndexOfGuestId)) {
              _tmpGuestId = null;
            } else {
              _tmpGuestId = _cursor.getLong(_cursorIndexOfGuestId);
            }
            final Long _tmpEmployeeId;
            if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
              _tmpEmployeeId = null;
            } else {
              _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            }
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final String _tmpBowlType;
            _tmpBowlType = _cursor.getString(_cursorIndexOfBowlType);
            final int _tmpStrength;
            _tmpStrength = _cursor.getInt(_cursorIndexOfStrength);
            final int _tmpHookahCount;
            _tmpHookahCount = _cursor.getInt(_cursorIndexOfHookahCount);
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpComment;
            _tmpComment = _cursor.getString(_cursorIndexOfComment);
            _item = new HookahHistoryEntity(_tmpId,_tmpVisitId,_tmpGuestId,_tmpEmployeeId,_tmpTableNumber,_tmpBowlType,_tmpStrength,_tmpHookahCount,_tmpStartedAt,_tmpClosedAt,_tmpStatus,_tmpComment);
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
  public Object getMixItemsForHookah(final long hookahId,
      final Continuation<? super List<HookahMixItemEntity>> $completion) {
    final String _sql = "SELECT * FROM hookah_mix_items WHERE hookahId = ? ORDER BY percentage DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, hookahId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HookahMixItemEntity>>() {
      @Override
      @NonNull
      public List<HookahMixItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHookahId = CursorUtil.getColumnIndexOrThrow(_cursor, "hookahId");
          final int _cursorIndexOfFlavorId = CursorUtil.getColumnIndexOrThrow(_cursor, "flavorId");
          final int _cursorIndexOfBrandNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "brandNameSnapshot");
          final int _cursorIndexOfFlavorNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "flavorNameSnapshot");
          final int _cursorIndexOfDescriptorSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptorSnapshot");
          final int _cursorIndexOfPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "percentage");
          final int _cursorIndexOfGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "grams");
          final List<HookahMixItemEntity> _result = new ArrayList<HookahMixItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HookahMixItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHookahId;
            _tmpHookahId = _cursor.getLong(_cursorIndexOfHookahId);
            final Long _tmpFlavorId;
            if (_cursor.isNull(_cursorIndexOfFlavorId)) {
              _tmpFlavorId = null;
            } else {
              _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            }
            final String _tmpBrandNameSnapshot;
            _tmpBrandNameSnapshot = _cursor.getString(_cursorIndexOfBrandNameSnapshot);
            final String _tmpFlavorNameSnapshot;
            _tmpFlavorNameSnapshot = _cursor.getString(_cursorIndexOfFlavorNameSnapshot);
            final String _tmpDescriptorSnapshot;
            _tmpDescriptorSnapshot = _cursor.getString(_cursorIndexOfDescriptorSnapshot);
            final int _tmpPercentage;
            _tmpPercentage = _cursor.getInt(_cursorIndexOfPercentage);
            final double _tmpGrams;
            _tmpGrams = _cursor.getDouble(_cursorIndexOfGrams);
            _item = new HookahMixItemEntity(_tmpId,_tmpHookahId,_tmpFlavorId,_tmpBrandNameSnapshot,_tmpFlavorNameSnapshot,_tmpDescriptorSnapshot,_tmpPercentage,_tmpGrams);
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
  public Object favoriteBrands(final long guestId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "\n"
            + "        SELECT brandNameSnapshot FROM hookah_mix_items m\n"
            + "        INNER JOIN hookah_history h ON h.id = m.hookahId\n"
            + "        WHERE h.guestId = ?\n"
            + "        GROUP BY brandNameSnapshot\n"
            + "        ORDER BY SUM(m.grams) DESC\n"
            + "        LIMIT 3\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Object favoriteFlavors(final long guestId,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "\n"
            + "        SELECT flavorNameSnapshot FROM hookah_mix_items m\n"
            + "        INNER JOIN hookah_history h ON h.id = m.hookahId\n"
            + "        WHERE h.guestId = ?\n"
            + "        GROUP BY flavorNameSnapshot\n"
            + "        ORDER BY SUM(m.grams) DESC\n"
            + "        LIMIT 5\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            _item = _cursor.getString(0);
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
  public Object favoriteStrength(final long guestId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT strength FROM hookah_history\n"
            + "        WHERE guestId = ?\n"
            + "        GROUP BY strength\n"
            + "        ORDER BY COUNT(*) DESC, MAX(startedAt) DESC\n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getInt(0);
            }
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
  public Flow<List<VisitEntity>> observeVisits(final long guestId) {
    final String _sql = "SELECT * FROM visits WHERE guestId = ? ORDER BY startedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"visits"}, new Callable<List<VisitEntity>>() {
      @Override
      @NonNull
      public List<VisitEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGuestId = CursorUtil.getColumnIndexOrThrow(_cursor, "guestId");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAt");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<VisitEntity> _result = new ArrayList<VisitEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final VisitEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpGuestId;
            if (_cursor.isNull(_cursorIndexOfGuestId)) {
              _tmpGuestId = null;
            } else {
              _tmpGuestId = _cursor.getLong(_cursorIndexOfGuestId);
            }
            final Long _tmpEmployeeId;
            if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
              _tmpEmployeeId = null;
            } else {
              _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            }
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpNotes;
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            _item = new VisitEntity(_tmpId,_tmpGuestId,_tmpEmployeeId,_tmpTableNumber,_tmpStartedAt,_tmpClosedAt,_tmpStatus,_tmpNotes);
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
  public Flow<List<HookahHistoryEntity>> observeHookahHistory(final long guestId) {
    final String _sql = "SELECT * FROM hookah_history WHERE guestId = ? ORDER BY startedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, guestId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"hookah_history"}, new Callable<List<HookahHistoryEntity>>() {
      @Override
      @NonNull
      public List<HookahHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVisitId = CursorUtil.getColumnIndexOrThrow(_cursor, "visitId");
          final int _cursorIndexOfGuestId = CursorUtil.getColumnIndexOrThrow(_cursor, "guestId");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfBowlType = CursorUtil.getColumnIndexOrThrow(_cursor, "bowlType");
          final int _cursorIndexOfStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "strength");
          final int _cursorIndexOfHookahCount = CursorUtil.getColumnIndexOrThrow(_cursor, "hookahCount");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closedAt");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final List<HookahHistoryEntity> _result = new ArrayList<HookahHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HookahHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVisitId;
            _tmpVisitId = _cursor.getLong(_cursorIndexOfVisitId);
            final Long _tmpGuestId;
            if (_cursor.isNull(_cursorIndexOfGuestId)) {
              _tmpGuestId = null;
            } else {
              _tmpGuestId = _cursor.getLong(_cursorIndexOfGuestId);
            }
            final Long _tmpEmployeeId;
            if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
              _tmpEmployeeId = null;
            } else {
              _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            }
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final String _tmpBowlType;
            _tmpBowlType = _cursor.getString(_cursorIndexOfBowlType);
            final int _tmpStrength;
            _tmpStrength = _cursor.getInt(_cursorIndexOfStrength);
            final int _tmpHookahCount;
            _tmpHookahCount = _cursor.getInt(_cursorIndexOfHookahCount);
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpComment;
            _tmpComment = _cursor.getString(_cursorIndexOfComment);
            _item = new HookahHistoryEntity(_tmpId,_tmpVisitId,_tmpGuestId,_tmpEmployeeId,_tmpTableNumber,_tmpBowlType,_tmpStrength,_tmpHookahCount,_tmpStartedAt,_tmpClosedAt,_tmpStatus,_tmpComment);
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
  public Flow<List<HookahMixItemEntity>> observeMixItems(final long hookahId) {
    final String _sql = "SELECT * FROM hookah_mix_items WHERE hookahId = ? ORDER BY percentage DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, hookahId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"hookah_mix_items"}, new Callable<List<HookahMixItemEntity>>() {
      @Override
      @NonNull
      public List<HookahMixItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHookahId = CursorUtil.getColumnIndexOrThrow(_cursor, "hookahId");
          final int _cursorIndexOfFlavorId = CursorUtil.getColumnIndexOrThrow(_cursor, "flavorId");
          final int _cursorIndexOfBrandNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "brandNameSnapshot");
          final int _cursorIndexOfFlavorNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "flavorNameSnapshot");
          final int _cursorIndexOfDescriptorSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "descriptorSnapshot");
          final int _cursorIndexOfPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "percentage");
          final int _cursorIndexOfGrams = CursorUtil.getColumnIndexOrThrow(_cursor, "grams");
          final List<HookahMixItemEntity> _result = new ArrayList<HookahMixItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HookahMixItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHookahId;
            _tmpHookahId = _cursor.getLong(_cursorIndexOfHookahId);
            final Long _tmpFlavorId;
            if (_cursor.isNull(_cursorIndexOfFlavorId)) {
              _tmpFlavorId = null;
            } else {
              _tmpFlavorId = _cursor.getLong(_cursorIndexOfFlavorId);
            }
            final String _tmpBrandNameSnapshot;
            _tmpBrandNameSnapshot = _cursor.getString(_cursorIndexOfBrandNameSnapshot);
            final String _tmpFlavorNameSnapshot;
            _tmpFlavorNameSnapshot = _cursor.getString(_cursorIndexOfFlavorNameSnapshot);
            final String _tmpDescriptorSnapshot;
            _tmpDescriptorSnapshot = _cursor.getString(_cursorIndexOfDescriptorSnapshot);
            final int _tmpPercentage;
            _tmpPercentage = _cursor.getInt(_cursorIndexOfPercentage);
            final double _tmpGrams;
            _tmpGrams = _cursor.getDouble(_cursorIndexOfGrams);
            _item = new HookahMixItemEntity(_tmpId,_tmpHookahId,_tmpFlavorId,_tmpBrandNameSnapshot,_tmpFlavorNameSnapshot,_tmpDescriptorSnapshot,_tmpPercentage,_tmpGrams);
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
  public Flow<List<CoalChangeEntity>> observeCoalChanges(final long hookahId) {
    final String _sql = "SELECT * FROM coal_changes WHERE hookahId = ? ORDER BY changedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, hookahId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"coal_changes"}, new Callable<List<CoalChangeEntity>>() {
      @Override
      @NonNull
      public List<CoalChangeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHookahId = CursorUtil.getColumnIndexOrThrow(_cursor, "hookahId");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfChangedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "changedAt");
          final int _cursorIndexOfSequenceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "sequenceNumber");
          final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
          final List<CoalChangeEntity> _result = new ArrayList<CoalChangeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CoalChangeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHookahId;
            _tmpHookahId = _cursor.getLong(_cursorIndexOfHookahId);
            final Long _tmpEmployeeId;
            if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
              _tmpEmployeeId = null;
            } else {
              _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            }
            final long _tmpChangedAt;
            _tmpChangedAt = _cursor.getLong(_cursorIndexOfChangedAt);
            final int _tmpSequenceNumber;
            _tmpSequenceNumber = _cursor.getInt(_cursorIndexOfSequenceNumber);
            final String _tmpComment;
            _tmpComment = _cursor.getString(_cursorIndexOfComment);
            _item = new CoalChangeEntity(_tmpId,_tmpHookahId,_tmpEmployeeId,_tmpChangedAt,_tmpSequenceNumber,_tmpComment);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
