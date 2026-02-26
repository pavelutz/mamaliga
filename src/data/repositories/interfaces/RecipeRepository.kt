package data.repositories.interfaces

import domain.enums.Difficulty
import domain.models.Ingredient
import domain.models.Recipe

interface RecipeRepository {
    fun getAll(): List<Recipe>
    fun findById(id: Int): Recipe?
    fun findByName(name: String): Recipe?
    fun findByIngredient(ingredient: Ingredient): List<Recipe>
    fun findByDifficulty(difficulty: Difficulty): List<Recipe>
}