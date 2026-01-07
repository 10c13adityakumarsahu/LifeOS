package com.example.lifeos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lifeos.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Query("SELECT * FROM finance_transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    
    @Query("SELECT SUM(amount) FROM finance_transactions WHERE type = 'EXPENSE' AND date >= :startDate")
    fun getTotalExpensesSince(startDate: java.time.LocalDateTime): Flow<Double?>
    
    @Query("SELECT * FROM finance_transactions WHERE type IN ('LEND', 'BORROW') AND recoveryStatus = 'PENDING'")
    fun getPendingRecoverables(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM finance_transactions WHERE isRecursive = 1")
    fun getRecursiveTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM finance_transactions WHERE isBill = 1 ORDER BY isPaid ASC, dueDate ASC")
    fun getDuePayments(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    // Goals
    @Query("SELECT * FROM financial_goals ORDER BY isCompleted ASC, id DESC")
    fun getAllGoals(): Flow<List<com.example.lifeos.data.local.entity.GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: com.example.lifeos.data.local.entity.GoalEntity)

    @Update
    suspend fun updateGoal(goal: com.example.lifeos.data.local.entity.GoalEntity)
    
    @Delete
    suspend fun deleteGoal(goal: com.example.lifeos.data.local.entity.GoalEntity)

    // SMS Patterns
    @Query("SELECT * FROM sms_patterns")
    fun getAllSmsPatterns(): Flow<List<com.example.lifeos.data.local.entity.SmsPatternEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSmsPattern(pattern: com.example.lifeos.data.local.entity.SmsPatternEntity)

    @Update
    suspend fun updateSmsPattern(pattern: com.example.lifeos.data.local.entity.SmsPatternEntity)

    @Delete
    suspend fun deleteSmsPattern(pattern: com.example.lifeos.data.local.entity.SmsPatternEntity)

    // Untracked Transactions
    @Query("SELECT * FROM untracked_transactions ORDER BY date DESC")
    fun getAllUntrackedTransactions(): Flow<List<com.example.lifeos.data.local.entity.UntrackedTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUntrackedTransaction(transaction: com.example.lifeos.data.local.entity.UntrackedTransactionEntity)

    @Delete
    suspend fun deleteUntrackedTransaction(transaction: com.example.lifeos.data.local.entity.UntrackedTransactionEntity)

    // People
    @Query("SELECT * FROM people ORDER BY name ASC")
    fun getAllPeople(): Flow<List<com.example.lifeos.data.local.entity.PersonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: com.example.lifeos.data.local.entity.PersonEntity)

    @Delete
    suspend fun deletePerson(person: com.example.lifeos.data.local.entity.PersonEntity)
}
