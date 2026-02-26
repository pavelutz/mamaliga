import data.repositories.implementations.*
import services.*
import ui.MenuHandler

fun main() {
    // Create repositories
    val ingredientRepository = InMemoryIngredientRepository()
    val recipeRepository = InMemoryRecipeRepository()
    val pantryRepository = InMemoryPantryRepository()

    // Create services
    val ingredientService = IngredientService(ingredientRepository)
    val pantryService = PantryService(pantryRepository, ingredientService)
    val recipeService = RecipeService(recipeRepository, pantryService)
    val cookingService = CookingService(pantryService, recipeService)

    // Create and start the menu
    val menuHandler = MenuHandler(
        ingredientService,
        pantryService,
        recipeService,
        cookingService
    )

    menuHandler.start()
}