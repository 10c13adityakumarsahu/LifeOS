package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

enum class TransactionType {
    EXPENSE, LEND, BORROW, INCOME
}

enum class RecoveryStatus {
    NONE, PENDING, RECOVERED
}

@Entity(tableName = "finance_transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val category: String,
    val description: String,
    val date: LocalDateTime,
    val type: TransactionType,
    val isRecoverable: Boolean = false,
    val recoveryStatus: RecoveryStatus = RecoveryStatus.NONE,
    val payerOrPayeeName: String? = null, // if lending/borrowing
    val isRecursive: Boolean = false,
    val recurrencePattern: String? = null, // e.g., "Monthly", "Weekly"
    val isBill: Boolean = false,
    val dueDate: LocalDateTime? = null,
    val isPaid: Boolean = false
)
