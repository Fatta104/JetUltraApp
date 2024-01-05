package com.fattanaufal.jetultraapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fattanaufal.jetultraapp.data.UltraRepository
import com.fattanaufal.jetultraapp.model.UltraItem
import com.fattanaufal.jetultraapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UltraRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<Map<Char, List<UltraItem>>>> =
        MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<Map<Char, List<UltraItem>>>> get() = _uiState

    private val _searchResult = MutableStateFlow<List<UltraItem>>(emptyList())
    val searchResult: StateFlow<List<UltraItem>> get() = _searchResult

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query

    fun getAllUltras() {
        viewModelScope.launch {
            repository.getSortedAndGroupedUltra()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { groupedUltraItems ->
                    _uiState.value = UiState.Success(groupedUltraItems)
                }
        }
    }

    fun searchUltras() {
        val currentQuery = _query.value
        viewModelScope.launch {
            repository.searchUltras(currentQuery)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect{ searchResult ->
                    _searchResult.value = searchResult
                }
        }
    }
    fun setQuery(newQuery: String) {
        _query.value = newQuery
    }
}