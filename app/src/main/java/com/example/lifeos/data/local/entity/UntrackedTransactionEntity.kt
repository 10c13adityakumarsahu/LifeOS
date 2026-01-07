package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import com.example.lifeos.data.local.entity.TransactionType

@Entity(tableName = "untracked_transactions")
data class UntrackedTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val type: TransactionType,
    val sender: String,
    val body: String,
    val date: LocalDateTime
)
