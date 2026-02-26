package ui.views

import services.RecipeService
import services.PantryService
import domain.models.Recipe

class RecipeView(
    private val recipeService: RecipeService,
    private val pantryService: PantryService
) {
    private val scanner = java.util.Scanner(System.`in`)

    fun showRecipeMenu() {
        var inRecipeMenu = true
        while (inRecipeMenu) {
            println("\n--- Recipe Browser ---")
            println("1. Show All Recipes")
            println("2. Search Recipes")
            println("3. Show Recipe Details")
            println("4. Show Recommended Recipes (What You Can Make)")
            println("5. Back to Main Menu")
            print("Choose an option: ")

            when (scanner.nextLine()) {
                "1" -> showAllRecipes()
                "2" -> searchRecipes()
                "3" -> showRecipeDetails()
                "4" -> showRecommendedRecipes()
                "5" -> inRecipeMenu = false
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    private fun showAllRecipes() {
        println("\n--- All Recipes ---")
        val recipes = recipeService.getAllRecipes()

        if (recipes.isEmpty()) {
            println("No recipes found.")
        } else {
            recipes.forEach { recipe ->
                println("${recipe.id}. ${recipe.name} (${recipe.difficulty}) - ${recipe.preparationTime} min")
            }
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }

    private fun searchRecipes() {
        println("\n--- Search Recipes ---")
        print("Enter search term: ")
        val query = scanner.nextLine()

        if (query.isBlank()) {
            println("Search term cannot be empty")
            return
        }

        val results = recipeService.searchRecipes(query)

        if (results.isEmpty()) {
            println("No recipes found matching '$query'")
        } else {
            println("Found ${results.size} recipe(s):")
            results.forEach { recipe ->
                println("${recipe.id}. ${recipe.name}")
            }
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }

    private fun showRecipeDetails() {
        println("\n--- Recipe Details ---")
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

        displayRecipeDetails(recipe)
    }

    private fun showRecommendedRecipes() {
        println("\n--- Recipes You Can Make ---")
        val recommendations = recipeService.getRecommendedRecipes()

        if (recommendations.isEmpty()) {
            println("You don't have enough ingredients to make any recipes.")
            println("Try adding more items to your pantry!")
        } else {
            println("You can make these recipes right now:")
            recommendations.forEach { recipe ->
                println("${recipe.id}. ${recipe.name}")
            }
            println("\nUse option 3 to view details of a specific recipe.")
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }

    private fun displayRecipeDetails(recipe: Recipe) {
        println("\n=== ${recipe.name} ===")
        println("Description: ${recipe.description}")
        println("Difficulty: ${recipe.difficulty}")
        println("Prep Time: ${recipe.preparationTime} minutes")
        println("Servings: ${recipe.servings}")

        println("\nIngredients:")
        recipe.ingredients.forEach { ri ->
            val inPantry = pantryService.getPantryContents()
                .find { it.ingredient == ri.ingredient }
            val have = inPantry?.quantity ?: 0.0

            val status = if (have >= ri.quantity) "✅" else "❌"
            println("  $status ${ri.quantity} ${ri.ingredient.unit} ${ri.ingredient.name} (you have: $have)")
        }

        println("\nInstructions:")
        recipe.instructions.forEachIndexed { index, step ->
            println("  ${index + 1}. $step")
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }
}