class Response (
    val hits: List<Hit> = listOf(),
) {
    companion object {
        fun parseToRecipes(hits: List<Hit>): List<Recipe> {
            return hits.map { it.recipe }
        }
    }
}

class Hit(
    val recipe: Recipe = Recipe()
)

class Recipe(
    val url: String = "",
    private val label: String = "",
    private val calories: String = "",
    private val ingredients: List<Ingredient> = listOf()
) {
    fun printRecipe() {

        val resetColor = "\u001B[0m"
        val whiteBg = "\u001b[48;2;255;255;255m"
        val darkPurpleTextColor = "\u001B[38;2;92;95;175m"
        val blueTextColor = "\u001b[38;2;92;99;175m"

        println()
        println(whiteBg + " " + darkPurpleTextColor + label.uppercase() + " " + resetColor + " " + "calories = $calories")
        println(blueTextColor + url + resetColor)

        for (ingredient in ingredients) ingredient.printIngredient()
        println()
    }
}

class Ingredient(
    private val text: String = "",
    private val quantity: Float = 0f,
    private val measure: String = "",
    private val food: String = "",
    private val weight: Float = 0f
) {

    fun printIngredient() {
        val resetColor = "\u001B[0m"
        val purple = "\u001b[38;2;108;65;244m"

        println(" ".repeat(4) + text )
        print(" ".repeat(8))
        println("${purple + quantity + resetColor} $measure $food; weight = ${purple + weight + resetColor}")
    }
}