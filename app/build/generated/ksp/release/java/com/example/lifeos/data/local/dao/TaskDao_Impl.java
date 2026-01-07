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
import com.example.lifeos.data.local.entity.Priority;
import com.example.lifeos.data.local.entity.Recurrence;
import com.example.lifeos.data.local.entity.TaskCategory;
import com.example.lifeos.data.local.entity.TaskEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TaskEntity> __insertionAdapterOfTaskEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __deletionAdapterOfTaskEntity;

  private final EntityDeletionOrUpdateAdapter<TaskEntity> __updateAdapterOfTaskEntity;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTaskEntity = new EntityInsertionAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `tasks` (`id`,`title`,`description`,`startTime`,`endTime`,`priority`,`category`,`flexibilityMinutes`,`estimatedDurationMinutes`,`isCompleted`,`isSkipped`,`recurrence`,`autoReschedule`,`thresholdTime`,`isGoal`,`hasSpawnedRecurrence`,`spawnedTaskId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        final Long _tmp = __converters.dateToTimestamp(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp_1);
        }
        final String _tmp_2 = __converters.priorityToString(entity.getPriority());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
        final String _tmp_3 = __converters.taskCategoryToString(entity.getCategory());
        if (_tmp_3 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_3);
        }
        statement.bindLong(8, entity.getFlexibilityMinutes());
        statement.bindLong(9, entity.getEstimatedDurationMinutes());
        final int _tmp_4 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(10, _tmp_4);
        final int _tmp_5 = entity.isSkipped() ? 1 : 0;
        statement.bindLong(11, _tmp_5);
        final String _tmp_6 = __converters.recurrenceToString(entity.getRecurrence());
        if (_tmp_6 == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, _tmp_6);
        }
        final int _tmp_7 = entity.getAutoReschedule() ? 1 : 0;
        statement.bindLong(13, _tmp_7);
        final String _tmp_8 = __converters.localTimeToString(entity.getThresholdTime());
        if (_tmp_8 == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, _tmp_8);
        }
        final int _tmp_9 = entity.isGoal() ? 1 : 0;
        statement.bindLong(15, _tmp_9);
        final int _tmp_10 = entity.getHasSpawnedRecurrence() ? 1 : 0;
        statement.bindLong(16, _tmp_10);
        if (entity.getSpawnedTaskId() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getSpawnedTaskId());
        }
      }
    };
    this.__deletionAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tasks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTaskEntity = new EntityDeletionOrUpdateAdapter<TaskEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tasks` SET `id` = ?,`title` = ?,`description` = ?,`startTime` = ?,`endTime` = ?,`priority` = ?,`category` = ?,`flexibilityMinutes` = ?,`estimatedDurationMinutes` = ?,`isCompleted` = ?,`isSkipped` = ?,`recurrence` = ?,`autoReschedule` = ?,`thresholdTime` = ?,`isGoal` = ?,`hasSpawnedRecurrence` = ?,`spawnedTaskId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TaskEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getDescription());
        final Long _tmp = __converters.dateToTimestamp(entity.getStartTime());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getEndTime());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp_1);
        }
        final String _tmp_2 = __converters.priorityToString(entity.getPriority());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
        final String _tmp_3 = __converters.taskCategoryToString(entity.getCategory());
        if (_tmp_3 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_3);
        }
        statement.bindLong(8, entity.getFlexibilityMinutes());
        statement.bindLong(9, entity.getEstimatedDurationMinutes());
        final int _tmp_4 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(10, _tmp_4);
        final int _tmp_5 = entity.isSkipped() ? 1 : 0;
        statement.bindLong(11, _tmp_5);
        final String _tmp_6 = __converters.recurrenceToString(entity.getRecurrence());
        if (_tmp_6 == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, _tmp_6);
        }
        final int _tmp_7 = entity.getAutoReschedule() ? 1 : 0;
        statement.bindLong(13, _tmp_7);
        final String _tmp_8 = __converters.localTimeToString(entity.getThresholdTime());
        if (_tmp_8 == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, _tmp_8);
        }
        final int _tmp_9 = entity.isGoal() ? 1 : 0;
        statement.bindLong(15, _tmp_9);
        final int _tmp_10 = entity.getHasSpawnedRecurrence() ? 1 : 0;
        statement.bindLong(16, _tmp_10);
        if (entity.getSpawnedTaskId() == null) {
          statement.bindNull(17);
        } else {
          statement.bindLong(17, entity.getSpawnedTaskId());
        }
        statement.bindLong(18, entity.getId());
      }
    };
  }

  @Override
  public Object insertTask(final TaskEntity task, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTaskEntity.insertAndReturnId(task);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTask(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTask(final TaskEntity task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTaskEntity.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TaskEntity>> getAllTasks() {
    final String _sql = "SELECT * FROM tasks ORDER BY priority DESC, startTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfFlexibilityMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "flexibilityMinutes");
          final int _cursorIndexOfEstimatedDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedDurationMinutes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsSkipped = CursorUtil.getColumnIndexOrThrow(_cursor, "isSkipped");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfAutoReschedule = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReschedule");
          final int _cursorIndexOfThresholdTime = CursorUtil.getColumnIndexOrThrow(_cursor, "thresholdTime");
          final int _cursorIndexOfIsGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "isGoal");
          final int _cursorIndexOfHasSpawnedRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "hasSpawnedRecurrence");
          final int _cursorIndexOfSpawnedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "spawnedTaskId");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final LocalDateTime _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp);
            final LocalDateTime _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_1);
            final Priority _tmpPriority;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPriority);
            }
            final Priority _tmp_3 = __converters.fromPriority(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Priority', but it was NULL.");
            } else {
              _tmpPriority = _tmp_3;
            }
            final TaskCategory _tmpCategory;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfCategory);
            }
            final TaskCategory _tmp_5 = __converters.fromTaskCategory(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TaskCategory', but it was NULL.");
            } else {
              _tmpCategory = _tmp_5;
            }
            final int _tmpFlexibilityMinutes;
            _tmpFlexibilityMinutes = _cursor.getInt(_cursorIndexOfFlexibilityMinutes);
            final int _tmpEstimatedDurationMinutes;
            _tmpEstimatedDurationMinutes = _cursor.getInt(_cursorIndexOfEstimatedDurationMinutes);
            final boolean _tmpIsCompleted;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_6 != 0;
            final boolean _tmpIsSkipped;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsSkipped);
            _tmpIsSkipped = _tmp_7 != 0;
            final Recurrence _tmpRecurrence;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfRecurrence)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfRecurrence);
            }
            final Recurrence _tmp_9 = __converters.fromRecurrence(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Recurrence', but it was NULL.");
            } else {
              _tmpRecurrence = _tmp_9;
            }
            final boolean _tmpAutoReschedule;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfAutoReschedule);
            _tmpAutoReschedule = _tmp_10 != 0;
            final LocalTime _tmpThresholdTime;
            final String _tmp_11;
            if (_cursor.isNull(_cursorIndexOfThresholdTime)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getString(_cursorIndexOfThresholdTime);
            }
            _tmpThresholdTime = __converters.fromLocalTime(_tmp_11);
            final boolean _tmpIsGoal;
            final int _tmp_12;
            _tmp_12 = _cursor.getInt(_cursorIndexOfIsGoal);
            _tmpIsGoal = _tmp_12 != 0;
            final boolean _tmpHasSpawnedRecurrence;
            final int _tmp_13;
            _tmp_13 = _cursor.getInt(_cursorIndexOfHasSpawnedRecurrence);
            _tmpHasSpawnedRecurrence = _tmp_13 != 0;
            final Integer _tmpSpawnedTaskId;
            if (_cursor.isNull(_cursorIndexOfSpawnedTaskId)) {
              _tmpSpawnedTaskId = null;
            } else {
              _tmpSpawnedTaskId = _cursor.getInt(_cursorIndexOfSpawnedTaskId);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpCategory,_tmpFlexibilityMinutes,_tmpEstimatedDurationMinutes,_tmpIsCompleted,_tmpIsSkipped,_tmpRecurrence,_tmpAutoReschedule,_tmpThresholdTime,_tmpIsGoal,_tmpHasSpawnedRecurrence,_tmpSpawnedTaskId);
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
  public Flow<List<TaskEntity>> getActiveTasks() {
    final String _sql = "SELECT * FROM tasks WHERE isCompleted = 0 AND isSkipped = 0 ORDER BY priority DESC, endTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfFlexibilityMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "flexibilityMinutes");
          final int _cursorIndexOfEstimatedDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedDurationMinutes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsSkipped = CursorUtil.getColumnIndexOrThrow(_cursor, "isSkipped");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfAutoReschedule = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReschedule");
          final int _cursorIndexOfThresholdTime = CursorUtil.getColumnIndexOrThrow(_cursor, "thresholdTime");
          final int _cursorIndexOfIsGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "isGoal");
          final int _cursorIndexOfHasSpawnedRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "hasSpawnedRecurrence");
          final int _cursorIndexOfSpawnedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "spawnedTaskId");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final LocalDateTime _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp);
            final LocalDateTime _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_1);
            final Priority _tmpPriority;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPriority);
            }
            final Priority _tmp_3 = __converters.fromPriority(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Priority', but it was NULL.");
            } else {
              _tmpPriority = _tmp_3;
            }
            final TaskCategory _tmpCategory;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfCategory);
            }
            final TaskCategory _tmp_5 = __converters.fromTaskCategory(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TaskCategory', but it was NULL.");
            } else {
              _tmpCategory = _tmp_5;
            }
            final int _tmpFlexibilityMinutes;
            _tmpFlexibilityMinutes = _cursor.getInt(_cursorIndexOfFlexibilityMinutes);
            final int _tmpEstimatedDurationMinutes;
            _tmpEstimatedDurationMinutes = _cursor.getInt(_cursorIndexOfEstimatedDurationMinutes);
            final boolean _tmpIsCompleted;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_6 != 0;
            final boolean _tmpIsSkipped;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsSkipped);
            _tmpIsSkipped = _tmp_7 != 0;
            final Recurrence _tmpRecurrence;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfRecurrence)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfRecurrence);
            }
            final Recurrence _tmp_9 = __converters.fromRecurrence(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Recurrence', but it was NULL.");
            } else {
              _tmpRecurrence = _tmp_9;
            }
            final boolean _tmpAutoReschedule;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfAutoReschedule);
            _tmpAutoReschedule = _tmp_10 != 0;
            final LocalTime _tmpThresholdTime;
            final String _tmp_11;
            if (_cursor.isNull(_cursorIndexOfThresholdTime)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getString(_cursorIndexOfThresholdTime);
            }
            _tmpThresholdTime = __converters.fromLocalTime(_tmp_11);
            final boolean _tmpIsGoal;
            final int _tmp_12;
            _tmp_12 = _cursor.getInt(_cursorIndexOfIsGoal);
            _tmpIsGoal = _tmp_12 != 0;
            final boolean _tmpHasSpawnedRecurrence;
            final int _tmp_13;
            _tmp_13 = _cursor.getInt(_cursorIndexOfHasSpawnedRecurrence);
            _tmpHasSpawnedRecurrence = _tmp_13 != 0;
            final Integer _tmpSpawnedTaskId;
            if (_cursor.isNull(_cursorIndexOfSpawnedTaskId)) {
              _tmpSpawnedTaskId = null;
            } else {
              _tmpSpawnedTaskId = _cursor.getInt(_cursorIndexOfSpawnedTaskId);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpCategory,_tmpFlexibilityMinutes,_tmpEstimatedDurationMinutes,_tmpIsCompleted,_tmpIsSkipped,_tmpRecurrence,_tmpAutoReschedule,_tmpThresholdTime,_tmpIsGoal,_tmpHasSpawnedRecurrence,_tmpSpawnedTaskId);
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
  public Object getActiveTasksSnapshot(final Continuation<? super List<TaskEntity>> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE isCompleted = 0 AND isSkipped = 0 ORDER BY priority DESC, endTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfFlexibilityMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "flexibilityMinutes");
          final int _cursorIndexOfEstimatedDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedDurationMinutes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsSkipped = CursorUtil.getColumnIndexOrThrow(_cursor, "isSkipped");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfAutoReschedule = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReschedule");
          final int _cursorIndexOfThresholdTime = CursorUtil.getColumnIndexOrThrow(_cursor, "thresholdTime");
          final int _cursorIndexOfIsGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "isGoal");
          final int _cursorIndexOfHasSpawnedRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "hasSpawnedRecurrence");
          final int _cursorIndexOfSpawnedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "spawnedTaskId");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final LocalDateTime _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp);
            final LocalDateTime _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_1);
            final Priority _tmpPriority;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPriority);
            }
            final Priority _tmp_3 = __converters.fromPriority(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Priority', but it was NULL.");
            } else {
              _tmpPriority = _tmp_3;
            }
            final TaskCategory _tmpCategory;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfCategory);
            }
            final TaskCategory _tmp_5 = __converters.fromTaskCategory(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TaskCategory', but it was NULL.");
            } else {
              _tmpCategory = _tmp_5;
            }
            final int _tmpFlexibilityMinutes;
            _tmpFlexibilityMinutes = _cursor.getInt(_cursorIndexOfFlexibilityMinutes);
            final int _tmpEstimatedDurationMinutes;
            _tmpEstimatedDurationMinutes = _cursor.getInt(_cursorIndexOfEstimatedDurationMinutes);
            final boolean _tmpIsCompleted;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_6 != 0;
            final boolean _tmpIsSkipped;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsSkipped);
            _tmpIsSkipped = _tmp_7 != 0;
            final Recurrence _tmpRecurrence;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfRecurrence)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfRecurrence);
            }
            final Recurrence _tmp_9 = __converters.fromRecurrence(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Recurrence', but it was NULL.");
            } else {
              _tmpRecurrence = _tmp_9;
            }
            final boolean _tmpAutoReschedule;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfAutoReschedule);
            _tmpAutoReschedule = _tmp_10 != 0;
            final LocalTime _tmpThresholdTime;
            final String _tmp_11;
            if (_cursor.isNull(_cursorIndexOfThresholdTime)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getString(_cursorIndexOfThresholdTime);
            }
            _tmpThresholdTime = __converters.fromLocalTime(_tmp_11);
            final boolean _tmpIsGoal;
            final int _tmp_12;
            _tmp_12 = _cursor.getInt(_cursorIndexOfIsGoal);
            _tmpIsGoal = _tmp_12 != 0;
            final boolean _tmpHasSpawnedRecurrence;
            final int _tmp_13;
            _tmp_13 = _cursor.getInt(_cursorIndexOfHasSpawnedRecurrence);
            _tmpHasSpawnedRecurrence = _tmp_13 != 0;
            final Integer _tmpSpawnedTaskId;
            if (_cursor.isNull(_cursorIndexOfSpawnedTaskId)) {
              _tmpSpawnedTaskId = null;
            } else {
              _tmpSpawnedTaskId = _cursor.getInt(_cursorIndexOfSpawnedTaskId);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpCategory,_tmpFlexibilityMinutes,_tmpEstimatedDurationMinutes,_tmpIsCompleted,_tmpIsSkipped,_tmpRecurrence,_tmpAutoReschedule,_tmpThresholdTime,_tmpIsGoal,_tmpHasSpawnedRecurrence,_tmpSpawnedTaskId);
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
  public Flow<List<TaskEntity>> getTasksInTimeRange(final LocalDateTime start,
      final LocalDateTime end) {
    final String _sql = "SELECT * FROM tasks WHERE startTime >= ? AND startTime <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp = __converters.dateToTimestamp(start);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1 = __converters.dateToTimestamp(end);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"tasks"}, new Callable<List<TaskEntity>>() {
      @Override
      @NonNull
      public List<TaskEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfFlexibilityMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "flexibilityMinutes");
          final int _cursorIndexOfEstimatedDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedDurationMinutes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsSkipped = CursorUtil.getColumnIndexOrThrow(_cursor, "isSkipped");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfAutoReschedule = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReschedule");
          final int _cursorIndexOfThresholdTime = CursorUtil.getColumnIndexOrThrow(_cursor, "thresholdTime");
          final int _cursorIndexOfIsGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "isGoal");
          final int _cursorIndexOfHasSpawnedRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "hasSpawnedRecurrence");
          final int _cursorIndexOfSpawnedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "spawnedTaskId");
          final List<TaskEntity> _result = new ArrayList<TaskEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TaskEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final LocalDateTime _tmpStartTime;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp_2);
            final LocalDateTime _tmpEndTime;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_3);
            final Priority _tmpPriority;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfPriority);
            }
            final Priority _tmp_5 = __converters.fromPriority(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Priority', but it was NULL.");
            } else {
              _tmpPriority = _tmp_5;
            }
            final TaskCategory _tmpCategory;
            final String _tmp_6;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_6 = null;
            } else {
              _tmp_6 = _cursor.getString(_cursorIndexOfCategory);
            }
            final TaskCategory _tmp_7 = __converters.fromTaskCategory(_tmp_6);
            if (_tmp_7 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TaskCategory', but it was NULL.");
            } else {
              _tmpCategory = _tmp_7;
            }
            final int _tmpFlexibilityMinutes;
            _tmpFlexibilityMinutes = _cursor.getInt(_cursorIndexOfFlexibilityMinutes);
            final int _tmpEstimatedDurationMinutes;
            _tmpEstimatedDurationMinutes = _cursor.getInt(_cursorIndexOfEstimatedDurationMinutes);
            final boolean _tmpIsCompleted;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_8 != 0;
            final boolean _tmpIsSkipped;
            final int _tmp_9;
            _tmp_9 = _cursor.getInt(_cursorIndexOfIsSkipped);
            _tmpIsSkipped = _tmp_9 != 0;
            final Recurrence _tmpRecurrence;
            final String _tmp_10;
            if (_cursor.isNull(_cursorIndexOfRecurrence)) {
              _tmp_10 = null;
            } else {
              _tmp_10 = _cursor.getString(_cursorIndexOfRecurrence);
            }
            final Recurrence _tmp_11 = __converters.fromRecurrence(_tmp_10);
            if (_tmp_11 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Recurrence', but it was NULL.");
            } else {
              _tmpRecurrence = _tmp_11;
            }
            final boolean _tmpAutoReschedule;
            final int _tmp_12;
            _tmp_12 = _cursor.getInt(_cursorIndexOfAutoReschedule);
            _tmpAutoReschedule = _tmp_12 != 0;
            final LocalTime _tmpThresholdTime;
            final String _tmp_13;
            if (_cursor.isNull(_cursorIndexOfThresholdTime)) {
              _tmp_13 = null;
            } else {
              _tmp_13 = _cursor.getString(_cursorIndexOfThresholdTime);
            }
            _tmpThresholdTime = __converters.fromLocalTime(_tmp_13);
            final boolean _tmpIsGoal;
            final int _tmp_14;
            _tmp_14 = _cursor.getInt(_cursorIndexOfIsGoal);
            _tmpIsGoal = _tmp_14 != 0;
            final boolean _tmpHasSpawnedRecurrence;
            final int _tmp_15;
            _tmp_15 = _cursor.getInt(_cursorIndexOfHasSpawnedRecurrence);
            _tmpHasSpawnedRecurrence = _tmp_15 != 0;
            final Integer _tmpSpawnedTaskId;
            if (_cursor.isNull(_cursorIndexOfSpawnedTaskId)) {
              _tmpSpawnedTaskId = null;
            } else {
              _tmpSpawnedTaskId = _cursor.getInt(_cursorIndexOfSpawnedTaskId);
            }
            _item = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpCategory,_tmpFlexibilityMinutes,_tmpEstimatedDurationMinutes,_tmpIsCompleted,_tmpIsSkipped,_tmpRecurrence,_tmpAutoReschedule,_tmpThresholdTime,_tmpIsGoal,_tmpHasSpawnedRecurrence,_tmpSpawnedTaskId);
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
  public Object getTaskById(final int id, final Continuation<? super TaskEntity> $completion) {
    final String _sql = "SELECT * FROM tasks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TaskEntity>() {
      @Override
      @Nullable
      public TaskEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfFlexibilityMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "flexibilityMinutes");
          final int _cursorIndexOfEstimatedDurationMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "estimatedDurationMinutes");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfIsSkipped = CursorUtil.getColumnIndexOrThrow(_cursor, "isSkipped");
          final int _cursorIndexOfRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrence");
          final int _cursorIndexOfAutoReschedule = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReschedule");
          final int _cursorIndexOfThresholdTime = CursorUtil.getColumnIndexOrThrow(_cursor, "thresholdTime");
          final int _cursorIndexOfIsGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "isGoal");
          final int _cursorIndexOfHasSpawnedRecurrence = CursorUtil.getColumnIndexOrThrow(_cursor, "hasSpawnedRecurrence");
          final int _cursorIndexOfSpawnedTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "spawnedTaskId");
          final TaskEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final LocalDateTime _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converters.fromTimestamp(_tmp);
            final LocalDateTime _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converters.fromTimestamp(_tmp_1);
            final Priority _tmpPriority;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPriority)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPriority);
            }
            final Priority _tmp_3 = __converters.fromPriority(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Priority', but it was NULL.");
            } else {
              _tmpPriority = _tmp_3;
            }
            final TaskCategory _tmpCategory;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfCategory);
            }
            final TaskCategory _tmp_5 = __converters.fromTaskCategory(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TaskCategory', but it was NULL.");
            } else {
              _tmpCategory = _tmp_5;
            }
            final int _tmpFlexibilityMinutes;
            _tmpFlexibilityMinutes = _cursor.getInt(_cursorIndexOfFlexibilityMinutes);
            final int _tmpEstimatedDurationMinutes;
            _tmpEstimatedDurationMinutes = _cursor.getInt(_cursorIndexOfEstimatedDurationMinutes);
            final boolean _tmpIsCompleted;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_6 != 0;
            final boolean _tmpIsSkipped;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsSkipped);
            _tmpIsSkipped = _tmp_7 != 0;
            final Recurrence _tmpRecurrence;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfRecurrence)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfRecurrence);
            }
            final Recurrence _tmp_9 = __converters.fromRecurrence(_tmp_8);
            if (_tmp_9 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.Recurrence', but it was NULL.");
            } else {
              _tmpRecurrence = _tmp_9;
            }
            final boolean _tmpAutoReschedule;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfAutoReschedule);
            _tmpAutoReschedule = _tmp_10 != 0;
            final LocalTime _tmpThresholdTime;
            final String _tmp_11;
            if (_cursor.isNull(_cursorIndexOfThresholdTime)) {
              _tmp_11 = null;
            } else {
              _tmp_11 = _cursor.getString(_cursorIndexOfThresholdTime);
            }
            _tmpThresholdTime = __converters.fromLocalTime(_tmp_11);
            final boolean _tmpIsGoal;
            final int _tmp_12;
            _tmp_12 = _cursor.getInt(_cursorIndexOfIsGoal);
            _tmpIsGoal = _tmp_12 != 0;
            final boolean _tmpHasSpawnedRecurrence;
            final int _tmp_13;
            _tmp_13 = _cursor.getInt(_cursorIndexOfHasSpawnedRecurrence);
            _tmpHasSpawnedRecurrence = _tmp_13 != 0;
            final Integer _tmpSpawnedTaskId;
            if (_cursor.isNull(_cursorIndexOfSpawnedTaskId)) {
              _tmpSpawnedTaskId = null;
            } else {
              _tmpSpawnedTaskId = _cursor.getInt(_cursorIndexOfSpawnedTaskId);
            }
            _result = new TaskEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpStartTime,_tmpEndTime,_tmpPriority,_tmpCategory,_tmpFlexibilityMinutes,_tmpEstimatedDurationMinutes,_tmpIsCompleted,_tmpIsSkipped,_tmpRecurrence,_tmpAutoReschedule,_tmpThresholdTime,_tmpIsGoal,_tmpHasSpawnedRecurrence,_tmpSpawnedTaskId);
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
}
