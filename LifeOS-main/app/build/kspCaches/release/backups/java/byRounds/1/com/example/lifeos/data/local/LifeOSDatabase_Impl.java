package com.example.lifeos.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.lifeos.data.local.dao.CallbackLogDao;
import com.example.lifeos.data.local.dao.CallbackLogDao_Impl;
import com.example.lifeos.data.local.dao.EventDao;
import com.example.lifeos.data.local.dao.EventDao_Impl;
import com.example.lifeos.data.local.dao.FinanceDao;
import com.example.lifeos.data.local.dao.FinanceDao_Impl;
import com.example.lifeos.data.local.dao.FitnessDao;
import com.example.lifeos.data.local.dao.FitnessDao_Impl;
import com.example.lifeos.data.local.dao.ScheduledMessageDao;
import com.example.lifeos.data.local.dao.ScheduledMessageDao_Impl;
import com.example.lifeos.data.local.dao.SettingsDao;
import com.example.lifeos.data.local.dao.SettingsDao_Impl;
import com.example.lifeos.data.local.dao.TaskDao;
import com.example.lifeos.data.local.dao.TaskDao_Impl;
import com.example.lifeos.data.local.dao.VaultDao;
import com.example.lifeos.data.local.dao.VaultDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class LifeOSDatabase_Impl extends LifeOSDatabase {
  private volatile TaskDao _taskDao;

  private volatile FinanceDao _financeDao;

  private volatile FitnessDao _fitnessDao;

  private volatile SettingsDao _settingsDao;

  private volatile EventDao _eventDao;

  private volatile ScheduledMessageDao _scheduledMessageDao;

  private volatile VaultDao _vaultDao;

  private volatile CallbackLogDao _callbackLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(14) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `startTime` INTEGER, `endTime` INTEGER, `priority` TEXT NOT NULL, `category` TEXT NOT NULL, `flexibilityMinutes` INTEGER NOT NULL, `estimatedDurationMinutes` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `isSkipped` INTEGER NOT NULL, `recurrence` TEXT NOT NULL, `autoReschedule` INTEGER NOT NULL, `thresholdTime` TEXT, `isGoal` INTEGER NOT NULL, `hasSpawnedRecurrence` INTEGER NOT NULL, `spawnedTaskId` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `finance_transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `amount` REAL NOT NULL, `category` TEXT NOT NULL, `description` TEXT NOT NULL, `date` INTEGER NOT NULL, `type` TEXT NOT NULL, `isRecoverable` INTEGER NOT NULL, `recoveryStatus` TEXT NOT NULL, `payerOrPayeeName` TEXT, `isRecursive` INTEGER NOT NULL, `recurrencePattern` TEXT, `isBill` INTEGER NOT NULL, `dueDate` INTEGER, `isPaid` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `fitness_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `value` REAL NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `settings` (`id` INTEGER NOT NULL, `isWaterReminderEnabled` INTEGER NOT NULL, `waterReminderIntervalMinutes` INTEGER NOT NULL, `isSedentaryReminderEnabled` INTEGER NOT NULL, `sedentaryReminderIntervalMinutes` INTEGER NOT NULL, `userName` TEXT NOT NULL, `areNotificationsEnabled` INTEGER NOT NULL, `isSleepModeEnabled` INTEGER NOT NULL, `isAutoReplyEnabled` INTEGER NOT NULL, `sleepWhitelist` TEXT NOT NULL, `pinCode` TEXT, `currentFocusMode` TEXT NOT NULL, `isSilentModeEnabledForFocus` INTEGER NOT NULL, `customReplySleep` TEXT, `customReplyDriving` TEXT, `customReplyMeeting` TEXT, `preFocusRingerMode` INTEGER NOT NULL, `allowOverride` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `financial_goals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `targetAmount` REAL NOT NULL, `currentAmount` REAL NOT NULL, `deadline` INTEGER, `isCompleted` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `date` INTEGER NOT NULL, `type` TEXT NOT NULL, `reminderMinutes` INTEGER NOT NULL, `isRecursive` INTEGER NOT NULL, `scheduledMessageBody` TEXT, `scheduledMessageContact` TEXT, `customCategory` TEXT, `description` TEXT, `scheduledMessageId` INTEGER, `scheduledMessagePlatform` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `scheduled_messages` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `contactName` TEXT NOT NULL, `contactNumber` TEXT NOT NULL, `messageBody` TEXT NOT NULL, `scheduledTime` INTEGER NOT NULL, `platform` TEXT NOT NULL, `status` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vault_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `type` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `callback_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER NOT NULL, `phoneNumber` TEXT NOT NULL, `messageSent` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sms_patterns` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pattern` TEXT NOT NULL, `senderId` TEXT, `type` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `untracked_transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `amount` REAL NOT NULL, `type` TEXT NOT NULL, `sender` TEXT NOT NULL, `body` TEXT NOT NULL, `date` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `people` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd0f1ff45eeb07bfb89bdfb012175db8a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `tasks`");
        db.execSQL("DROP TABLE IF EXISTS `finance_transactions`");
        db.execSQL("DROP TABLE IF EXISTS `fitness_logs`");
        db.execSQL("DROP TABLE IF EXISTS `settings`");
        db.execSQL("DROP TABLE IF EXISTS `financial_goals`");
        db.execSQL("DROP TABLE IF EXISTS `events`");
        db.execSQL("DROP TABLE IF EXISTS `scheduled_messages`");
        db.execSQL("DROP TABLE IF EXISTS `vault_items`");
        db.execSQL("DROP TABLE IF EXISTS `callback_logs`");
        db.execSQL("DROP TABLE IF EXISTS `sms_patterns`");
        db.execSQL("DROP TABLE IF EXISTS `untracked_transactions`");
        db.execSQL("DROP TABLE IF EXISTS `people`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTasks = new HashMap<String, TableInfo.Column>(17);
        _columnsTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("startTime", new TableInfo.Column("startTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("endTime", new TableInfo.Column("endTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("flexibilityMinutes", new TableInfo.Column("flexibilityMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("estimatedDurationMinutes", new TableInfo.Column("estimatedDurationMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("isSkipped", new TableInfo.Column("isSkipped", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("recurrence", new TableInfo.Column("recurrence", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("autoReschedule", new TableInfo.Column("autoReschedule", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("thresholdTime", new TableInfo.Column("thresholdTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("isGoal", new TableInfo.Column("isGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("hasSpawnedRecurrence", new TableInfo.Column("hasSpawnedRecurrence", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTasks.put("spawnedTaskId", new TableInfo.Column("spawnedTaskId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTasks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTasks = new TableInfo("tasks", _columnsTasks, _foreignKeysTasks, _indicesTasks);
        final TableInfo _existingTasks = TableInfo.read(db, "tasks");
        if (!_infoTasks.equals(_existingTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "tasks(com.example.lifeos.data.local.entity.TaskEntity).\n"
                  + " Expected:\n" + _infoTasks + "\n"
                  + " Found:\n" + _existingTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsFinanceTransactions = new HashMap<String, TableInfo.Column>(14);
        _columnsFinanceTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("isRecoverable", new TableInfo.Column("isRecoverable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("recoveryStatus", new TableInfo.Column("recoveryStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("payerOrPayeeName", new TableInfo.Column("payerOrPayeeName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("isRecursive", new TableInfo.Column("isRecursive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("recurrencePattern", new TableInfo.Column("recurrencePattern", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("isBill", new TableInfo.Column("isBill", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("dueDate", new TableInfo.Column("dueDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinanceTransactions.put("isPaid", new TableInfo.Column("isPaid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFinanceTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFinanceTransactions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFinanceTransactions = new TableInfo("finance_transactions", _columnsFinanceTransactions, _foreignKeysFinanceTransactions, _indicesFinanceTransactions);
        final TableInfo _existingFinanceTransactions = TableInfo.read(db, "finance_transactions");
        if (!_infoFinanceTransactions.equals(_existingFinanceTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "finance_transactions(com.example.lifeos.data.local.entity.TransactionEntity).\n"
                  + " Expected:\n" + _infoFinanceTransactions + "\n"
                  + " Found:\n" + _existingFinanceTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsFitnessLogs = new HashMap<String, TableInfo.Column>(4);
        _columnsFitnessLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFitnessLogs.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFitnessLogs.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFitnessLogs.put("value", new TableInfo.Column("value", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFitnessLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFitnessLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFitnessLogs = new TableInfo("fitness_logs", _columnsFitnessLogs, _foreignKeysFitnessLogs, _indicesFitnessLogs);
        final TableInfo _existingFitnessLogs = TableInfo.read(db, "fitness_logs");
        if (!_infoFitnessLogs.equals(_existingFitnessLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "fitness_logs(com.example.lifeos.data.local.entity.FitnessLogEntity).\n"
                  + " Expected:\n" + _infoFitnessLogs + "\n"
                  + " Found:\n" + _existingFitnessLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsSettings = new HashMap<String, TableInfo.Column>(18);
        _columnsSettings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("isWaterReminderEnabled", new TableInfo.Column("isWaterReminderEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("waterReminderIntervalMinutes", new TableInfo.Column("waterReminderIntervalMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("isSedentaryReminderEnabled", new TableInfo.Column("isSedentaryReminderEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("sedentaryReminderIntervalMinutes", new TableInfo.Column("sedentaryReminderIntervalMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("userName", new TableInfo.Column("userName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("areNotificationsEnabled", new TableInfo.Column("areNotificationsEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("isSleepModeEnabled", new TableInfo.Column("isSleepModeEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("isAutoReplyEnabled", new TableInfo.Column("isAutoReplyEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("sleepWhitelist", new TableInfo.Column("sleepWhitelist", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("pinCode", new TableInfo.Column("pinCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("currentFocusMode", new TableInfo.Column("currentFocusMode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("isSilentModeEnabledForFocus", new TableInfo.Column("isSilentModeEnabledForFocus", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("customReplySleep", new TableInfo.Column("customReplySleep", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("customReplyDriving", new TableInfo.Column("customReplyDriving", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("customReplyMeeting", new TableInfo.Column("customReplyMeeting", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("preFocusRingerMode", new TableInfo.Column("preFocusRingerMode", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("allowOverride", new TableInfo.Column("allowOverride", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSettings = new TableInfo("settings", _columnsSettings, _foreignKeysSettings, _indicesSettings);
        final TableInfo _existingSettings = TableInfo.read(db, "settings");
        if (!_infoSettings.equals(_existingSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "settings(com.example.lifeos.data.local.entity.SettingsEntity).\n"
                  + " Expected:\n" + _infoSettings + "\n"
                  + " Found:\n" + _existingSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsFinancialGoals = new HashMap<String, TableInfo.Column>(6);
        _columnsFinancialGoals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinancialGoals.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinancialGoals.put("targetAmount", new TableInfo.Column("targetAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinancialGoals.put("currentAmount", new TableInfo.Column("currentAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinancialGoals.put("deadline", new TableInfo.Column("deadline", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFinancialGoals.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFinancialGoals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFinancialGoals = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFinancialGoals = new TableInfo("financial_goals", _columnsFinancialGoals, _foreignKeysFinancialGoals, _indicesFinancialGoals);
        final TableInfo _existingFinancialGoals = TableInfo.read(db, "financial_goals");
        if (!_infoFinancialGoals.equals(_existingFinancialGoals)) {
          return new RoomOpenHelper.ValidationResult(false, "financial_goals(com.example.lifeos.data.local.entity.GoalEntity).\n"
                  + " Expected:\n" + _infoFinancialGoals + "\n"
                  + " Found:\n" + _existingFinancialGoals);
        }
        final HashMap<String, TableInfo.Column> _columnsEvents = new HashMap<String, TableInfo.Column>(12);
        _columnsEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("reminderMinutes", new TableInfo.Column("reminderMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("isRecursive", new TableInfo.Column("isRecursive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("scheduledMessageBody", new TableInfo.Column("scheduledMessageBody", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("scheduledMessageContact", new TableInfo.Column("scheduledMessageContact", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("customCategory", new TableInfo.Column("customCategory", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("scheduledMessageId", new TableInfo.Column("scheduledMessageId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("scheduledMessagePlatform", new TableInfo.Column("scheduledMessagePlatform", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEvents = new TableInfo("events", _columnsEvents, _foreignKeysEvents, _indicesEvents);
        final TableInfo _existingEvents = TableInfo.read(db, "events");
        if (!_infoEvents.equals(_existingEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "events(com.example.lifeos.data.local.entity.EventEntity).\n"
                  + " Expected:\n" + _infoEvents + "\n"
                  + " Found:\n" + _existingEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsScheduledMessages = new HashMap<String, TableInfo.Column>(7);
        _columnsScheduledMessages.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledMessages.put("contactName", new TableInfo.Column("contactName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledMessages.put("contactNumber", new TableInfo.Column("contactNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledMessages.put("messageBody", new TableInfo.Column("messageBody", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledMessages.put("scheduledTime", new TableInfo.Column("scheduledTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledMessages.put("platform", new TableInfo.Column("platform", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduledMessages.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScheduledMessages = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScheduledMessages = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScheduledMessages = new TableInfo("scheduled_messages", _columnsScheduledMessages, _foreignKeysScheduledMessages, _indicesScheduledMessages);
        final TableInfo _existingScheduledMessages = TableInfo.read(db, "scheduled_messages");
        if (!_infoScheduledMessages.equals(_existingScheduledMessages)) {
          return new RoomOpenHelper.ValidationResult(false, "scheduled_messages(com.example.lifeos.data.local.entity.ScheduledMessageEntity).\n"
                  + " Expected:\n" + _infoScheduledMessages + "\n"
                  + " Found:\n" + _existingScheduledMessages);
        }
        final HashMap<String, TableInfo.Column> _columnsVaultItems = new HashMap<String, TableInfo.Column>(5);
        _columnsVaultItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaultItems.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaultItems.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaultItems.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaultItems.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVaultItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVaultItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVaultItems = new TableInfo("vault_items", _columnsVaultItems, _foreignKeysVaultItems, _indicesVaultItems);
        final TableInfo _existingVaultItems = TableInfo.read(db, "vault_items");
        if (!_infoVaultItems.equals(_existingVaultItems)) {
          return new RoomOpenHelper.ValidationResult(false, "vault_items(com.example.lifeos.data.local.entity.VaultItemEntity).\n"
                  + " Expected:\n" + _infoVaultItems + "\n"
                  + " Found:\n" + _existingVaultItems);
        }
        final HashMap<String, TableInfo.Column> _columnsCallbackLogs = new HashMap<String, TableInfo.Column>(4);
        _columnsCallbackLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallbackLogs.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallbackLogs.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallbackLogs.put("messageSent", new TableInfo.Column("messageSent", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCallbackLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCallbackLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCallbackLogs = new TableInfo("callback_logs", _columnsCallbackLogs, _foreignKeysCallbackLogs, _indicesCallbackLogs);
        final TableInfo _existingCallbackLogs = TableInfo.read(db, "callback_logs");
        if (!_infoCallbackLogs.equals(_existingCallbackLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "callback_logs(com.example.lifeos.data.local.entity.CallbackLogEntity).\n"
                  + " Expected:\n" + _infoCallbackLogs + "\n"
                  + " Found:\n" + _existingCallbackLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsSmsPatterns = new HashMap<String, TableInfo.Column>(4);
        _columnsSmsPatterns.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsPatterns.put("pattern", new TableInfo.Column("pattern", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsPatterns.put("senderId", new TableInfo.Column("senderId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSmsPatterns.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSmsPatterns = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSmsPatterns = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSmsPatterns = new TableInfo("sms_patterns", _columnsSmsPatterns, _foreignKeysSmsPatterns, _indicesSmsPatterns);
        final TableInfo _existingSmsPatterns = TableInfo.read(db, "sms_patterns");
        if (!_infoSmsPatterns.equals(_existingSmsPatterns)) {
          return new RoomOpenHelper.ValidationResult(false, "sms_patterns(com.example.lifeos.data.local.entity.SmsPatternEntity).\n"
                  + " Expected:\n" + _infoSmsPatterns + "\n"
                  + " Found:\n" + _existingSmsPatterns);
        }
        final HashMap<String, TableInfo.Column> _columnsUntrackedTransactions = new HashMap<String, TableInfo.Column>(6);
        _columnsUntrackedTransactions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUntrackedTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUntrackedTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUntrackedTransactions.put("sender", new TableInfo.Column("sender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUntrackedTransactions.put("body", new TableInfo.Column("body", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUntrackedTransactions.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUntrackedTransactions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUntrackedTransactions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUntrackedTransactions = new TableInfo("untracked_transactions", _columnsUntrackedTransactions, _foreignKeysUntrackedTransactions, _indicesUntrackedTransactions);
        final TableInfo _existingUntrackedTransactions = TableInfo.read(db, "untracked_transactions");
        if (!_infoUntrackedTransactions.equals(_existingUntrackedTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "untracked_transactions(com.example.lifeos.data.local.entity.UntrackedTransactionEntity).\n"
                  + " Expected:\n" + _infoUntrackedTransactions + "\n"
                  + " Found:\n" + _existingUntrackedTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsPeople = new HashMap<String, TableInfo.Column>(2);
        _columnsPeople.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPeople.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPeople = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPeople = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPeople = new TableInfo("people", _columnsPeople, _foreignKeysPeople, _indicesPeople);
        final TableInfo _existingPeople = TableInfo.read(db, "people");
        if (!_infoPeople.equals(_existingPeople)) {
          return new RoomOpenHelper.ValidationResult(false, "people(com.example.lifeos.data.local.entity.PersonEntity).\n"
                  + " Expected:\n" + _infoPeople + "\n"
                  + " Found:\n" + _existingPeople);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d0f1ff45eeb07bfb89bdfb012175db8a", "5d5c5d1b201ba35d79cdee45309b068b");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "tasks","finance_transactions","fitness_logs","settings","financial_goals","events","scheduled_messages","vault_items","callback_logs","sms_patterns","untracked_transactions","people");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `tasks`");
      _db.execSQL("DELETE FROM `finance_transactions`");
      _db.execSQL("DELETE FROM `fitness_logs`");
      _db.execSQL("DELETE FROM `settings`");
      _db.execSQL("DELETE FROM `financial_goals`");
      _db.execSQL("DELETE FROM `events`");
      _db.execSQL("DELETE FROM `scheduled_messages`");
      _db.execSQL("DELETE FROM `vault_items`");
      _db.execSQL("DELETE FROM `callback_logs`");
      _db.execSQL("DELETE FROM `sms_patterns`");
      _db.execSQL("DELETE FROM `untracked_transactions`");
      _db.execSQL("DELETE FROM `people`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TaskDao.class, TaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FinanceDao.class, FinanceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FitnessDao.class, FitnessDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SettingsDao.class, SettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EventDao.class, EventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScheduledMessageDao.class, ScheduledMessageDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VaultDao.class, VaultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CallbackLogDao.class, CallbackLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TaskDao taskDao() {
    if (_taskDao != null) {
      return _taskDao;
    } else {
      synchronized(this) {
        if(_taskDao == null) {
          _taskDao = new TaskDao_Impl(this);
        }
        return _taskDao;
      }
    }
  }

  @Override
  public FinanceDao financeDao() {
    if (_financeDao != null) {
      return _financeDao;
    } else {
      synchronized(this) {
        if(_financeDao == null) {
          _financeDao = new FinanceDao_Impl(this);
        }
        return _financeDao;
      }
    }
  }

  @Override
  public FitnessDao fitnessDao() {
    if (_fitnessDao != null) {
      return _fitnessDao;
    } else {
      synchronized(this) {
        if(_fitnessDao == null) {
          _fitnessDao = new FitnessDao_Impl(this);
        }
        return _fitnessDao;
      }
    }
  }

  @Override
  public SettingsDao settingsDao() {
    if (_settingsDao != null) {
      return _settingsDao;
    } else {
      synchronized(this) {
        if(_settingsDao == null) {
          _settingsDao = new SettingsDao_Impl(this);
        }
        return _settingsDao;
      }
    }
  }

  @Override
  public EventDao eventDao() {
    if (_eventDao != null) {
      return _eventDao;
    } else {
      synchronized(this) {
        if(_eventDao == null) {
          _eventDao = new EventDao_Impl(this);
        }
        return _eventDao;
      }
    }
  }

  @Override
  public ScheduledMessageDao scheduledMessageDao() {
    if (_scheduledMessageDao != null) {
      return _scheduledMessageDao;
    } else {
      synchronized(this) {
        if(_scheduledMessageDao == null) {
          _scheduledMessageDao = new ScheduledMessageDao_Impl(this);
        }
        return _scheduledMessageDao;
      }
    }
  }

  @Override
  public VaultDao vaultDao() {
    if (_vaultDao != null) {
      return _vaultDao;
    } else {
      synchronized(this) {
        if(_vaultDao == null) {
          _vaultDao = new VaultDao_Impl(this);
        }
        return _vaultDao;
      }
    }
  }

  @Override
  public CallbackLogDao callbackDao() {
    if (_callbackLogDao != null) {
      return _callbackLogDao;
    } else {
      synchronized(this) {
        if(_callbackLogDao == null) {
          _callbackLogDao = new CallbackLogDao_Impl(this);
        }
        return _callbackLogDao;
      }
    }
  }
}
