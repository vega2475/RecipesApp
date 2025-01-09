package edu.vega.recipesapp.data

import edu.vega.recipesapp.network.NetworkModule
import edu.vega.recipesapp.network.SpoonacularApi


class RecipesRepository(
    private val api: SpoonacularApi = NetworkModule.api
) {

    suspend fun getRandomRecipes(apiKey: String, count: Int = 5): List<Recipe> {
        val response = api.getRandomRecipes(apiKey, count)
        return response.recipes
    }

    suspend fun searchRecipes(apiKey: String, query: String): List<Recipe> {
        val response = api.searchRecipes(apiKey, query)
        return response.results ?: emptyList()
    }

    suspend fun getRecipeDetails(apiKey: String, recipeId: Int): Recipe {
        return api.getRecipeDetails(recipeId, apiKey)
    }
}