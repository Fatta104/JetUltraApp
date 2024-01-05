package com.fattanaufal.jetultraapp.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fattanaufal.jetultraapp.data.UltraRepository
import com.fattanaufal.jetultraapp.model.UltraItem
import com.fattanaufal.jetultraapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val  repository: UltraRepository): ViewModel() {
    private val _uiState: MutableStateFlow<UiState<UltraItem>> = MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<UltraItem>> get() = _uiState

    fun getUltraById(ultraId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getUltraItemById(ultraId))
        }
    }

    fun addToFavorites(ultraId: String) {
        viewModelScope.launch {
            repository.addToFavorites(ultraId)
        }
    }
    fun removeFromFavorite(ultraId: String) {
        viewModelScope.launch {
            repository.removeFromFavorites(ultraId)
        }
    }

    fun checkFavorite(ultraId: String, onResult:(Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavorite = repository.isFavorite(ultraId)
            onResult(isFavorite)
        }
    }
}