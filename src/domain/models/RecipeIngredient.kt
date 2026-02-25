// Properties: ingredient, quantity, unit? (if different from default)

package domain.models

data class RecipeIngredient(
    val ingredient: Ingredient,
    val quantity: Double,
)