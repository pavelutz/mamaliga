package ui.views

import services.PantryService
import services.IngredientService

class PantryView(
    private val pantryService: PantryService,
    private val ingredientService: IngredientService
) {
    private val scanner = java.util.Scanner(System.`in`)

    fun showPantryMenu() {
        var inPantryMenu = true
        while (inPantryMenu) {
            println("\n--- Pantry Management ---")
            println("1. View Pantry Contents")
            println("2. Add Ingredient to Pantry")
            println("3. Update Ingredient Quantity")
            println("4. Remove Ingredient from Pantry")
            println("5. Back to Main Menu")
            print("Choose an option: ")

            when (scanner.nextLine()) {
                "1" -> showPantryContents()
                "2" -> addToPantry()
                "3" -> updateQuantity()
                "4" -> removeFromPantry()
                "5" -> inPantryMenu = false
                else -> println("Invalid option. Please try again.")
            }
        }
    }

    private fun showPantryContents() {
        println("\n--- Your Pantry ---")
        val items = pantryService.getPantryContents()

        if (items.isEmpty()) {
            println("Your pantry is empty.")
        } else {
            items.forEach { item ->
                println("${item.ingredient.name}: ${item.quantity} ${item.ingredient.unit}")
            }
        }

        println("\nPress Enter to continue...")
        scanner.nextLine()
    }

    private fun addToPantry() {
        println("\n--- Add Ingredient to Pantry ---")

        // Show available ingredients first
        println("Available ingredients:")
        ingredientService.getAllIngredients().forEach {
            println("${it.id}. ${it.name}")
        }

        print("Enter ingredient ID: ")
        val id = scanner.nextLine().toIntOrNull()
        if (id == null) {
            println("Invalid ID")
            return
        }

        val ingredient = ingredientService.findIngredientById(id)
        if (ingredient == null) {
            println("Ingredient not found")
            return
        }

        print("Enter quantity (in ${ingredient.unit}): ")
        val quantity = scanner.nextLine().toDoubleOrNull()
        if (quantity == null || quantity <= 0) {
            println("Invalid quantity")
            return
        }

        pantryService.addToPantry(id, quantity)
        println("Added ${quantity} ${ingredient.unit} of ${ingredient.name} to pantry")
    }

    private fun updateQuantity() {
        // Similar to add but for updating existing items
        println("\n--- Update Pantry Quantity ---")
        showPantryContents()

        if (pantryService.getPantryContents().isEmpty()) {
            return
        }

        print("Enter ingredient ID to update: ")
        val id = scanner.nextLine().toIntOrNull() ?: run {
            println("Invalid ID")
            return
        }

        print("Enter new quantity: ")
        val quantity = scanner.nextLine().toDoubleOrNull()
        if (quantity == null || quantity < 0) {
            println("Invalid quantity")
            return
        }

        pantryService.updateInPantry(id, quantity)
        println("Quantity updated")
    }

    private fun removeFromPantry() {
        println("\n--- Remove Ingredient from Pantry ---")
        showPantryContents()

        if (pantryService.getPantryContents().isEmpty()) {
            return
        }

        print("Enter ingredient ID to remove: ")
        val id = scanner.nextLine().toIntOrNull() ?: run {
            println("Invalid ID")
            return
        }

        print("Enter quantity to remove: ")
        val quantity = scanner.nextLine().toDoubleOrNull()
        if (quantity == null || quantity <= 0) {
            println("Invalid quantity")
            return
        }

        try {
            pantryService.removeFromPantry(id, quantity)
            println("Removed $quantity from pantry")
        } catch (e: IllegalStateException) {
            println("Error: ${e.message}")
        }
    }
}