package ru.igni.manager.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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
public final class HallDao_Impl implements HallDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ActiveTableSessionEntity> __insertionAdapterOfActiveTableSessionEntity;

  private final EntityInsertionAdapter<FavoriteMixEntity> __insertionAdapterOfFavoriteMixEntity;

  private final EntityDeletionOrUpdateAdapter<FavoriteMixEntity> __deletionAdapterOfFavoriteMixEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSession;

  public HallDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfActiveTableSessionEntity = new EntityInsertionAdapter<ActiveTableSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `active_table_sessions` (`tableNumber`,`guestName`,`guestId`,`bowlType`,`strength`,`hookahCount`,`mixData`,`startedAt`,`lastCoalAt`,`coalChanges`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ActiveTableSessionEntity entity) {
        statement.bindLong(1, entity.getTableNumber());
        statement.bindString(2, entity.getGuestName());
        if (entity.getGuestId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getGuestId());
        }
        statement.bindString(4, entity.getBowlType());
        statement.bindLong(5, entity.getStrength());
        statement.bindLong(6, entity.getHookahCount());
        statement.bindString(7, entity.getMixData());
        statement.bindLong(8, entity.getStartedAt());
        statement.bindLong(9, entity.getLastCoalAt());
        statement.bindLong(10, entity.getCoalChanges());
      }
    };
    this.__insertionAdapterOfFavoriteMixEntity = new EntityInsertionAdapter<FavoriteMixEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `favorite_mixes` (`id`,`name`,`mixData`,`createdAt`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteMixEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getMixData());
        statement.bindLong(4, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfFavoriteMixEntity = new EntityDeletionOrUpdateAdapter<FavoriteMixEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `favorite_mixes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FavoriteMixEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSession = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM active_table_sessions WHERE tableNumber = ?";
        return _query;
      }
    };
  }

  @Override
  public Object saveSession(final ActiveTableSessionEntity session,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfActiveTableSessionEntity.insert(session);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertFavorite(final FavoriteMixEntity favorite,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFavoriteMixEntity.insertAndReturnId(favorite);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteFavorite(final FavoriteMixEntity favorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFavoriteMixEntity.handle(favorite);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSession(final int tableNumber, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSession.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, tableNumber);
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
          __preparedStmtOfDeleteSession.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ActiveTableSessionEntity>> observeActiveSessions() {
    final String _sql = "SELECT * FROM active_table_sessions ORDER BY tableNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"active_table_sessions"}, new Callable<List<ActiveTableSessionEntity>>() {
      @Override
      @NonNull
      public List<ActiveTableSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfGuestName = CursorUtil.getColumnIndexOrThrow(_cursor, "guestName");
          final int _cursorIndexOfGuestId = CursorUtil.getColumnIndexOrThrow(_cursor, "guestId");
          final int _cursorIndexOfBowlType = CursorUtil.getColumnIndexOrThrow(_cursor, "bowlType");
          final int _cursorIndexOfStrength = CursorUtil.getColumnIndexOrThrow(_cursor, "strength");
          final int _cursorIndexOfHookahCount = CursorUtil.getColumnIndexOrThrow(_cursor, "hookahCount");
          final int _cursorIndexOfMixData = CursorUtil.getColumnIndexOrThrow(_cursor, "mixData");
          final int _cursorIndexOfStartedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAt");
          final int _cursorIndexOfLastCoalAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastCoalAt");
          final int _cursorIndexOfCoalChanges = CursorUtil.getColumnIndexOrThrow(_cursor, "coalChanges");
          final List<ActiveTableSessionEntity> _result = new ArrayList<ActiveTableSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ActiveTableSessionEntity _item;
            final int _tmpTableNumber;
            _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            final String _tmpGuestName;
            _tmpGuestName = _cursor.getString(_cursorIndexOfGuestName);
            final Long _tmpGuestId;
            if (_cursor.isNull(_cursorIndexOfGuestId)) {
              _tmpGuestId = null;
            } else {
              _tmpGuestId = _cursor.getLong(_cursorIndexOfGuestId);
            }
            final String _tmpBowlType;
            _tmpBowlType = _cursor.getString(_cursorIndexOfBowlType);
            final int _tmpStrength;
            _tmpStrength = _cursor.getInt(_cursorIndexOfStrength);
            final int _tmpHookahCount;
            _tmpHookahCount = _cursor.getInt(_cursorIndexOfHookahCount);
            final String _tmpMixData;
            _tmpMixData = _cursor.getString(_cursorIndexOfMixData);
            final long _tmpStartedAt;
            _tmpStartedAt = _cursor.getLong(_cursorIndexOfStartedAt);
            final long _tmpLastCoalAt;
            _tmpLastCoalAt = _cursor.getLong(_cursorIndexOfLastCoalAt);
            final int _tmpCoalChanges;
            _tmpCoalChanges = _cursor.getInt(_cursorIndexOfCoalChanges);
            _item = new ActiveTableSessionEntity(_tmpTableNumber,_tmpGuestName,_tmpGuestId,_tmpBowlType,_tmpStrength,_tmpHookahCount,_tmpMixData,_tmpStartedAt,_tmpLastCoalAt,_tmpCoalChanges);
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
  public Flow<List<FavoriteMixEntity>> observeFavoriteMixes() {
    final String _sql = "SELECT * FROM favorite_mixes ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"favorite_mixes"}, new Callable<List<FavoriteMixEntity>>() {
      @Override
      @NonNull
      public List<FavoriteMixEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfMixData = CursorUtil.getColumnIndexOrThrow(_cursor, "mixData");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<FavoriteMixEntity> _result = new ArrayList<FavoriteMixEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FavoriteMixEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpMixData;
            _tmpMixData = _cursor.getString(_cursorIndexOfMixData);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new FavoriteMixEntity(_tmpId,_tmpName,_tmpMixData,_tmpCreatedAt);
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
