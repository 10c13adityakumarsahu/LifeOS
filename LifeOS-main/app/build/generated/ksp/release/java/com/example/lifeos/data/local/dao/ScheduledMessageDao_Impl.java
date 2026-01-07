package com.example.lifeos.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.lifeos.data.local.Converters;
import com.example.lifeos.data.local.entity.MessagePlatform;
import com.example.lifeos.data.local.entity.MessageStatus;
import com.example.lifeos.data.local.entity.ScheduledMessageEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
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
public final class ScheduledMessageDao_Impl implements ScheduledMessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScheduledMessageEntity> __insertionAdapterOfScheduledMessageEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ScheduledMessageEntity> __deletionAdapterOfScheduledMessageEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduledMessageEntity> __updateAdapterOfScheduledMessageEntity;

  public ScheduledMessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScheduledMessageEntity = new EntityInsertionAdapter<ScheduledMessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `scheduled_messages` (`id`,`contactName`,`contactNumber`,`messageBody`,`scheduledTime`,`platform`,`status`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledMessageEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getContactName());
        statement.bindString(3, entity.getContactNumber());
        statement.bindString(4, entity.getMessageBody());
        final Long _tmp = __converters.dateToTimestamp(entity.getScheduledTime());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        statement.bindString(6, __MessagePlatform_enumToString(entity.getPlatform()));
        statement.bindString(7, __MessageStatus_enumToString(entity.getStatus()));
      }
    };
    this.__deletionAdapterOfScheduledMessageEntity = new EntityDeletionOrUpdateAdapter<ScheduledMessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `scheduled_messages` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledMessageEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfScheduledMessageEntity = new EntityDeletionOrUpdateAdapter<ScheduledMessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `scheduled_messages` SET `id` = ?,`contactName` = ?,`contactNumber` = ?,`messageBody` = ?,`scheduledTime` = ?,`platform` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduledMessageEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getContactName());
        statement.bindString(3, entity.getContactNumber());
        statement.bindString(4, entity.getMessageBody());
        final Long _tmp = __converters.dateToTimestamp(entity.getScheduledTime());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        statement.bindString(6, __MessagePlatform_enumToString(entity.getPlatform()));
        statement.bindString(7, __MessageStatus_enumToString(entity.getStatus()));
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertMessage(final ScheduledMessageEntity message,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScheduledMessageEntity.insertAndReturnId(message);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMessage(final ScheduledMessageEntity message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfScheduledMessageEntity.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMessage(final ScheduledMessageEntity message,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfScheduledMessageEntity.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ScheduledMessageEntity>> getAllMessages() {
    final String _sql = "SELECT * FROM scheduled_messages ORDER BY scheduledTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"scheduled_messages"}, new Callable<List<ScheduledMessageEntity>>() {
      @Override
      @NonNull
      public List<ScheduledMessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final int _cursorIndexOfContactNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "contactNumber");
          final int _cursorIndexOfMessageBody = CursorUtil.getColumnIndexOrThrow(_cursor, "messageBody");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<ScheduledMessageEntity> _result = new ArrayList<ScheduledMessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduledMessageEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpContactName;
            _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            final String _tmpContactNumber;
            _tmpContactNumber = _cursor.getString(_cursorIndexOfContactNumber);
            final String _tmpMessageBody;
            _tmpMessageBody = _cursor.getString(_cursorIndexOfMessageBody);
            final LocalDateTime _tmpScheduledTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfScheduledTime);
            }
            final LocalDateTime _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpScheduledTime = _tmp_1;
            }
            final MessagePlatform _tmpPlatform;
            _tmpPlatform = __MessagePlatform_stringToEnum(_cursor.getString(_cursorIndexOfPlatform));
            final MessageStatus _tmpStatus;
            _tmpStatus = __MessageStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            _item = new ScheduledMessageEntity(_tmpId,_tmpContactName,_tmpContactNumber,_tmpMessageBody,_tmpScheduledTime,_tmpPlatform,_tmpStatus);
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
  public Object getMessageById(final int id,
      final Continuation<? super ScheduledMessageEntity> $completion) {
    final String _sql = "SELECT * FROM scheduled_messages WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduledMessageEntity>() {
      @Override
      @Nullable
      public ScheduledMessageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfContactName = CursorUtil.getColumnIndexOrThrow(_cursor, "contactName");
          final int _cursorIndexOfContactNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "contactNumber");
          final int _cursorIndexOfMessageBody = CursorUtil.getColumnIndexOrThrow(_cursor, "messageBody");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final ScheduledMessageEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpContactName;
            _tmpContactName = _cursor.getString(_cursorIndexOfContactName);
            final String _tmpContactNumber;
            _tmpContactNumber = _cursor.getString(_cursorIndexOfContactNumber);
            final String _tmpMessageBody;
            _tmpMessageBody = _cursor.getString(_cursorIndexOfMessageBody);
            final LocalDateTime _tmpScheduledTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfScheduledTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfScheduledTime);
            }
            final LocalDateTime _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpScheduledTime = _tmp_1;
            }
            final MessagePlatform _tmpPlatform;
            _tmpPlatform = __MessagePlatform_stringToEnum(_cursor.getString(_cursorIndexOfPlatform));
            final MessageStatus _tmpStatus;
            _tmpStatus = __MessageStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            _result = new ScheduledMessageEntity(_tmpId,_tmpContactName,_tmpContactNumber,_tmpMessageBody,_tmpScheduledTime,_tmpPlatform,_tmpStatus);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __MessagePlatform_enumToString(@NonNull final MessagePlatform _value) {
    switch (_value) {
      case WHATSAPP: return "WHATSAPP";
      case SMS: return "SMS";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __MessageStatus_enumToString(@NonNull final MessageStatus _value) {
    switch (_value) {
      case PENDING: return "PENDING";
      case SENT: return "SENT";
      case FAILED: return "FAILED";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private MessagePlatform __MessagePlatform_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "WHATSAPP": return MessagePlatform.WHATSAPP;
      case "SMS": return MessagePlatform.SMS;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private MessageStatus __MessageStatus_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "PENDING": return MessageStatus.PENDING;
      case "SENT": return MessageStatus.SENT;
      case "FAILED": return MessageStatus.FAILED;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
