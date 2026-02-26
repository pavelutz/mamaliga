package services

import data.repositories.interfaces.RecipeRepository
import domain.models.Recipe
import domain.models.Ingredient

class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val pantryService: PantryService  // To check what you have
) {

    fun getAllRecipes(): List<Recipe> = recipeRepository.getAll()

    fun findRecipeById(id: Int): Recipe? = recipeRepository.findById(id)

    fun findRecipesByIngredient(ingredient: Ingredient): List<Recipe> =
        recipeRepository.findByIngredient(ingredient)

    fun searchRecipes(query: String): List<Recipe> =
        recipeRepository.getAll().filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }

    fun getRecommendedRecipes(): List<Recipe> {
        // Return only recipes you have all ingredients for
        return recipeRepository.getAll().filter { recipe ->
            recipe.ingredients.all { recipeIngredient ->
                pantryService.hasIngredient(
                    recipeIngredient.ingredient,
                    recipeIngredient.quantity
                )
            }
        }
    }

    fun getMissingIngredients(recipe: Recipe): Map<Ingredient, Double> {
        // Return what you need to buy for this recipe
        val missing = mutableMapOf<Ingredient, Double>()

        for (recipeIngredient in recipe.ingredients) {
            val ingredient = recipeIngredient.ingredient
            val needed = recipeIngredient.quantity

            if (!pantryService.hasIngredient(ingredient, needed)) {
                val pantryItem = pantryService.getPantryContents()
                    .find { it.ingredient == ingredient }

                val have = pantryItem?.quantity ?: 0.0
                missing[ingredient] = needed - have
            }
        }

        return missing
    }
}