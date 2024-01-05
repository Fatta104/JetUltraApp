package com.fattanaufal.jetultraapp.ui.screen.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fattanaufal.jetultraapp.R
import com.fattanaufal.jetultraapp.di.Injection
import com.fattanaufal.jetultraapp.helper.ViewModelFactory
import com.fattanaufal.jetultraapp.model.UltraItem
import com.fattanaufal.jetultraapp.ui.common.UiState
import com.fattanaufal.jetultraapp.ui.components.UltraListItem
import com.fattanaufal.jetultraapp.ui.theme.JetUltraAppTheme

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewmodel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateBack: () -> Unit,
    navigateToDetail: (String) -> Unit
){
    val favoriteUltras by viewmodel.favoriteUltras.collectAsState(emptyList())

    viewmodel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is  UiState.Loading -> {
                viewmodel.getAllFavoriteUltras()
            }
            is UiState.Success -> {
                FavoriteContent(
                    favoriteUltras = favoriteUltras,
                    modifier = modifier,
                    onBackClick = navigateBack,
                    navigateToDetail = navigateToDetail
                )
            }
            is UiState.Error -> {}
        }
    }
}

@Composable
fun FavoriteContent(
    favoriteUltras: List<UltraItem>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
){
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Box {
            Row (
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(imageVector = Icons.Default.ArrowBack,
                    tint = Color.Black,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable { onBackClick() }
                )
                Text(
                    text = "Favorite",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                    modifier = modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
            }
        }
        Column {
            if (favoriteUltras.isEmpty()) {
                Text(
                    text = stringResource(R.string.empty_fav),
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth()
                        .testTag("EmptyFavoriteText"),
                    color = Color.LightGray,
                    textAlign = TextAlign.Justify
               )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    ){
                    items(favoriteUltras) { data ->
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
}

@Preview(showBackground = true)
@Composable
fun PreviewFavoriteContent() {
    JetUltraAppTheme {
        FavoriteContent(
            favoriteUltras = listOf(),
            onBackClick = {},
            navigateToDetail = {}
        )
    }
}