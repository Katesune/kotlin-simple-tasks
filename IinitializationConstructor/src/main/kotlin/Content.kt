abstract class Content (
    val title: String,
    val countriesOfProduction: Set<Countries>,
    private val genres: Set<Genre>,
    private val description: String,
    var accessForWatching: AccessForWatching

) {
    abstract val duration: Int

    init {
        require(title.isNotBlank()) { "The title should not be blank." }
        require(description.isNotBlank()) { "The description should not be blank." }
    }

    var likesCount = 0
        set(value) {
            field += value
        }

    abstract val popularity: String

    private val resetColor = "\u001B[0m"

    private val titleBgColor = "\u001B[48;2;255;255;255m"
    private val titleTextColor = "\u001B[38;2;0;0;0m"

    private val availableForWatchColor = "\u001b[38;2;92;99;175m"
    private val unAvailableForWatchColor = "\u001b[38;108;65;244m"

    fun displayMainInformation() {
        println("\n" + titleBgColor + titleTextColor + title + resetColor)
        println(description)
        for (genre in genres) {
            print("$genre ")
        }
        println("\nDuration - $duration minutes")
        if (accessForWatching == AccessForWatching.AVAILABLE) println(availableForWatchColor + "Available for watching")
        else println(unAvailableForWatchColor + "Not available without subscription")
        println(resetColor)
    }

    fun displayLikes() {
        println("\n$titleBgColor$titleTextColor$title$resetColor likes - $likesCount\n")
    }

}

enum class Countries {
    GreatBritain,
    SouthKorea,
    France,
}

enum class Genre {
    ACTION,
    COMEDY,
    HISTORY,
    DRAMA,
    ROMANCE,
    THRILLER
}

enum class AccessForWatching {
    AVAILABLE,
    UNAVAILABLE
}