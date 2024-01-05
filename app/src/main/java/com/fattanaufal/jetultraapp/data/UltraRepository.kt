package com.fattanaufal.jetultraapp.data

import android.util.Log
import com.fattanaufal.jetultraapp.model.UltraItem
import com.fattanaufal.jetultraapp.model.UltrasData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UltraRepository {

    private val ultraItem = mutableListOf<UltraItem>()
    private val favoriteUltras = mutableListOf<String>()

    init {
        if (ultraItem.isEmpty()) {
            UltrasData.ultra.forEach {
                ultraItem.add(UltraItem(it, 0))
            }
        }
    }

    fun  getSortedAndGroupedUltra(): Flow<Map<Char, List<UltraItem>>> {
        return flow {
            val sortedUltras = ultraItem.sortedBy { it.item.ultraName }
            val groupedUltras = sortedUltras.groupBy { it.item.ultraName[0] }
            emit(groupedUltras)
        }
    }

    fun getUltraItemById(ultraId: String): UltraItem {
        return ultraItem.first {
            it.item.id == ultraId

        }
    }
    fun searchUltras(query: String): Flow<List<UltraItem>> {
        return flow {
            val filteredUltras = ultraItem.filter {
                it.item.ultraName.contains(query, ignoreCase = true)
            }
            emit(filteredUltras)
        }
    }
    fun getFavoriteUltras(): Flow<List<UltraItem>> {
        return flow {
            val favoriteUltraItems = ultraItem.filter { it.item.id in  favoriteUltras }
            emit(favoriteUltraItems)
        }
    }

    fun addToFavorites(ultraId: String) {
        if (!favoriteUltras.contains(ultraId)) {
            favoriteUltras.add(ultraId)
        }
    }

    fun removeFromFavorites(ultraId: String) {
        favoriteUltras.remove(ultraId)
    }
    fun isFavorite(ultraId: String): Boolean {
        return favoriteUltras.contains(ultraId)
    }

    companion object{
        @Volatile
        private var instance: UltraRepository? = null

        fun getInstance(): UltraRepository = instance ?: synchronized(this) {
            UltraRepository().apply {
                instance = this
            }
        }
    }
}