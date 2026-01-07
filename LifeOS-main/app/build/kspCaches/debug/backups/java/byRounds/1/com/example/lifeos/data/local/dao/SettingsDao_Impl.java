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
import com.example.lifeos.data.local.entity.SettingsEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SettingsDao_Impl implements SettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SettingsEntity> __insertionAdapterOfSettingsEntity;

  private final EntityDeletionOrUpdateAdapter<SettingsEntity> __updateAdapterOfSettingsEntity;

  public SettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSettingsEntity = new EntityInsertionAdapter<SettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `settings` (`id`,`isWaterReminderEnabled`,`waterReminderIntervalMinutes`,`isSedentaryReminderEnabled`,`sedentaryReminderIntervalMinutes`,`userName`,`areNotificationsEnabled`,`isSleepModeEnabled`,`isAutoReplyEnabled`,`sleepWhitelist`,`pinCode`,`currentFocusMode`,`isSilentModeEnabledForFocus`,`customReplySleep`,`customReplyDriving`,`customReplyMeeting`,`preFocusRingerMode`,`allowOverride`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SettingsEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isWaterReminderEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getWaterReminderIntervalMinutes());
        final int _tmp_1 = entity.isSedentaryReminderEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        statement.bindLong(5, entity.getSedentaryReminderIntervalMinutes());
        statement.bindString(6, entity.getUserName());
        final int _tmp_2 = entity.getAreNotificationsEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        final int _tmp_3 = entity.isSleepModeEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        final int _tmp_4 = entity.isAutoReplyEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_4);
        statement.bindString(10, entity.getSleepWhitelist());
        if (entity.getPinCode() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getPinCode());
        }
        statement.bindString(12, entity.getCurrentFocusMode());
        final int _tmp_5 = entity.isSilentModeEnabledForFocus() ? 1 : 0;
        statement.bindLong(13, _tmp_5);
        if (entity.getCustomReplySleep() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCustomReplySleep());
        }
        if (entity.getCustomReplyDriving() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getCustomReplyDriving());
        }
        if (entity.getCustomReplyMeeting() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getCustomReplyMeeting());
        }
        statement.bindLong(17, entity.getPreFocusRingerMode());
        final int _tmp_6 = entity.getAllowOverride() ? 1 : 0;
        statement.bindLong(18, _tmp_6);
      }
    };
    this.__updateAdapterOfSettingsEntity = new EntityDeletionOrUpdateAdapter<SettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `settings` SET `id` = ?,`isWaterReminderEnabled` = ?,`waterReminderIntervalMinutes` = ?,`isSedentaryReminderEnabled` = ?,`sedentaryReminderIntervalMinutes` = ?,`userName` = ?,`areNotificationsEnabled` = ?,`isSleepModeEnabled` = ?,`isAutoReplyEnabled` = ?,`sleepWhitelist` = ?,`pinCode` = ?,`currentFocusMode` = ?,`isSilentModeEnabledForFocus` = ?,`customReplySleep` = ?,`customReplyDriving` = ?,`customReplyMeeting` = ?,`preFocusRingerMode` = ?,`allowOverride` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SettingsEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isWaterReminderEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        statement.bindLong(3, entity.getWaterReminderIntervalMinutes());
        final int _tmp_1 = entity.isSedentaryReminderEnabled() ? 1 : 0;
        statement.bindLong(4, _tmp_1);
        statement.bindLong(5, entity.getSedentaryReminderIntervalMinutes());
        statement.bindString(6, entity.getUserName());
        final int _tmp_2 = entity.getAreNotificationsEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        final int _tmp_3 = entity.isSleepModeEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        final int _tmp_4 = entity.isAutoReplyEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_4);
        statement.bindString(10, entity.getSleepWhitelist());
        if (entity.getPinCode() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getPinCode());
        }
        statement.bindString(12, entity.getCurrentFocusMode());
        final int _tmp_5 = entity.isSilentModeEnabledForFocus() ? 1 : 0;
        statement.bindLong(13, _tmp_5);
        if (entity.getCustomReplySleep() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCustomReplySleep());
        }
        if (entity.getCustomReplyDriving() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getCustomReplyDriving());
        }
        if (entity.getCustomReplyMeeting() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getCustomReplyMeeting());
        }
        statement.bindLong(17, entity.getPreFocusRingerMode());
        final int _tmp_6 = entity.getAllowOverride() ? 1 : 0;
        statement.bindLong(18, _tmp_6);
        statement.bindLong(19, entity.getId());
      }
    };
  }

  @Override
  public Object insertSettings(final SettingsEntity settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSettingsEntity.insert(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSettings(final SettingsEntity settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSettingsEntity.handle(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<SettingsEntity> getSettings() {
    final String _sql = "SELECT * FROM settings WHERE id = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"settings"}, new Callable<SettingsEntity>() {
      @Override
      @Nullable
      public SettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIsWaterReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isWaterReminderEnabled");
          final int _cursorIndexOfWaterReminderIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "waterReminderIntervalMinutes");
          final int _cursorIndexOfIsSedentaryReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSedentaryReminderEnabled");
          final int _cursorIndexOfSedentaryReminderIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "sedentaryReminderIntervalMinutes");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfAreNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "areNotificationsEnabled");
          final int _cursorIndexOfIsSleepModeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSleepModeEnabled");
          final int _cursorIndexOfIsAutoReplyEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isAutoReplyEnabled");
          final int _cursorIndexOfSleepWhitelist = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepWhitelist");
          final int _cursorIndexOfPinCode = CursorUtil.getColumnIndexOrThrow(_cursor, "pinCode");
          final int _cursorIndexOfCurrentFocusMode = CursorUtil.getColumnIndexOrThrow(_cursor, "currentFocusMode");
          final int _cursorIndexOfIsSilentModeEnabledForFocus = CursorUtil.getColumnIndexOrThrow(_cursor, "isSilentModeEnabledForFocus");
          final int _cursorIndexOfCustomReplySleep = CursorUtil.getColumnIndexOrThrow(_cursor, "customReplySleep");
          final int _cursorIndexOfCustomReplyDriving = CursorUtil.getColumnIndexOrThrow(_cursor, "customReplyDriving");
          final int _cursorIndexOfCustomReplyMeeting = CursorUtil.getColumnIndexOrThrow(_cursor, "customReplyMeeting");
          final int _cursorIndexOfPreFocusRingerMode = CursorUtil.getColumnIndexOrThrow(_cursor, "preFocusRingerMode");
          final int _cursorIndexOfAllowOverride = CursorUtil.getColumnIndexOrThrow(_cursor, "allowOverride");
          final SettingsEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpIsWaterReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWaterReminderEnabled);
            _tmpIsWaterReminderEnabled = _tmp != 0;
            final int _tmpWaterReminderIntervalMinutes;
            _tmpWaterReminderIntervalMinutes = _cursor.getInt(_cursorIndexOfWaterReminderIntervalMinutes);
            final boolean _tmpIsSedentaryReminderEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSedentaryReminderEnabled);
            _tmpIsSedentaryReminderEnabled = _tmp_1 != 0;
            final int _tmpSedentaryReminderIntervalMinutes;
            _tmpSedentaryReminderIntervalMinutes = _cursor.getInt(_cursorIndexOfSedentaryReminderIntervalMinutes);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final boolean _tmpAreNotificationsEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfAreNotificationsEnabled);
            _tmpAreNotificationsEnabled = _tmp_2 != 0;
            final boolean _tmpIsSleepModeEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsSleepModeEnabled);
            _tmpIsSleepModeEnabled = _tmp_3 != 0;
            final boolean _tmpIsAutoReplyEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsAutoReplyEnabled);
            _tmpIsAutoReplyEnabled = _tmp_4 != 0;
            final String _tmpSleepWhitelist;
            _tmpSleepWhitelist = _cursor.getString(_cursorIndexOfSleepWhitelist);
            final String _tmpPinCode;
            if (_cursor.isNull(_cursorIndexOfPinCode)) {
              _tmpPinCode = null;
            } else {
              _tmpPinCode = _cursor.getString(_cursorIndexOfPinCode);
            }
            final String _tmpCurrentFocusMode;
            _tmpCurrentFocusMode = _cursor.getString(_cursorIndexOfCurrentFocusMode);
            final boolean _tmpIsSilentModeEnabledForFocus;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSilentModeEnabledForFocus);
            _tmpIsSilentModeEnabledForFocus = _tmp_5 != 0;
            final String _tmpCustomReplySleep;
            if (_cursor.isNull(_cursorIndexOfCustomReplySleep)) {
              _tmpCustomReplySleep = null;
            } else {
              _tmpCustomReplySleep = _cursor.getString(_cursorIndexOfCustomReplySleep);
            }
            final String _tmpCustomReplyDriving;
            if (_cursor.isNull(_cursorIndexOfCustomReplyDriving)) {
              _tmpCustomReplyDriving = null;
            } else {
              _tmpCustomReplyDriving = _cursor.getString(_cursorIndexOfCustomReplyDriving);
            }
            final String _tmpCustomReplyMeeting;
            if (_cursor.isNull(_cursorIndexOfCustomReplyMeeting)) {
              _tmpCustomReplyMeeting = null;
            } else {
              _tmpCustomReplyMeeting = _cursor.getString(_cursorIndexOfCustomReplyMeeting);
            }
            final int _tmpPreFocusRingerMode;
            _tmpPreFocusRingerMode = _cursor.getInt(_cursorIndexOfPreFocusRingerMode);
            final boolean _tmpAllowOverride;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfAllowOverride);
            _tmpAllowOverride = _tmp_6 != 0;
            _result = new SettingsEntity(_tmpId,_tmpIsWaterReminderEnabled,_tmpWaterReminderIntervalMinutes,_tmpIsSedentaryReminderEnabled,_tmpSedentaryReminderIntervalMinutes,_tmpUserName,_tmpAreNotificationsEnabled,_tmpIsSleepModeEnabled,_tmpIsAutoReplyEnabled,_tmpSleepWhitelist,_tmpPinCode,_tmpCurrentFocusMode,_tmpIsSilentModeEnabledForFocus,_tmpCustomReplySleep,_tmpCustomReplyDriving,_tmpCustomReplyMeeting,_tmpPreFocusRingerMode,_tmpAllowOverride);
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
  public Object getSettingsSnapshot(final Continuation<? super SettingsEntity> $completion) {
    final String _sql = "SELECT * FROM settings WHERE id = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SettingsEntity>() {
      @Override
      @Nullable
      public SettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIsWaterReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isWaterReminderEnabled");
          final int _cursorIndexOfWaterReminderIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "waterReminderIntervalMinutes");
          final int _cursorIndexOfIsSedentaryReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSedentaryReminderEnabled");
          final int _cursorIndexOfSedentaryReminderIntervalMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "sedentaryReminderIntervalMinutes");
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfAreNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "areNotificationsEnabled");
          final int _cursorIndexOfIsSleepModeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isSleepModeEnabled");
          final int _cursorIndexOfIsAutoReplyEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "isAutoReplyEnabled");
          final int _cursorIndexOfSleepWhitelist = CursorUtil.getColumnIndexOrThrow(_cursor, "sleepWhitelist");
          final int _cursorIndexOfPinCode = CursorUtil.getColumnIndexOrThrow(_cursor, "pinCode");
          final int _cursorIndexOfCurrentFocusMode = CursorUtil.getColumnIndexOrThrow(_cursor, "currentFocusMode");
          final int _cursorIndexOfIsSilentModeEnabledForFocus = CursorUtil.getColumnIndexOrThrow(_cursor, "isSilentModeEnabledForFocus");
          final int _cursorIndexOfCustomReplySleep = CursorUtil.getColumnIndexOrThrow(_cursor, "customReplySleep");
          final int _cursorIndexOfCustomReplyDriving = CursorUtil.getColumnIndexOrThrow(_cursor, "customReplyDriving");
          final int _cursorIndexOfCustomReplyMeeting = CursorUtil.getColumnIndexOrThrow(_cursor, "customReplyMeeting");
          final int _cursorIndexOfPreFocusRingerMode = CursorUtil.getColumnIndexOrThrow(_cursor, "preFocusRingerMode");
          final int _cursorIndexOfAllowOverride = CursorUtil.getColumnIndexOrThrow(_cursor, "allowOverride");
          final SettingsEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpIsWaterReminderEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsWaterReminderEnabled);
            _tmpIsWaterReminderEnabled = _tmp != 0;
            final int _tmpWaterReminderIntervalMinutes;
            _tmpWaterReminderIntervalMinutes = _cursor.getInt(_cursorIndexOfWaterReminderIntervalMinutes);
            final boolean _tmpIsSedentaryReminderEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSedentaryReminderEnabled);
            _tmpIsSedentaryReminderEnabled = _tmp_1 != 0;
            final int _tmpSedentaryReminderIntervalMinutes;
            _tmpSedentaryReminderIntervalMinutes = _cursor.getInt(_cursorIndexOfSedentaryReminderIntervalMinutes);
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final boolean _tmpAreNotificationsEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfAreNotificationsEnabled);
            _tmpAreNotificationsEnabled = _tmp_2 != 0;
            final boolean _tmpIsSleepModeEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsSleepModeEnabled);
            _tmpIsSleepModeEnabled = _tmp_3 != 0;
            final boolean _tmpIsAutoReplyEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfIsAutoReplyEnabled);
            _tmpIsAutoReplyEnabled = _tmp_4 != 0;
            final String _tmpSleepWhitelist;
            _tmpSleepWhitelist = _cursor.getString(_cursorIndexOfSleepWhitelist);
            final String _tmpPinCode;
            if (_cursor.isNull(_cursorIndexOfPinCode)) {
              _tmpPinCode = null;
            } else {
              _tmpPinCode = _cursor.getString(_cursorIndexOfPinCode);
            }
            final String _tmpCurrentFocusMode;
            _tmpCurrentFocusMode = _cursor.getString(_cursorIndexOfCurrentFocusMode);
            final boolean _tmpIsSilentModeEnabledForFocus;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfIsSilentModeEnabledForFocus);
            _tmpIsSilentModeEnabledForFocus = _tmp_5 != 0;
            final String _tmpCustomReplySleep;
            if (_cursor.isNull(_cursorIndexOfCustomReplySleep)) {
              _tmpCustomReplySleep = null;
            } else {
              _tmpCustomReplySleep = _cursor.getString(_cursorIndexOfCustomReplySleep);
            }
            final String _tmpCustomReplyDriving;
            if (_cursor.isNull(_cursorIndexOfCustomReplyDriving)) {
              _tmpCustomReplyDriving = null;
            } else {
              _tmpCustomReplyDriving = _cursor.getString(_cursorIndexOfCustomReplyDriving);
            }
            final String _tmpCustomReplyMeeting;
            if (_cursor.isNull(_cursorIndexOfCustomReplyMeeting)) {
              _tmpCustomReplyMeeting = null;
            } else {
              _tmpCustomReplyMeeting = _cursor.getString(_cursorIndexOfCustomReplyMeeting);
            }
            final int _tmpPreFocusRingerMode;
            _tmpPreFocusRingerMode = _cursor.getInt(_cursorIndexOfPreFocusRingerMode);
            final boolean _tmpAllowOverride;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfAllowOverride);
            _tmpAllowOverride = _tmp_6 != 0;
            _result = new SettingsEntity(_tmpId,_tmpIsWaterReminderEnabled,_tmpWaterReminderIntervalMinutes,_tmpIsSedentaryReminderEnabled,_tmpSedentaryReminderIntervalMinutes,_tmpUserName,_tmpAreNotificationsEnabled,_tmpIsSleepModeEnabled,_tmpIsAutoReplyEnabled,_tmpSleepWhitelist,_tmpPinCode,_tmpCurrentFocusMode,_tmpIsSilentModeEnabledForFocus,_tmpCustomReplySleep,_tmpCustomReplyDriving,_tmpCustomReplyMeeting,_tmpPreFocusRingerMode,_tmpAllowOverride);
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
