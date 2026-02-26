package data.repositories.implementations

import data.repositories.interfaces.PantryRepository
import domain.models.Ingredient
import domain.models.PantryItem

class InMemoryPantryRepository : PantryRepository {

    // Mutable list since pantry changes
    private val pantryItems: MutableList<PantryItem> = mutableListOf()

    override fun getAll(): List<PantryItem> = pantryItems.toList() // Return immutable copy

    override fun findByIngredient(ingredient: Ingredient): PantryItem? =
        pantryItems.find { it.ingredient == ingredient }

    override fun add(ingredient: Ingredient, quantity: Double) {
        // Check if already exists
        val existing = findByIngredient(ingredient)
        if (existing != null) {
            throw IllegalStateException("Ingredient already in pantry. Use update() to change quantity.")
        }
        pantryItems.add(PantryItem(ingredient, quantity))
    }

    override fun update(ingredient: Ingredient, newQuantity: Double) {
        val existing = findByIngredient(ingredient)
            ?: throw NoSuchElementException("Ingredient not found in pantry")
        existing.quantity = newQuantity
    }

    override fun remove(ingredient: Ingredient) {
        pantryItems.removeAll { it.ingredient == ingredient }
    }

    override fun clear() {
        pantryItems.clear()
    }
}