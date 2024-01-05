package com.fattanaufal.jetultraapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fattanaufal.jetultraapp.di.Injection
import com.fattanaufal.jetultraapp.helper.ViewModelFactory
import com.fattanaufal.jetultraapp.model.UltraItem
import com.fattanaufal.jetultraapp.ui.common.UiState
import com.fattanaufal.jetultraapp.ui.components.SearchBar
import com.fattanaufal.jetultraapp.ui.components.UltraListItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (String) -> Unit,
){
    val searchResult by viewModel.searchResult.collectAsState(initial = emptyList())
    val query by viewModel.query.collectAsState(initial = "")

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when(uiState) {
            is UiState.Loading -> {
                viewModel.getAllUltras()
            }

            is UiState.Success -> {
                Column {
                    SearchBar(
                        query = query,
                        onQueryChange ={newQuery ->
                            viewModel.setQuery(newQuery)
                            viewModel.searchUltras()
                        },
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary)
                        )
                    HomeContent(
                        groupedUltras = if (query.isEmpty()) uiState.data else emptyMap(),
                        searchResult = searchResult,
                        modifier = modifier,
                        navigateToDetail = navigateToDetail,
                    )
                }
            }
            is UiState.Error -> {}
        }
    }
}
@Composable
fun HomeContent(
    groupedUltras: Map<Char, List<UltraItem>>,
    searchResult: List<UltraItem>,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.testTag("UltraList")
    ){
        if (searchResult.isNotEmpty()) {
            items(searchResult, key = {it.item.id}) {data ->
                UltraListItem(
                    ultraName = data.item.ultraName,
                    bannerUrl = data.item.bannerUrl,
                    modifier = Modifier.clickable {
                        navigateToDetail(data.item.id)
                    }
                )
            }
        } else{
            groupedUltras.entries.forEach { (_ , ultraItems) ->
                items(ultraItems) {data ->
                    UltraListItem(
                        ultraName = data.item.ultraName,
                        bannerUrl = data.item.bannerUrl,
                        modifier = Modifier.clickable {
                            navigateToDetail(data.item.id)
                        }
                    )
                }
            }
        }
    }
}