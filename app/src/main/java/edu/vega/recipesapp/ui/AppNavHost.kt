package edu.vega.recipesapp.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavHost(viewModel: RecipesViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start") {
        composable("start") {
            StartScreen(onGoToMain = {
                navController.navigate("main") {
                    popUpTo("start") { inclusive = true }
                }
            })
        }
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onClickCard = { recipeId ->
                    navController.navigate("detail/$recipeId")
                },
                onGoBackToStart = {
                    navController.navigate("start") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
            LaunchedEffect(recipeId) {
                viewModel.getDetails(recipeId)
            }

            DetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onGoToStart = {
                    navController.navigate("start") {
                        popUpTo("detail/{recipeId}") { inclusive = true }
                    }
                }
            )
        }
    }
}

