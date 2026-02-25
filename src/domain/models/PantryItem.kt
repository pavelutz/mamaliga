// Properties: ingredient, quantity, lastUpdated, expirationDate? (optional)

package domain.models

data class PantryItem(
    val ingredient: Ingredient,
    var quantity: Double
)