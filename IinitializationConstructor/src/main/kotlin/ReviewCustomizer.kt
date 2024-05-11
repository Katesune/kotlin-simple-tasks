import kotlin.random.Random

var reviewCustomizer: (String) -> String = {it}

inline fun customizeReview(
    review: String,
    customizer: (String) -> String = { reviewCustomizer(it) }
) {
    println(customizer(review))
}

fun changeCustomizerByCommand(command: String) {
    val status: String
    val resetColor = "\u001B[0m"

    val customizer: (String) -> String
    when (command) {
        "skip" -> when(Random.nextInt(0,2)) {
            0 -> {
                status = "There is no review"
                customizer = { review ->
                    "This is not a review" + ".".repeat(3) + " " + review.lowercase()
                }
            }
            else -> {
                status = "There is a negative review"
                val red = "\u001b[38;2;247;56;56m"
                customizer = { review ->
                    red + review + resetColor
                }
            }
        }
        "like" -> when(Random.nextInt(0,2)) {
            0 -> {
                status = "There is a positive review"
                val purple = "\u001b[38;2;108;65;244m"
                customizer = { review ->
                    purple + review.capitalize() + resetColor
                }
            }
            else -> {
                status = "There is a very positive review"
                val purple = "\u001b[38;2;108;65;244m"
                customizer = { review ->
                    purple + review.uppercase() + "!".repeat(3) + resetColor
                }
            }
        }
        else -> {
            status = "There is a neutral review"
            customizer = { review ->
                review.map{"$it"}.joinToString(".")
            }
        }
    }

    reviewCustomizer = customizer
    customizeReview(status)
}