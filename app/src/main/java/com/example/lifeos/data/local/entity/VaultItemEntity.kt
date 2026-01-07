package com.example.lifeos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VaultItemType {
    TEXT,
    IMAGE
}

@Entity(tableName = "vault_items")
data class VaultItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String, // Text content or Image URI string
    val type: VaultItemType,
    val createdAt: Long = System.currentTimeMillis()
)
