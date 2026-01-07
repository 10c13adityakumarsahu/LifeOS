package com.example.lifeos.data.local.dao;

import android.database.Cursor;
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
import com.example.lifeos.data.local.LocalDateConverter;
import com.example.lifeos.data.local.entity.GoalEntity;
import com.example.lifeos.data.local.entity.PersonEntity;
import com.example.lifeos.data.local.entity.RecoveryStatus;
import com.example.lifeos.data.local.entity.SmsPatternEntity;
import com.example.lifeos.data.local.entity.TransactionEntity;
import com.example.lifeos.data.local.entity.TransactionType;
import com.example.lifeos.data.local.entity.UntrackedTransactionEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
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
public final class FinanceDao_Impl implements FinanceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransactionEntity> __insertionAdapterOfTransactionEntity;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<GoalEntity> __insertionAdapterOfGoalEntity;

  private final LocalDateConverter __localDateConverter = new LocalDateConverter();

  private final EntityInsertionAdapter<SmsPatternEntity> __insertionAdapterOfSmsPatternEntity;

  private final EntityInsertionAdapter<UntrackedTransactionEntity> __insertionAdapterOfUntrackedTransactionEntity;

  private final EntityInsertionAdapter<PersonEntity> __insertionAdapterOfPersonEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __deletionAdapterOfTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<GoalEntity> __deletionAdapterOfGoalEntity;

  private final EntityDeletionOrUpdateAdapter<SmsPatternEntity> __deletionAdapterOfSmsPatternEntity;

  private final EntityDeletionOrUpdateAdapter<UntrackedTransactionEntity> __deletionAdapterOfUntrackedTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<PersonEntity> __deletionAdapterOfPersonEntity;

  private final EntityDeletionOrUpdateAdapter<TransactionEntity> __updateAdapterOfTransactionEntity;

  private final EntityDeletionOrUpdateAdapter<GoalEntity> __updateAdapterOfGoalEntity;

  private final EntityDeletionOrUpdateAdapter<SmsPatternEntity> __updateAdapterOfSmsPatternEntity;

  public FinanceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransactionEntity = new EntityInsertionAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `finance_transactions` (`id`,`amount`,`category`,`description`,`date`,`type`,`isRecoverable`,`recoveryStatus`,`payerOrPayeeName`,`isRecursive`,`recurrencePattern`,`isBill`,`dueDate`,`isPaid`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getAmount());
        statement.bindString(3, entity.getCategory());
        statement.bindString(4, entity.getDescription());
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final String _tmp_1 = __converters.transactionTypeToString(entity.getType());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
        final int _tmp_2 = entity.isRecoverable() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        final String _tmp_3 = __converters.recoveryStatusToString(entity.getRecoveryStatus());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_3);
        }
        if (entity.getPayerOrPayeeName() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPayerOrPayeeName());
        }
        final int _tmp_4 = entity.isRecursive() ? 1 : 0;
        statement.bindLong(10, _tmp_4);
        if (entity.getRecurrencePattern() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRecurrencePattern());
        }
        final int _tmp_5 = entity.isBill() ? 1 : 0;
        statement.bindLong(12, _tmp_5);
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getDueDate());
        if (_tmp_6 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_6);
        }
        final int _tmp_7 = entity.isPaid() ? 1 : 0;
        statement.bindLong(14, _tmp_7);
      }
    };
    this.__insertionAdapterOfGoalEntity = new EntityInsertionAdapter<GoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `financial_goals` (`id`,`name`,`targetAmount`,`currentAmount`,`deadline`,`isCompleted`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getTargetAmount());
        statement.bindDouble(4, entity.getCurrentAmount());
        final Long _tmp = __localDateConverter.dateToTimestamp(entity.getDeadline());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final int _tmp_1 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
      }
    };
    this.__insertionAdapterOfSmsPatternEntity = new EntityInsertionAdapter<SmsPatternEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sms_patterns` (`id`,`pattern`,`senderId`,`type`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmsPatternEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPattern());
        if (entity.getSenderId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSenderId());
        }
        final String _tmp = __converters.transactionTypeToString(entity.getType());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
      }
    };
    this.__insertionAdapterOfUntrackedTransactionEntity = new EntityInsertionAdapter<UntrackedTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `untracked_transactions` (`id`,`amount`,`type`,`sender`,`body`,`date`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UntrackedTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getAmount());
        final String _tmp = __converters.transactionTypeToString(entity.getType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        statement.bindString(4, entity.getSender());
        statement.bindString(5, entity.getBody());
        final Long _tmp_1 = __converters.dateToTimestamp(entity.getDate());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp_1);
        }
      }
    };
    this.__insertionAdapterOfPersonEntity = new EntityInsertionAdapter<PersonEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `people` (`id`,`name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__deletionAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `finance_transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfGoalEntity = new EntityDeletionOrUpdateAdapter<GoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `financial_goals` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfSmsPatternEntity = new EntityDeletionOrUpdateAdapter<SmsPatternEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `sms_patterns` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmsPatternEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfUntrackedTransactionEntity = new EntityDeletionOrUpdateAdapter<UntrackedTransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `untracked_transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UntrackedTransactionEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__deletionAdapterOfPersonEntity = new EntityDeletionOrUpdateAdapter<PersonEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `people` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PersonEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTransactionEntity = new EntityDeletionOrUpdateAdapter<TransactionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `finance_transactions` SET `id` = ?,`amount` = ?,`category` = ?,`description` = ?,`date` = ?,`type` = ?,`isRecoverable` = ?,`recoveryStatus` = ?,`payerOrPayeeName` = ?,`isRecursive` = ?,`recurrencePattern` = ?,`isBill` = ?,`dueDate` = ?,`isPaid` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransactionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getAmount());
        statement.bindString(3, entity.getCategory());
        statement.bindString(4, entity.getDescription());
        final Long _tmp = __converters.dateToTimestamp(entity.getDate());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final String _tmp_1 = __converters.transactionTypeToString(entity.getType());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
        final int _tmp_2 = entity.isRecoverable() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        final String _tmp_3 = __converters.recoveryStatusToString(entity.getRecoveryStatus());
        if (_tmp_3 == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, _tmp_3);
        }
        if (entity.getPayerOrPayeeName() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getPayerOrPayeeName());
        }
        final int _tmp_4 = entity.isRecursive() ? 1 : 0;
        statement.bindLong(10, _tmp_4);
        if (entity.getRecurrencePattern() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getRecurrencePattern());
        }
        final int _tmp_5 = entity.isBill() ? 1 : 0;
        statement.bindLong(12, _tmp_5);
        final Long _tmp_6 = __converters.dateToTimestamp(entity.getDueDate());
        if (_tmp_6 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_6);
        }
        final int _tmp_7 = entity.isPaid() ? 1 : 0;
        statement.bindLong(14, _tmp_7);
        statement.bindLong(15, entity.getId());
      }
    };
    this.__updateAdapterOfGoalEntity = new EntityDeletionOrUpdateAdapter<GoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `financial_goals` SET `id` = ?,`name` = ?,`targetAmount` = ?,`currentAmount` = ?,`deadline` = ?,`isCompleted` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getTargetAmount());
        statement.bindDouble(4, entity.getCurrentAmount());
        final Long _tmp = __localDateConverter.dateToTimestamp(entity.getDeadline());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        final int _tmp_1 = entity.isCompleted() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        statement.bindLong(7, entity.getId());
      }
    };
    this.__updateAdapterOfSmsPatternEntity = new EntityDeletionOrUpdateAdapter<SmsPatternEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `sms_patterns` SET `id` = ?,`pattern` = ?,`senderId` = ?,`type` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SmsPatternEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getPattern());
        if (entity.getSenderId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getSenderId());
        }
        final String _tmp = __converters.transactionTypeToString(entity.getType());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insertTransaction(final TransactionEntity transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTransactionEntity.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertGoal(final GoalEntity goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGoalEntity.insert(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSmsPattern(final SmsPatternEntity pattern,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSmsPatternEntity.insert(pattern);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertUntrackedTransaction(final UntrackedTransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUntrackedTransactionEntity.insert(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPerson(final PersonEntity person,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPersonEntity.insert(person);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransaction(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteGoal(final GoalEntity goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGoalEntity.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSmsPattern(final SmsPatternEntity pattern,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSmsPatternEntity.handle(pattern);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteUntrackedTransaction(final UntrackedTransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUntrackedTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePerson(final PersonEntity person,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPersonEntity.handle(person);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTransaction(final TransactionEntity transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransactionEntity.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateGoal(final GoalEntity goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGoalEntity.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSmsPattern(final SmsPatternEntity pattern,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSmsPatternEntity.handle(pattern);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TransactionEntity>> getAllTransactions() {
    final String _sql = "SELECT * FROM finance_transactions ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"finance_transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIsRecoverable = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecoverable");
          final int _cursorIndexOfRecoveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "recoveryStatus");
          final int _cursorIndexOfPayerOrPayeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "payerOrPayeeName");
          final int _cursorIndexOfIsRecursive = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecursive");
          final int _cursorIndexOfRecurrencePattern = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrencePattern");
          final int _cursorIndexOfIsBill = CursorUtil.getColumnIndexOrThrow(_cursor, "isBill");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
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
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.fromTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final boolean _tmpIsRecoverable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsRecoverable);
            _tmpIsRecoverable = _tmp_4 != 0;
            final RecoveryStatus _tmpRecoveryStatus;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfRecoveryStatus)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfRecoveryStatus);
            }
            final RecoveryStatus _tmp_6 = __converters.fromRecoveryStatus(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.RecoveryStatus', but it was NULL.");
            } else {
              _tmpRecoveryStatus = _tmp_6;
            }
            final String _tmpPayerOrPayeeName;
            if (_cursor.isNull(_cursorIndexOfPayerOrPayeeName)) {
              _tmpPayerOrPayeeName = null;
            } else {
              _tmpPayerOrPayeeName = _cursor.getString(_cursorIndexOfPayerOrPayeeName);
            }
            final boolean _tmpIsRecursive;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsRecursive);
            _tmpIsRecursive = _tmp_7 != 0;
            final String _tmpRecurrencePattern;
            if (_cursor.isNull(_cursorIndexOfRecurrencePattern)) {
              _tmpRecurrencePattern = null;
            } else {
              _tmpRecurrencePattern = _cursor.getString(_cursorIndexOfRecurrencePattern);
            }
            final boolean _tmpIsBill;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsBill);
            _tmpIsBill = _tmp_8 != 0;
            final LocalDateTime _tmpDueDate;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfDueDate);
            }
            _tmpDueDate = __converters.fromTimestamp(_tmp_9);
            final boolean _tmpIsPaid;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp_10 != 0;
            _item = new TransactionEntity(_tmpId,_tmpAmount,_tmpCategory,_tmpDescription,_tmpDate,_tmpType,_tmpIsRecoverable,_tmpRecoveryStatus,_tmpPayerOrPayeeName,_tmpIsRecursive,_tmpRecurrencePattern,_tmpIsBill,_tmpDueDate,_tmpIsPaid);
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
  public Flow<Double> getTotalExpensesSince(final LocalDateTime startDate) {
    final String _sql = "SELECT SUM(amount) FROM finance_transactions WHERE type = 'EXPENSE' AND date >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp = __converters.dateToTimestamp(startDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"finance_transactions"}, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp_1;
            if (_cursor.isNull(0)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getDouble(0);
            }
            _result = _tmp_1;
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
  public Flow<List<TransactionEntity>> getPendingRecoverables() {
    final String _sql = "SELECT * FROM finance_transactions WHERE type IN ('LEND', 'BORROW') AND recoveryStatus = 'PENDING'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"finance_transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIsRecoverable = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecoverable");
          final int _cursorIndexOfRecoveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "recoveryStatus");
          final int _cursorIndexOfPayerOrPayeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "payerOrPayeeName");
          final int _cursorIndexOfIsRecursive = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecursive");
          final int _cursorIndexOfRecurrencePattern = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrencePattern");
          final int _cursorIndexOfIsBill = CursorUtil.getColumnIndexOrThrow(_cursor, "isBill");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
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
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.fromTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final boolean _tmpIsRecoverable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsRecoverable);
            _tmpIsRecoverable = _tmp_4 != 0;
            final RecoveryStatus _tmpRecoveryStatus;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfRecoveryStatus)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfRecoveryStatus);
            }
            final RecoveryStatus _tmp_6 = __converters.fromRecoveryStatus(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.RecoveryStatus', but it was NULL.");
            } else {
              _tmpRecoveryStatus = _tmp_6;
            }
            final String _tmpPayerOrPayeeName;
            if (_cursor.isNull(_cursorIndexOfPayerOrPayeeName)) {
              _tmpPayerOrPayeeName = null;
            } else {
              _tmpPayerOrPayeeName = _cursor.getString(_cursorIndexOfPayerOrPayeeName);
            }
            final boolean _tmpIsRecursive;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsRecursive);
            _tmpIsRecursive = _tmp_7 != 0;
            final String _tmpRecurrencePattern;
            if (_cursor.isNull(_cursorIndexOfRecurrencePattern)) {
              _tmpRecurrencePattern = null;
            } else {
              _tmpRecurrencePattern = _cursor.getString(_cursorIndexOfRecurrencePattern);
            }
            final boolean _tmpIsBill;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsBill);
            _tmpIsBill = _tmp_8 != 0;
            final LocalDateTime _tmpDueDate;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfDueDate);
            }
            _tmpDueDate = __converters.fromTimestamp(_tmp_9);
            final boolean _tmpIsPaid;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp_10 != 0;
            _item = new TransactionEntity(_tmpId,_tmpAmount,_tmpCategory,_tmpDescription,_tmpDate,_tmpType,_tmpIsRecoverable,_tmpRecoveryStatus,_tmpPayerOrPayeeName,_tmpIsRecursive,_tmpRecurrencePattern,_tmpIsBill,_tmpDueDate,_tmpIsPaid);
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
  public Flow<List<TransactionEntity>> getRecursiveTransactions() {
    final String _sql = "SELECT * FROM finance_transactions WHERE isRecursive = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"finance_transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIsRecoverable = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecoverable");
          final int _cursorIndexOfRecoveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "recoveryStatus");
          final int _cursorIndexOfPayerOrPayeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "payerOrPayeeName");
          final int _cursorIndexOfIsRecursive = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecursive");
          final int _cursorIndexOfRecurrencePattern = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrencePattern");
          final int _cursorIndexOfIsBill = CursorUtil.getColumnIndexOrThrow(_cursor, "isBill");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
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
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.fromTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final boolean _tmpIsRecoverable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsRecoverable);
            _tmpIsRecoverable = _tmp_4 != 0;
            final RecoveryStatus _tmpRecoveryStatus;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfRecoveryStatus)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfRecoveryStatus);
            }
            final RecoveryStatus _tmp_6 = __converters.fromRecoveryStatus(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.RecoveryStatus', but it was NULL.");
            } else {
              _tmpRecoveryStatus = _tmp_6;
            }
            final String _tmpPayerOrPayeeName;
            if (_cursor.isNull(_cursorIndexOfPayerOrPayeeName)) {
              _tmpPayerOrPayeeName = null;
            } else {
              _tmpPayerOrPayeeName = _cursor.getString(_cursorIndexOfPayerOrPayeeName);
            }
            final boolean _tmpIsRecursive;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsRecursive);
            _tmpIsRecursive = _tmp_7 != 0;
            final String _tmpRecurrencePattern;
            if (_cursor.isNull(_cursorIndexOfRecurrencePattern)) {
              _tmpRecurrencePattern = null;
            } else {
              _tmpRecurrencePattern = _cursor.getString(_cursorIndexOfRecurrencePattern);
            }
            final boolean _tmpIsBill;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsBill);
            _tmpIsBill = _tmp_8 != 0;
            final LocalDateTime _tmpDueDate;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfDueDate);
            }
            _tmpDueDate = __converters.fromTimestamp(_tmp_9);
            final boolean _tmpIsPaid;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp_10 != 0;
            _item = new TransactionEntity(_tmpId,_tmpAmount,_tmpCategory,_tmpDescription,_tmpDate,_tmpType,_tmpIsRecoverable,_tmpRecoveryStatus,_tmpPayerOrPayeeName,_tmpIsRecursive,_tmpRecurrencePattern,_tmpIsBill,_tmpDueDate,_tmpIsPaid);
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
  public Flow<List<TransactionEntity>> getDuePayments() {
    final String _sql = "SELECT * FROM finance_transactions WHERE isBill = 1 ORDER BY isPaid ASC, dueDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"finance_transactions"}, new Callable<List<TransactionEntity>>() {
      @Override
      @NonNull
      public List<TransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfIsRecoverable = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecoverable");
          final int _cursorIndexOfRecoveryStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "recoveryStatus");
          final int _cursorIndexOfPayerOrPayeeName = CursorUtil.getColumnIndexOrThrow(_cursor, "payerOrPayeeName");
          final int _cursorIndexOfIsRecursive = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecursive");
          final int _cursorIndexOfRecurrencePattern = CursorUtil.getColumnIndexOrThrow(_cursor, "recurrencePattern");
          final int _cursorIndexOfIsBill = CursorUtil.getColumnIndexOrThrow(_cursor, "isBill");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "isPaid");
          final List<TransactionEntity> _result = new ArrayList<TransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TransactionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
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
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.fromTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final boolean _tmpIsRecoverable;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsRecoverable);
            _tmpIsRecoverable = _tmp_4 != 0;
            final RecoveryStatus _tmpRecoveryStatus;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfRecoveryStatus)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfRecoveryStatus);
            }
            final RecoveryStatus _tmp_6 = __converters.fromRecoveryStatus(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.RecoveryStatus', but it was NULL.");
            } else {
              _tmpRecoveryStatus = _tmp_6;
            }
            final String _tmpPayerOrPayeeName;
            if (_cursor.isNull(_cursorIndexOfPayerOrPayeeName)) {
              _tmpPayerOrPayeeName = null;
            } else {
              _tmpPayerOrPayeeName = _cursor.getString(_cursorIndexOfPayerOrPayeeName);
            }
            final boolean _tmpIsRecursive;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsRecursive);
            _tmpIsRecursive = _tmp_7 != 0;
            final String _tmpRecurrencePattern;
            if (_cursor.isNull(_cursorIndexOfRecurrencePattern)) {
              _tmpRecurrencePattern = null;
            } else {
              _tmpRecurrencePattern = _cursor.getString(_cursorIndexOfRecurrencePattern);
            }
            final boolean _tmpIsBill;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfIsBill);
            _tmpIsBill = _tmp_8 != 0;
            final LocalDateTime _tmpDueDate;
            final Long _tmp_9;
            if (_cursor.isNull(_cursorIndexOfDueDate)) {
              _tmp_9 = null;
            } else {
              _tmp_9 = _cursor.getLong(_cursorIndexOfDueDate);
            }
            _tmpDueDate = __converters.fromTimestamp(_tmp_9);
            final boolean _tmpIsPaid;
            final int _tmp_10;
            _tmp_10 = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp_10 != 0;
            _item = new TransactionEntity(_tmpId,_tmpAmount,_tmpCategory,_tmpDescription,_tmpDate,_tmpType,_tmpIsRecoverable,_tmpRecoveryStatus,_tmpPayerOrPayeeName,_tmpIsRecursive,_tmpRecurrencePattern,_tmpIsBill,_tmpDueDate,_tmpIsPaid);
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
  public Flow<List<GoalEntity>> getAllGoals() {
    final String _sql = "SELECT * FROM financial_goals ORDER BY isCompleted ASC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"financial_goals"}, new Callable<List<GoalEntity>>() {
      @Override
      @NonNull
      public List<GoalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfTargetAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "targetAmount");
          final int _cursorIndexOfCurrentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "currentAmount");
          final int _cursorIndexOfDeadline = CursorUtil.getColumnIndexOrThrow(_cursor, "deadline");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final List<GoalEntity> _result = new ArrayList<GoalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GoalEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpTargetAmount;
            _tmpTargetAmount = _cursor.getDouble(_cursorIndexOfTargetAmount);
            final double _tmpCurrentAmount;
            _tmpCurrentAmount = _cursor.getDouble(_cursorIndexOfCurrentAmount);
            final LocalDate _tmpDeadline;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDeadline)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDeadline);
            }
            _tmpDeadline = __localDateConverter.fromTimestamp(_tmp);
            final boolean _tmpIsCompleted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp_1 != 0;
            _item = new GoalEntity(_tmpId,_tmpName,_tmpTargetAmount,_tmpCurrentAmount,_tmpDeadline,_tmpIsCompleted);
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
  public Flow<List<SmsPatternEntity>> getAllSmsPatterns() {
    final String _sql = "SELECT * FROM sms_patterns";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sms_patterns"}, new Callable<List<SmsPatternEntity>>() {
      @Override
      @NonNull
      public List<SmsPatternEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPattern = CursorUtil.getColumnIndexOrThrow(_cursor, "pattern");
          final int _cursorIndexOfSenderId = CursorUtil.getColumnIndexOrThrow(_cursor, "senderId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final List<SmsPatternEntity> _result = new ArrayList<SmsPatternEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SmsPatternEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpPattern;
            _tmpPattern = _cursor.getString(_cursorIndexOfPattern);
            final String _tmpSenderId;
            if (_cursor.isNull(_cursorIndexOfSenderId)) {
              _tmpSenderId = null;
            } else {
              _tmpSenderId = _cursor.getString(_cursorIndexOfSenderId);
            }
            final TransactionType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_1 = __converters.fromTransactionType(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_1;
            }
            _item = new SmsPatternEntity(_tmpId,_tmpPattern,_tmpSenderId,_tmpType);
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
  public Flow<List<UntrackedTransactionEntity>> getAllUntrackedTransactions() {
    final String _sql = "SELECT * FROM untracked_transactions ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"untracked_transactions"}, new Callable<List<UntrackedTransactionEntity>>() {
      @Override
      @NonNull
      public List<UntrackedTransactionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSender = CursorUtil.getColumnIndexOrThrow(_cursor, "sender");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final List<UntrackedTransactionEntity> _result = new ArrayList<UntrackedTransactionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UntrackedTransactionEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final TransactionType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_1 = __converters.fromTransactionType(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.example.lifeos.data.local.entity.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_1;
            }
            final String _tmpSender;
            _tmpSender = _cursor.getString(_cursorIndexOfSender);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            final LocalDateTime _tmpDate;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_3 = __converters.fromTimestamp(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_3;
            }
            _item = new UntrackedTransactionEntity(_tmpId,_tmpAmount,_tmpType,_tmpSender,_tmpBody,_tmpDate);
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
  public Flow<List<PersonEntity>> getAllPeople() {
    final String _sql = "SELECT * FROM people ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"people"}, new Callable<List<PersonEntity>>() {
      @Override
      @NonNull
      public List<PersonEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final List<PersonEntity> _result = new ArrayList<PersonEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PersonEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item = new PersonEntity(_tmpId,_tmpName);
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
