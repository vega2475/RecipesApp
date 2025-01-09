package edu.vega.recipesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.vega.recipesapp.ui.AppNavHost
import edu.vega.recipesapp.ui.RecipesViewModel
import edu.vega.recipesapp.ui.theme.RecipesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipesAppTheme {
                val viewModel: RecipesViewModel = viewModel()
                AppNavHost(viewModel)
            }
        }
    }
}