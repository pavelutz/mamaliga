package ui.views

import services.CookingService
import services.RecipeService
import services.PantryService
import domain.models.Recipe
import services.CookingResult

class CookingView(
    private val cookingService: CookingService,
    private val recipeService: RecipeService,
    private val pantryService: PantryService
) {
    private val scanner = java.util.Scanner(System.`in`)

    fun showCookingMenu() {
        var inCookingMenu = true
        while (inCookingMenu) {
            println("\n--- Cooking ---")
            println("1. See What You Can Cook")
            println("2. Cook a Recipe")
            println("3. Scale a Recipe")
            println("4. Back to Main Menu")
            print("Choose an option: ")

            when (scanner.nextLine()) {
                "1" -> showCookableRecipes()
                "2" -> cookRecipe()
                "3" -> scaleRecipe()
                "4" -> inCookingMenu = false
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    private fun showCookableRecipes() {
        println("\n--- Recipes You Can Cook ---")
        val cookable = recipeService.getRecommendedRecipes()

        if (cookable.isEmpty()) {
            println("You don't have enough ingredients to cook anything.")
            return
        }

        cookable.forEach { recipe ->
            println("${recipe.id}. ${recipe.name}")
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }

    private fun cookRecipe() {
        println("\n--- Cook a Recipe ---")

        // Show cookable recipes first
        val cookable = recipeService.getRecommendedRecipes()
        if (cookable.isEmpty()) {
            println("You don't have enough ingredients to cook anything.")
            return
        }

        println("Recipes you can cook:")
        cookable.forEach { recipe ->
            println("${recipe.id}. ${recipe.name}")
        }

        print("\nEnter recipe ID to cook: ")
        val id = scanner.nextLine().toIntOrNull()
        if (id == null) {
            println("Invalid ID")
            return
        }

        val recipe = recipeService.findRecipeById(id)
        if (recipe == null) {
            println("Recipe not found")
            return
        }

        // Confirm
        println("\nYou are about to cook: ${recipe.name}")
        println("This will use:")
        recipe.ingredients.forEach { ri ->
            println("  - ${ri.quantity} ${ri.ingredient.unit} ${ri.ingredient.name}")
        }

        print("\nProceed? (y/n): ")
        if (scanner.nextLine().lowercase() != "y") {
            println("Cooking cancelled")
            return
        }

        // Cook it!
        when (val result = cookingService.cookRecipe(recipe)) {
            is CookingResult.Success -> {
                println("\n✅ Success! Enjoy your ${recipe.name}!")

                // Show updated pantry
                println("\nUpdated pantry:")
                val pantry = pantryService.getPantryContents()
                if (pantry.isEmpty()) {
                    println("Your pantry is now empty")
                } else {
                    pantry.forEach { item ->
                        println("  ${item.ingredient.name}: ${item.quantity} ${item.ingredient.unit}")
                    }
                }
            }
            is CookingResult.Failure -> {
                println("\n❌ Cannot cook this recipe. Missing ingredients:")
                result.missingIngredients.forEach { (ingredient, amount) ->
                    println("  - Need ${amount} more ${ingredient.unit} of ${ingredient.name}")
                }
            }
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }

    private fun scaleRecipe() {
        println("\n--- Scale a Recipe ---")

        print("Enter recipe ID: ")
        val id = scanner.nextLine().toIntOrNull()
        if (id == null) {
            println("Invalid ID")
            return
        }

        val recipe = recipeService.findRecipeById(id)
        if (recipe == null) {
            println("Recipe not found")
            return
        }

        println("\nOriginal recipe: ${recipe.name}")
        println("Original servings: ${recipe.servings}")

        print("Enter scale factor (e.g., 0.5 for half, 2 for double): ")
        val factor = scanner.nextLine().toDoubleOrNull()
        if (factor == null || factor <= 0) {
            println("Invalid factor")
            return
        }

        val scaled = cookingService.scaleRecipe(recipe, factor)

        println("\n=== Scaled Recipe: ${scaled.name} ===")
        println("Servings: ${scaled.servings}")
        println("\nIngredients:")
        scaled.ingredients.forEach { ri ->
            println("  - ${ri.quantity} ${ri.ingredient.unit} ${ri.ingredient.name}")
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }
}