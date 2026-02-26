package ui

import services.*
import ui.views.*

class MenuHandler(
    private val ingredientService: IngredientService,
    private val pantryService: PantryService,
    private val recipeService: RecipeService,
    private val cookingService: CookingService
) {

    private val ingredientView = IngredientView(ingredientService)
    private val pantryView = PantryView(pantryService, ingredientService)
    private val recipeView = RecipeView(recipeService, pantryService)
    private val cookingView = CookingView(cookingService, recipeService, pantryService)

    private val scanner = java.util.Scanner(System.`in`)

    fun start() {
        println("=== Welcome to Your Recipe Manager ===")

        var running = true
        while (running) {
            printMainMenu()
            when (scanner.nextLine()) {
                "1" -> ingredientView.showAllIngredients()
                "2" -> pantryView.showPantryMenu()
                "3" -> recipeView.showRecipeMenu()
                "4" -> cookingView.showCookingMenu()
                "5" -> {
                    println("Goodbye!")
                    running = false
                }
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    private fun printMainMenu() {
        println("\n--- Main Menu ---")
        println("1. View All Ingredients")
        println("2. Manage Pantry")
        println("3. Browse Recipes")
        println("4. Cook Something")
        println("5. Exit")
        print("Choose an option: ")
    }
}