package edu.vega.recipesapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.vega.recipesapp.BuildConfig
import edu.vega.recipesapp.data.Recipe
import edu.vega.recipesapp.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class SuccessRandom(val recipes: List<Recipe>) : UiState()
    data class SuccessSearch(val recipes: List<Recipe>) : UiState()
    data class SuccessDetail(val recipe: Recipe) : UiState()
    data class Error(val message: String) : UiState()
}

class RecipesViewModel(
    private val repository: RecipesRepository = RecipesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    fun fetchRandomRecipes() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val data = repository.getRandomRecipes(
                    apiKey = BuildConfig.API_KEY
                )
                _uiState.value = UiState.SuccessRandom(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun searchRecipes(query: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val data = repository.searchRecipes(
                    apiKey = BuildConfig.API_KEY,
                    query = query
                )
                _uiState.value = UiState.SuccessSearch(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getDetails(id: Int) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val recipe = repository.getRecipeDetails(
                    apiKey = BuildConfig.API_KEY,
                    recipeId = id
                )
                _uiState.value = UiState.SuccessDetail(recipe)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}