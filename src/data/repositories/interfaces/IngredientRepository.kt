package data.repositories.interfaces

import domain.models.Ingredient

interface IngredientRepository {
    fun getAll(): List<Ingredient>
    fun findById(id: Int): Ingredient?
    fun findByName(name: String): Ingredient?
}