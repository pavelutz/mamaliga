package services

import data.repositories.interfaces.IngredientRepository
import domain.models.Ingredient

class IngredientService(
    private val ingredientRepository: IngredientRepository
) {
    fun getAllIngredients(): List<Ingredient> = ingredientRepository.getAll()

    fun findIngredientById(id: Int): Ingredient? = ingredientRepository.findById(id)

    fun findIngredientByName(name: String): Ingredient? = ingredientRepository.findByName(name)

    // Maybe a search method?
    fun searchIngredients(query: String): List<Ingredient> =
        ingredientRepository.getAll().filter {
            it.name.contains(query, ignoreCase = true)
        }
}