package edu.vega.recipesapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import edu.vega.recipesapp.data.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: RecipesViewModel,
    onClickCard: (Int) -> Unit,
    onGoBackToStart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("RecipesApp - Main") },
                navigationIcon = {
                    IconButton(onClick = onGoBackToStart) {
                        Icon(Icons.Filled.Home, contentDescription = "На старт")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Button(
                onClick = { viewModel.fetchRandomRecipes() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Show Random Recipes (5)")
            }

            var searchText by remember { mutableStateOf("") }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    textStyle = TextStyle(color = Color.White),
                    cursorBrush = SolidColor(Color.White)
                )
                Button(onClick = {
                    if (searchText.isNotBlank()) {
                        viewModel.searchRecipes(searchText)
                    }
                }) {
                    Text("Search")
                }
            }

            when (uiState) {
                is UiState.Idle -> { Text("Welcome!") }
                is UiState.Loading -> { Text("Loading...") }
                is UiState.SuccessRandom -> {
                    val recipes = (uiState as UiState.SuccessRandom).recipes
                    RecipeList(
                        recipes = recipes,
                        onClickCard = onClickCard,
                        onLoadMore = { viewModel.loadMoreRandomRecipes() },
                        listState = listState
                    )
                }
                is UiState.SuccessSearch -> {
                    val recipes = (uiState as UiState.SuccessSearch).recipes
                    RecipeList(
                        recipes = recipes,
                        onClickCard = onClickCard,
                        onLoadMore = {},
                        listState = listState
                    )
                }
                is UiState.Error -> {
                    val msg = (uiState as UiState.Error).message
                    Text("Error: $msg")
                }
                is UiState.SuccessDetail -> {}
            }
        }
    }
}

