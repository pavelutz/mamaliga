package services

import domain.models.Ingredient
import domain.models.Recipe
import domain.models.RecipeIngredient

class CookingService(
    private val pantryService: PantryService,
    private val recipeService: RecipeService
) {

    fun canCook(recipe: Recipe): Boolean {
        // Reuse the recommendation logic
        return recipeService.getRecommendedRecipes().contains(recipe)
    }

    fun cookRecipe(recipe: Recipe): CookingResult {
        // First check if we can cook it
        if (!canCook(recipe)) {
            val missing = recipeService.getMissingIngredients(recipe)
            return CookingResult.Failure(missing)
        }

        // Subtract all ingredients from pantry
        for (recipeIngredient in recipe.ingredients) {
            pantryService.removeFromPantry(
                recipeIngredient.ingredient.id,
                recipeIngredient.quantity
            )
        }

        return CookingResult.Success
    }

    fun scaleRecipe(recipe: Recipe, factor: Double): Recipe {
        // Return a new recipe with quantities scaled
        val scaledIngredients = recipe.ingredients.map {
            RecipeIngredient(it.ingredient, it.quantity * factor)
        }

        return recipe.copy(
            ingredients = scaledIngredients,
            servings = (recipe.servings * factor).toInt()
        )
    }
}

sealed class CookingResult {
    object Success : CookingResult()
    data class Failure(val missingIngredients: Map<Ingredient, Double>) : CookingResult()
}