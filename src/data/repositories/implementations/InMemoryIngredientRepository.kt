package data.repositories.implementations

import data.repositories.interfaces.IngredientRepository
import domain.models.Ingredient
import domain.enums.MeasurementUnit

class InMemoryIngredientRepository : IngredientRepository {
    private val ingredients = listOf(
        Ingredient(1, "Flour", MeasurementUnit.CUP),
        Ingredient(2, "Eggs", MeasurementUnit.PIECE),
        Ingredient(3, "Milk", MeasurementUnit.CUP),
        Ingredient(4, "Butter", MeasurementUnit.TABLESPOON),
        Ingredient(5, "Sugar", MeasurementUnit.CUP),
        Ingredient(6, "Salt", MeasurementUnit.TEASPOON),
        Ingredient(7, "Baking Powder", MeasurementUnit.TEASPOON),
        Ingredient(8, "Rice", MeasurementUnit.CUP),
        Ingredient(9, "Chicken Breast", MeasurementUnit.PIECE),
        Ingredient(10, "Olive Oil", MeasurementUnit.TABLESPOON)
    )

    override fun getAll(): List<Ingredient> = ingredients

    override fun findById(id: Int): Ingredient? = ingredients.find { it.id == id }

    override fun findByName(name: String): Ingredient? =
        ingredients.find { it.name.equals(name, ignoreCase = true) }
}