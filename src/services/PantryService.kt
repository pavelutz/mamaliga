package services

import data.repositories.interfaces.PantryRepository
import domain.models.Ingredient
import domain.models.PantryItem

class PantryService(
    private val pantryRepository: PantryRepository,
    private val ingredientService: IngredientService  // To validate ingredients exist
) {

    fun getPantryContents(): List<PantryItem> = pantryRepository.getAll()

    fun addToPantry(ingredientId: Int, quantity: Double) {
        // First check if ingredient exists
        val ingredient = ingredientService.findIngredientById(ingredientId)
            ?: throw IllegalArgumentException("Ingredient with id $ingredientId does not exist")

        // Check if already in pantry
        val existing = pantryRepository.findByIngredient(ingredient)
        if (existing != null) {
            // Update existing
            pantryRepository.update(ingredient, existing.quantity + quantity)
        } else {
            // Add new
            pantryRepository.add(ingredient, quantity)
        }
    }

    fun removeFromPantry(ingredientId: Int, quantity: Double) {
        val ingredient = ingredientService.findIngredientById(ingredientId)
            ?: throw IllegalArgumentException("Ingredient with id $ingredientId does not exist")

        val existing = pantryRepository.findByIngredient(ingredient)
            ?: throw IllegalStateException("Ingredient not in pantry")

        if (existing.quantity < quantity) {
            throw IllegalStateException("Not enough ${ingredient.name} in pantry")
        }

        val newQuantity = existing.quantity - quantity
        if (newQuantity > 0) {
            pantryRepository.update(ingredient, newQuantity)
        } else {
            pantryRepository.remove(ingredient)
        }
    }

    fun updateInPantry(ingredientId: Int, newQuantity: Double) {
        val ingredient = ingredientService.findIngredientById(ingredientId)
            ?: throw IllegalArgumentException("Ingredient with id $ingredientId does not exist")

        // Check if ingredient exists in pantry
        val existing = pantryRepository.findByIngredient(ingredient)
            ?: throw IllegalStateException("Ingredient not in pantry")

        // Update with new quantity
        pantryRepository.update(ingredient, newQuantity)
    }

    fun hasIngredient(ingredient: Ingredient, neededQuantity: Double): Boolean {
        val pantryItem = pantryRepository.findByIngredient(ingredient)
        return pantryItem != null && pantryItem.quantity >= neededQuantity
    }
}