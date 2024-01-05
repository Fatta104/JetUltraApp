package com.fattanaufal.jetultraapp.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fattanaufal.jetultraapp.data.UltraRepository
import com.fattanaufal.jetultraapp.model.UltraItem
import com.fattanaufal.jetultraapp.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: UltraRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<UltraItem>>> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<List<UltraItem>>> get() = _uiState

    val favoriteUltras: Flow<List<UltraItem>> = repository.getFavoriteUltras()

    fun getAllFavoriteUltras() {
        viewModelScope.launch {
            repository.getFavoriteUltras()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { favoriteUltraItems ->
                    _uiState.value = UiState.Success(favoriteUltraItems)
                }
        }
    }
}