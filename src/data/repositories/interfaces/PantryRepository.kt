package data.repositories.interfaces

import domain.models.Ingredient
import domain.models.PantryItem

interface PantryRepository {
    fun getAll(): List<PantryItem>
    fun findByIngredient(ingredient: Ingredient): PantryItem?
    fun add(ingredient: Ingredient, quantity: Double)  // Only for new items
    fun update(ingredient: Ingredient, newQuantity: Double)  // Only for existing
    fun remove(ingredient: Ingredient)
    fun clear()
}