package ui.views

import services.IngredientService

class IngredientView(
    private val ingredientService: IngredientService
) {
    private val scanner = java.util.Scanner(System.`in`)

    fun showAllIngredients() {
        println("\n--- All Ingredients ---")
        val ingredients = ingredientService.getAllIngredients()

        if (ingredients.isEmpty()) {
            println("No ingredients found.")
            return
        }

        // Simple formatting for now
        ingredients.forEach { ingredient ->
            println("${ingredient.id}. ${ingredient.name} (${ingredient.unit})")
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }
}