package ru.igni.manager.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
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
public final class EmployeeDao_Impl implements EmployeeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EmployeeEntity> __insertionAdapterOfEmployeeEntity;

  private final SharedSQLiteStatement __preparedStmtOfArchive;

  public EmployeeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEmployeeEntity = new EntityInsertionAdapter<EmployeeEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `employees` (`id`,`name`,`role`,`active`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EmployeeEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getRole());
        final int _tmp = entity.getActive() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getCreatedAt());
      }
    };
    this.__preparedStmtOfArchive = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE employees SET active = 0 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final EmployeeEntity employee,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEmployeeEntity.insertAndReturnId(employee);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object archive(final long employeeId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfArchive.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, employeeId);
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
          __preparedStmtOfArchive.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EmployeeEntity>> observeAll() {
    final String _sql = "SELECT * FROM employees ORDER BY active DESC, name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"employees"}, new Callable<List<EmployeeEntity>>() {
      @Override
      @NonNull
      public List<EmployeeEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<EmployeeEntity> _result = new ArrayList<EmployeeEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EmployeeEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpRole;
            _tmpRole = _cursor.getString(_cursorIndexOfRole);
            final boolean _tmpActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActive);
            _tmpActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new EmployeeEntity(_tmpId,_tmpName,_tmpRole,_tmpActive,_tmpCreatedAt);
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
