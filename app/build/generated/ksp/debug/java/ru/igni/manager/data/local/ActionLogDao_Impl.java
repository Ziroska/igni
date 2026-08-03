package ru.igni.manager.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ActionLogDao_Impl implements ActionLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ActionLogEntity> __insertionAdapterOfActionLogEntity;

  public ActionLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfActionLogEntity = new EntityInsertionAdapter<ActionLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `action_log` (`id`,`employeeId`,`actionType`,`entityType`,`entityId`,`tableNumber`,`details`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ActionLogEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getEmployeeId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getEmployeeId());
        }
        statement.bindString(3, entity.getActionType());
        statement.bindString(4, entity.getEntityType());
        if (entity.getEntityId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEntityId());
        }
        if (entity.getTableNumber() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getTableNumber());
        }
        statement.bindString(7, entity.getDetails());
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final ActionLogEntity entry, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfActionLogEntity.insertAndReturnId(entry);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ActionLogEntity>> observeRecent(final int limit) {
    final String _sql = "SELECT * FROM action_log ORDER BY createdAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"action_log"}, new Callable<List<ActionLogEntity>>() {
      @Override
      @NonNull
      public List<ActionLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfActionType = CursorUtil.getColumnIndexOrThrow(_cursor, "actionType");
          final int _cursorIndexOfEntityType = CursorUtil.getColumnIndexOrThrow(_cursor, "entityType");
          final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entityId");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "details");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ActionLogEntity> _result = new ArrayList<ActionLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ActionLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpEmployeeId;
            if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
              _tmpEmployeeId = null;
            } else {
              _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            }
            final String _tmpActionType;
            _tmpActionType = _cursor.getString(_cursorIndexOfActionType);
            final String _tmpEntityType;
            _tmpEntityType = _cursor.getString(_cursorIndexOfEntityType);
            final Long _tmpEntityId;
            if (_cursor.isNull(_cursorIndexOfEntityId)) {
              _tmpEntityId = null;
            } else {
              _tmpEntityId = _cursor.getLong(_cursorIndexOfEntityId);
            }
            final Integer _tmpTableNumber;
            if (_cursor.isNull(_cursorIndexOfTableNumber)) {
              _tmpTableNumber = null;
            } else {
              _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            }
            final String _tmpDetails;
            _tmpDetails = _cursor.getString(_cursorIndexOfDetails);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ActionLogEntity(_tmpId,_tmpEmployeeId,_tmpActionType,_tmpEntityType,_tmpEntityId,_tmpTableNumber,_tmpDetails,_tmpCreatedAt);
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
  public Flow<List<ActionLogEntity>> observeForTable(final int tableNumber) {
    final String _sql = "SELECT * FROM action_log WHERE tableNumber = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tableNumber);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"action_log"}, new Callable<List<ActionLogEntity>>() {
      @Override
      @NonNull
      public List<ActionLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEmployeeId = CursorUtil.getColumnIndexOrThrow(_cursor, "employeeId");
          final int _cursorIndexOfActionType = CursorUtil.getColumnIndexOrThrow(_cursor, "actionType");
          final int _cursorIndexOfEntityType = CursorUtil.getColumnIndexOrThrow(_cursor, "entityType");
          final int _cursorIndexOfEntityId = CursorUtil.getColumnIndexOrThrow(_cursor, "entityId");
          final int _cursorIndexOfTableNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "tableNumber");
          final int _cursorIndexOfDetails = CursorUtil.getColumnIndexOrThrow(_cursor, "details");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<ActionLogEntity> _result = new ArrayList<ActionLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ActionLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Long _tmpEmployeeId;
            if (_cursor.isNull(_cursorIndexOfEmployeeId)) {
              _tmpEmployeeId = null;
            } else {
              _tmpEmployeeId = _cursor.getLong(_cursorIndexOfEmployeeId);
            }
            final String _tmpActionType;
            _tmpActionType = _cursor.getString(_cursorIndexOfActionType);
            final String _tmpEntityType;
            _tmpEntityType = _cursor.getString(_cursorIndexOfEntityType);
            final Long _tmpEntityId;
            if (_cursor.isNull(_cursorIndexOfEntityId)) {
              _tmpEntityId = null;
            } else {
              _tmpEntityId = _cursor.getLong(_cursorIndexOfEntityId);
            }
            final Integer _tmpTableNumber;
            if (_cursor.isNull(_cursorIndexOfTableNumber)) {
              _tmpTableNumber = null;
            } else {
              _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
            }
            final String _tmpDetails;
            _tmpDetails = _cursor.getString(_cursorIndexOfDetails);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ActionLogEntity(_tmpId,_tmpEmployeeId,_tmpActionType,_tmpEntityType,_tmpEntityId,_tmpTableNumber,_tmpDetails,_tmpCreatedAt);
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
