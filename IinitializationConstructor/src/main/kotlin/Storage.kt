class Storage() {
    private val movieWithMainConstructor: Movie = Movie(
        title = "Parasite",
        initialYearOfProduction = "2019",
        countriesOfProduction = setOf(
            Countries.SouthKorea
        ),
        genres = setOf(
            Genre.DRAMA,
            Genre.COMEDY,
            Genre.THRILLER),
        description = "A South Korean comedy with thriller elements.",
        duration = 131
    )

    private val movieWithAdditionalConstructor = Movie(
        "Fight Club",
        "1999",
        setOf(
            Genre.DRAMA,
            Genre.THRILLER),
        description = "It is based on the 1996 novel by Chuck Palahniuk.",
        duration = 139
    )

    val movies = listOf(
        movieWithMainConstructor,
        //movieWithAdditionalConstructor
    )

    private val tvSeriesWithMainConstructor = TvSeries(
        "How I Met Your Mother",
        setOf(
            Genre.COMEDY,
            Genre.ROMANCE
        ),
        "Ted tells his children about how he met their mother.",
        208,
        22
    )

    private val tvSeriesWithAdditionalConstructor = TvSeries(
        "The big bang theory",
        setOf(
            Genre.COMEDY,
            Genre.DRAMA
        ),
        "A popular American comedy television series.",
        279,
        21
    )

    val series = listOf(
        tvSeriesWithMainConstructor,
        tvSeriesWithAdditionalConstructor
    )

    val users = listOf(
        User("lili.akk@gmail.com", "Lili"),
        //User("nina@mail.com", "Nina"),
       // User("ron@gmail.com", "Ron")
    )

    val communities = listOf(
        Community("The best movies",25),
        //Community("TV series fans", 46),
       // Community("News")
    )

    val communitiesContent = listOf(
        Community("TV series fans", 46),
    )

}