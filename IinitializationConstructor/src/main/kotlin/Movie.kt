import java.time.format.DateTimeFormatter

class Movie (
    title: String,
    private val initialYearOfProduction: String = "2024",
    countriesOfProduction: Set<Countries>,
    genres: Set<Genre>,
    description: String,
    override val duration: Int,
    val slogan: String = "",
    accessForWatching: AccessForWatching = AccessForWatching.AVAILABLE,

    ): Content(title, countriesOfProduction, genres, description, accessForWatching) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy")
    private val yearOfProduction
        get() = dateFormatter.parse(initialYearOfProduction)


    var review = "I haven't watched this movie yet."
        get() = "$title: $field"
        private set(value) {
            println(review)
            field = value.trim().capitalize()
        }

    override val popularity: String
        get() = when (likesCount) {
            in 0..10 -> "not popular"
            in 11..20 -> "relatively popular"
            else -> "popular"
        }

    init {
        review = "I'm watching this movie now."
        println(review)
    }

    constructor(
        title: String,
        countriesOfProduction: Set<Countries>,
        genres: Set<Genre>,
        description: String,
        duration: Int) : this (
        title = title,
        initialYearOfProduction = "2024",
        countriesOfProduction = countriesOfProduction,
        genres = genres,
        description = description,
        duration = duration,
    )

    constructor(
        title: String,
        initialYearOfProduction: String,
        genres: Set<Genre>,
        description: String,
        duration: Int) : this (
        title = title,
        initialYearOfProduction = initialYearOfProduction,
        countriesOfProduction = setOf<Countries>(),
        genres = genres,
        description = description,
        duration = duration,
    ) {
        review = "I've watched this movie a few times, it's good."
        println(review) }

}
