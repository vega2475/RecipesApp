package edu.vega.recipesapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import edu.vega.recipesapp.data.Recipe
import edu.vega.recipesapp.utils.stripHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: RecipesViewModel,
    onBack: () -> Unit,
    onGoToStart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Recipe", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onGoToStart) {
                        Text("On Main")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is UiState.Loading -> {
                    Text("Loading...", modifier = Modifier.padding(16.dp))
                }
                is UiState.SuccessDetail -> {
                    val recipe = (uiState as UiState.SuccessDetail).recipe
                    DetailContent(recipe)
                }
                is UiState.Error -> {
                    val msg = (uiState as UiState.Error).message
                    Text("Error: $msg", modifier = Modifier.padding(16.dp))
                }
                else -> {
                    Text("No data for demonstration", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun DetailContent(recipe: Recipe) {
    val instructionsClean = recipe.instructions?.stripHtml() ?: "No instructions"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        recipe.image?.let { url ->
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = instructionsClean,
            style = MaterialTheme.typography.bodyMedium
        )

        recipe.extendedIngredients?.let { ingredients ->
            Spacer(modifier = Modifier.height(12.dp))
            Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
            ingredients.forEach {
                Text("- ${it.original}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
