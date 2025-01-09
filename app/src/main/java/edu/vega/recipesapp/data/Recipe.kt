package edu.vega.recipesapp.data

data class RecipeResponse(
    val recipes: List<Recipe>
)

data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val summary: String? = null,
    val instructions: String? = null,
    val extendedIngredients: List<Ingredient>? = null,
    val imageType: String? = null
)

data class Ingredient(
    val id: Int,
    val original: String,
    val name: String
)