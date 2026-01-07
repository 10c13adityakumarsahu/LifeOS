package com.example.lifeos.ui.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifeos.data.local.entity.TransactionEntity
import com.example.lifeos.data.repository.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(private val financeRepository: FinanceRepository) : ViewModel() {
    val allTransactions: StateFlow<List<TransactionEntity>> = financeRepository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingRecoverables: StateFlow<List<TransactionEntity>> = financeRepository.pendingRecoverables
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val recursiveTransactions: StateFlow<List<TransactionEntity>> = financeRepository.recursiveTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val duePayments: StateFlow<List<TransactionEntity>> = financeRepository.duePayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getTransactionsForDuration(start: java.time.LocalDateTime, end: java.time.LocalDateTime): List<TransactionEntity> {
        return (allTransactions.value).filter { it.date in start..end }
    }

    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            financeRepository.addTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            financeRepository.deleteTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            financeRepository.updateTransaction(transaction)
        }
    }

    // Goals
    val allGoals = financeRepository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addGoal(name: String, target: Double) {
        viewModelScope.launch {
            financeRepository.addGoal(com.example.lifeos.data.local.entity.GoalEntity(name = name, targetAmount = target))
        }
    }
    
    fun updateGoalAmount(goal: com.example.lifeos.data.local.entity.GoalEntity, amountToAdd: Double) {
        viewModelScope.launch {
            financeRepository.updateGoal(goal.copy(currentAmount = goal.currentAmount + amountToAdd))
        }
    }

    // People
    val allPeople = financeRepository.allPeople
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addPerson(name: String) {
        viewModelScope.launch {
            financeRepository.addPerson(com.example.lifeos.data.local.entity.PersonEntity(name = name))
        }
    }

    fun settleAllForPerson(personName: String) {
        viewModelScope.launch {
            val personTransactions = (allTransactions.value).filter { it.payerOrPayeeName == personName }
            personTransactions.forEach { t ->
                if (t.type == com.example.lifeos.data.local.entity.TransactionType.LEND) {
                    financeRepository.updateTransaction(t.copy(recoveryStatus = com.example.lifeos.data.local.entity.RecoveryStatus.RECOVERED))
                } else if (t.type == com.example.lifeos.data.local.entity.TransactionType.BORROW) {
                    financeRepository.updateTransaction(t.copy(isPaid = true))
                }
            }
        }
    }
}
