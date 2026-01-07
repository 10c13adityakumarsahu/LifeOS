package com.example.lifeos.data.repository

import com.example.lifeos.data.local.dao.VaultDao
import com.example.lifeos.data.local.entity.VaultItemEntity
import kotlinx.coroutines.flow.Flow

class VaultRepository(private val dao: VaultDao) {
    val allItems: Flow<List<VaultItemEntity>> = dao.getAllItems()

    suspend fun addItem(item: VaultItemEntity) {
        dao.insertItem(item)
    }

    suspend fun deleteItem(item: VaultItemEntity) {
        dao.deleteItem(item)
    }
}
