package com.example.lifeos.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.lifeos.data.local.Converters;
import com.example.lifeos.data.local.entity.EventEntity;
import com.example.lifeos.data.local.entity.EventType;
import com.example.lifeos.data.local.entity.MessagePlatform;
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
public final class EventDao_Impl implements EventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EventEntity> __insertionAdapterOfEventEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<EventEntity> __deletionAdapterOfEventEntity;

  private final EntityDeletionOrUpdateAdapter<EventEntity> __updateAdapterOfEventEntity;

  public EventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEventEntity = new EntityInsertionAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `events` (`id`,`title`,`date`,`type`,`reminderMinutes`,`isRecursive`,`scheduledMessageBody`,`scheduledMessageContact`,`customCategory`,`description`,`scheduledMessageId`,`scheduledMessagePlatform`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindString(4, __EventType_enumToString(entity.getType()));
        statement.bindLong(5, entity.getReminderMinutes());
        final int _tmp_1 = entity.isRecursive() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getScheduledMessageBody() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getScheduledMessageBody());
        }
        if (entity.getScheduledMessageContact() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getScheduledMessageContact());
        }
        if (entity.getCustomCategory() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCustomCategory());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDescription());
        }
        if (entity.getScheduledMessageId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getScheduledMessageId());
        }
        if (entity.getScheduledMessagePlatform() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, __MessagePlatform_enumToString(entity.getScheduledMessagePlatform()));
        }
      }
    };
    this.__deletionAdapterOfEventEntity = new EntityDeletionOrUpdateAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `events` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfEventEntity = new EntityDeletionOrUpdateAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `events` SET `id` = ?,`title` = ?,`date` = ?,`type` = ?,`reminderMinutes` = ?,`isRecursive` = ?,`scheduledMessageBody` = ?,`scheduledMessageContact` = ?,`customCategory` = ?,`description` = ?,`scheduledMessageId` = ?,`scheduledMessagePlatform` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, _tmp);
        }
        statement.bindString(4, __EventType_enumToString(entity.getType()));
        statement.bindLong(5, entity.getReminderMinutes());
        final int _tmp_1 = entity.isRecursive() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        if (entity.getScheduledMessageBody() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getScheduledMessageBody());
        }
        if (entity.getScheduledMessageContact() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getScheduledMessageContact());
        }
        if (entity.getCustomCategory() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCustomCategory());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDescription());
        }
        if (entity.getScheduledMessageId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getScheduledMessageId());
        }
        if (entity.getScheduledMessagePlatform() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, __MessagePlatform_enumToString(entity.getScheduledMessagePlatform()));
        }
        statement.bindLong(13, entity.getId());
      }
    };
  }

  @Override
  public Object insertEvent(final EventEntity event, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteEvent(final EventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateEvent(final EventEntity event, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EventEntity>> getAllEvents() {
    final String _sql = "SELECT * FROM events ORDER BY date ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfReminderMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinutes");
          final int _cursorIndexOfIsRecursive = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecursive");
          final int _cursorIndexOfScheduledMessageBody = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledMessageBody");
          final int _cursorIndexOfScheduledMessageContact = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledMessageContact");
          final int _cursorIndexOfCustomCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "customCategory");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfScheduledMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledMessageId");
          final int _cursorIndexOfScheduledMessagePlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledMessagePlatform");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final LocalDateTime _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_1 = __converters.fromTimestamp(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_1;
            }
            final EventType _tmpType;
            _tmpType = __EventType_stringToEnum(_cursor.getString(_cursorIndexOfType));
            final int _tmpReminderMinutes;
            _tmpReminderMinutes = _cursor.getInt(_cursorIndexOfReminderMinutes);
            final boolean _tmpIsRecursive;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsRecursive);
            _tmpIsRecursive = _tmp_2 != 0;
            final String _tmpScheduledMessageBody;
            if (_cursor.isNull(_cursorIndexOfScheduledMessageBody)) {
              _tmpScheduledMessageBody = null;
            } else {
              _tmpScheduledMessageBody = _cursor.getString(_cursorIndexOfScheduledMessageBody);
            }
            final String _tmpScheduledMessageContact;
            if (_cursor.isNull(_cursorIndexOfScheduledMessageContact)) {
              _tmpScheduledMessageContact = null;
            } else {
              _tmpScheduledMessageContact = _cursor.getString(_cursorIndexOfScheduledMessageContact);
            }
            final String _tmpCustomCategory;
            if (_cursor.isNull(_cursorIndexOfCustomCategory)) {
              _tmpCustomCategory = null;
            } else {
              _tmpCustomCategory = _cursor.getString(_cursorIndexOfCustomCategory);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final Long _tmpScheduledMessageId;
            if (_cursor.isNull(_cursorIndexOfScheduledMessageId)) {
              _tmpScheduledMessageId = null;
            } else {
              _tmpScheduledMessageId = _cursor.getLong(_cursorIndexOfScheduledMessageId);
            }
            final MessagePlatform _tmpScheduledMessagePlatform;
            if (_cursor.isNull(_cursorIndexOfScheduledMessagePlatform)) {
              _tmpScheduledMessagePlatform = null;
            } else {
              _tmpScheduledMessagePlatform = __MessagePlatform_stringToEnum(_cursor.getString(_cursorIndexOfScheduledMessagePlatform));
            }
            _item = new EventEntity(_tmpId,_tmpTitle,_tmpDate,_tmpType,_tmpReminderMinutes,_tmpIsRecursive,_tmpScheduledMessageBody,_tmpScheduledMessageContact,_tmpCustomCategory,_tmpDescription,_tmpScheduledMessageId,_tmpScheduledMessagePlatform);
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

  private String __EventType_enumToString(@NonNull final EventType _value) {
    switch (_value) {
      case BIRTHDAY: return "BIRTHDAY";
      case ANNIVERSARY: return "ANNIVERSARY";
      case OTHER: return "OTHER";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private String __MessagePlatform_enumToString(@NonNull final MessagePlatform _value) {
    switch (_value) {
      case WHATSAPP: return "WHATSAPP";
      case SMS: return "SMS";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private EventType __EventType_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "BIRTHDAY": return EventType.BIRTHDAY;
      case "ANNIVERSARY": return EventType.ANNIVERSARY;
      case "OTHER": return EventType.OTHER;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }

  private MessagePlatform __MessagePlatform_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "WHATSAPP": return MessagePlatform.WHATSAPP;
      case "SMS": return MessagePlatform.SMS;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
