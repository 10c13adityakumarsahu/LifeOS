package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.FinanceDao
import com.example.lifeos.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class FinanceRepository(private val financeDao: FinanceDao) {
    val allTransactions: Flow<List<TransactionEntity>> = financeDao.getAllTransactions()
    
    val pendingRecoverables: Flow<List<TransactionEntity>> = financeDao.getPendingRecoverables()
    
    val recursiveTransactions: Flow<List<TransactionEntity>> = financeDao.getRecursiveTransactions()
    
    val duePayments: Flow<List<TransactionEntity>> = financeDao.getDuePayments()

    fun getTotalExpensesSince(date: LocalDateTime): Flow<Double?> = financeDao.getTotalExpensesSince(date)

    suspend fun addTransaction(transaction: TransactionEntity) = financeDao.insertTransaction(transaction)
    
    suspend fun updateTransaction(transaction: TransactionEntity) = financeDao.updateTransaction(transaction)
    
    suspend fun deleteTransaction(transaction: TransactionEntity) = financeDao.deleteTransaction(transaction)

    // Goals
    val allGoals: Flow<List<com.example.lifeos.data.local.entity.GoalEntity>> = financeDao.getAllGoals()
    suspend fun addGoal(goal: com.example.lifeos.data.local.entity.GoalEntity) = financeDao.insertGoal(goal)
    suspend fun updateGoal(goal: com.example.lifeos.data.local.entity.GoalEntity) = financeDao.updateGoal(goal)
    suspend fun deleteGoal(goal: com.example.lifeos.data.local.entity.GoalEntity) = financeDao.deleteGoal(goal)
    
    // People
    val allPeople: Flow<List<com.example.lifeos.data.local.entity.PersonEntity>> = financeDao.getAllPeople()
    suspend fun addPerson(person: com.example.lifeos.data.local.entity.PersonEntity) = financeDao.insertPerson(person)
    suspend fun deletePerson(person: com.example.lifeos.data.local.entity.PersonEntity) = financeDao.deletePerson(person)

}
