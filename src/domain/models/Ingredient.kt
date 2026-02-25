// Properties: id, name, unit, category? (optional)

package domain.models

import domain.enums.MeasurementUnit

data class Ingredient(
    val id: Int,
    val name: String,
    val unit: MeasurementUnit
)