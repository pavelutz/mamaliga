// Properties: id, name, description, instructions, servings,
// preparationTime, difficulty, ingredients (List<RecipeIngredient>)

package domain.models

import domain.enums.Difficulty

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val instructions: List<String>,
    val servings: Int,
    val preparationTime: Int, // in minutes
    val difficulty: Difficulty,
    val ingredients: List<RecipeIngredient>
)