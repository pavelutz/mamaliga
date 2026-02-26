package data.repositories.implementations

import data.repositories.interfaces.RecipeRepository
import domain.enums.*
import domain.models.*

class InMemoryRecipeRepository : RecipeRepository {

    // First, create references to ingredients
    private val eggs = Ingredient(2, "Eggs", MeasurementUnit.PIECE)
    private val milk = Ingredient(3, "Milk", MeasurementUnit.CUP)
    private val butter = Ingredient(4, "Butter", MeasurementUnit.TABLESPOON)
    private val flour = Ingredient(1, "Flour", MeasurementUnit.CUP)
    private val sugar = Ingredient(5, "Sugar", MeasurementUnit.CUP)
    private val salt = Ingredient(6, "Salt", MeasurementUnit.TEASPOON)
    private val bakingPowder = Ingredient(7, "Baking Powder", MeasurementUnit.TEASPOON)

    // Then build recipes that use these ingredients
    private val recipes = listOf(
        Recipe(
            id = 1,
            name = "Scrambled Eggs",
            description = "Fluffy scrambled eggs perfect for breakfast",
            instructions = listOf(
                "Crack eggs into a bowl and beat well",
                "Add a splash of milk and mix",
                "Pour into buttered pan over medium heat",
                "Stir gently until cooked to your liking"
            ),
            servings = 1,
            preparationTime = 5,
            difficulty = Difficulty.EASY,
            ingredients = listOf(
                RecipeIngredient(eggs, 2.0),
                RecipeIngredient(milk, 0.25),
                RecipeIngredient(butter, 1.0)
            )
        ),
        Recipe(
            id = 2,
            name = "Pancakes",
            description = "Simple fluffy pancakes",
            instructions = listOf(
                "Mix dry ingredients in a bowl",
                "Add milk and eggs, whisk until smooth",
                "Pour batter onto hot griddle",
                "Flip when bubbles form, cook until golden"
            ),
            servings = 2,
            preparationTime = 15,
            difficulty = Difficulty.EASY,
            ingredients = listOf(
                RecipeIngredient(flour, 1.0),
                RecipeIngredient(milk, 0.75),
                RecipeIngredient(eggs, 1.0),
                RecipeIngredient(sugar, 1.0),
                RecipeIngredient(bakingPowder, 2.0),
                RecipeIngredient(salt, 0.5),
                RecipeIngredient(butter, 2.0)
            )
        )
    )

    override fun getAll(): List<Recipe> = recipes

    override fun findById(id: Int): Recipe? = recipes.find { it.id == id }

    override fun findByName(name: String): Recipe? =
        recipes.find { it.name.equals(name, ignoreCase = true) }

    override fun findByIngredient(ingredient: Ingredient): List<Recipe> =
        recipes.filter { recipe ->
            recipe.ingredients.any { it.ingredient == ingredient }
        }

    override fun findByDifficulty(difficulty: Difficulty): List<Recipe> =
        recipes.filter { it.difficulty == difficulty }
}