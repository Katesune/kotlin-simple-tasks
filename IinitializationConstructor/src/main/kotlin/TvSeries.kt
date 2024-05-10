class TvSeries (
    title: String,
    countriesOfProduction: Set<Countries>,
    genres: Set<Genre>,
    description: String,
    private val episodesNum: Int,
    private val episodeDuration: Int,
    accessForWatching: AccessForWatching = AccessForWatching.AVAILABLE,

    ): Content(title, countriesOfProduction, genres, description, accessForWatching) {

    override val popularity: String
        get() = when (likesCount) {
            in 0..30 -> "not popular"
            in 31..60 -> "relatively popular"
            else -> "popular"
        }

    override val duration: Int
        get() = episodesNum * episodeDuration

    constructor(
        title: String,
        genres: Set<Genre>,
        description: String,
        episodesNum: Int,
        episodeDuration: Int,
    ): this(
        title = title,
        countriesOfProduction = setOf<Countries>(),
        genres = genres,
        description = description,
        episodesNum = episodesNum,
        episodeDuration = episodeDuration,
    )
}