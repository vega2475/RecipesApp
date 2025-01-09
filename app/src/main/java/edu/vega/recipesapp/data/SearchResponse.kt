package edu.vega.recipesapp.data

data class SearchResponse(
    val results: List<Recipe>?,
    val offset: Int?,
    val number: Int?,
    val totalResults: Int?
)