package edu.vega.recipesapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import edu.vega.recipesapp.data.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: RecipesViewModel,
    onClickCard: (Int) -> Unit,
    onGoBackToStart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("RecipesApp - Main", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onGoBackToStart) {
                        Icon(Icons.Filled.Home, contentDescription = "На старт")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Button(
                onClick = { viewModel.fetchRandomRecipes() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Show Random Recipes")
            }

            var searchText by remember { mutableStateOf("") }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
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
                is UiState.Idle -> {
                    Text(
                        "Welcome! Click «Show Random» or use search.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is UiState.Loading -> {
                    Text("Loading...", modifier = Modifier.padding(16.dp))
                }
                is UiState.SuccessRandom -> {
                    val recipes = (uiState as UiState.SuccessRandom).recipes
                    RecipeList(recipes, onClickCard)
                }
                is UiState.SuccessSearch -> {
                    val recipes = (uiState as UiState.SuccessSearch).recipes
                    RecipeList(recipes, onClickCard)
                }
                is UiState.Error -> {
                    val msg = (uiState as UiState.Error).message
                    Text("Error: $msg", modifier = Modifier.padding(16.dp))
                }

                is UiState.SuccessDetail -> Text("")
            }
        }
    }
}

@Composable
fun RecipeList(recipes: List<Recipe>, onClickCard: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(recipes) { recipe ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(recipe.title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    recipe.image?.let { imageUrl ->
                        val painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = recipe.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { onClickCard(recipe.id) }) {
                        Text("About")
                    }
                }
            }
        }
    }
}
