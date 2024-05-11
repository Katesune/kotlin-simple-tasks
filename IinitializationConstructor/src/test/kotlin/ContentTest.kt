import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.Serial
import kotlin.math.sign

class ContentTest {
    private val fullMovieTitle = "Parasite"
    private val fullMovieYear = "2019"
    private val fullMovieCountries = setOf(Countries.SouthKorea)
    private val fullMovieGenres = setOf(Genre.DRAMA, Genre.COMEDY, Genre.THRILLER)
    private val fullMovieDescription = "A South Korean comedy with thriller elements."
    private val fullMovieDuration = 131

    private val fullSeriesTitle = "How I Met Your Mother"
    private val fullSeriesGenres = setOf( Genre.COMEDY, Genre.ROMANCE)
    private val fullSeriesDescription = "Ted tells his children about how he met their mother."
    private val fullSeriesEpisodesNum = 208
    private val fullSeriesEpisodeDuration = 22

    @Test
    fun controlEmptyPropertiesMovie() {
        val emptyTitleException = assertThrows(IllegalArgumentException::class.java) {
            Movie("", fullMovieYear, fullMovieCountries, fullMovieGenres, fullMovieDescription, fullMovieDuration)
        }

        val emptyDescriptionException = assertThrows(IllegalArgumentException::class.java) {
            Movie(fullMovieTitle, fullMovieYear, fullMovieCountries, fullMovieGenres, "", fullMovieDuration)
        }

        assertAll(
            { assertEquals("The title should not be blank.", emptyTitleException.message) },
            { assertEquals("The description should not be blank.", emptyDescriptionException.message) },
        )
    }

    @Test
    fun controlEmptyPropertiesSeries() {
        val emptyTitleException = assertThrows(IllegalArgumentException::class.java) {
            TvSeries("", fullSeriesGenres, fullSeriesDescription, fullSeriesEpisodesNum, fullSeriesEpisodeDuration)
        }

        val emptyDescriptionException = assertThrows(IllegalArgumentException::class.java) {
            TvSeries(fullSeriesTitle, fullSeriesGenres, "", fullSeriesEpisodesNum, fullSeriesEpisodeDuration)
        }

        assertAll(
            { assertEquals("The title should not be blank.", emptyTitleException.message) },
            { assertEquals("The description should not be blank.", emptyDescriptionException.message) },
        )
    }

    private val movie = Movie(fullMovieTitle, fullMovieYear, fullMovieGenres, fullMovieDescription, fullMovieDuration)

    @Test
    fun displayMovieData() {
        val expected = Unit

        assertAll(
            { assertEquals(expected, movie.displayMainInformation()) },
            { assertEquals(expected, movie.displayLikes()) },
        )
    }

    private val series = TvSeries(fullSeriesTitle, fullSeriesGenres, fullSeriesDescription, fullSeriesEpisodesNum, fullSeriesEpisodeDuration)
    @Test
    fun displaySerialData() {
        val expected = Unit

        assertAll(
            { assertEquals(expected, series.displayMainInformation()) },
            { assertEquals(expected, series.displayLikes()) },
        )
    }

    @Test
    fun changeConfigureMovieReview() {
        val expected = Unit

        val likeCommand = "like"
        val skipCommand = "skip"
        val errorCommand = "getReview"

        assertAll(
            { assertEquals(expected, movie.configureReview(likeCommand)) },
            { assertEquals(expected, movie.configureReview(skipCommand)) },
            { assertEquals(expected, movie.configureReview(errorCommand)) },
        )
    }

    @Test
    fun changeConfigureSeriesReview() {
        val expected = Unit

        val likeCommand = "like"
        val skipCommand = "skip"
        val fakeCommand = "fake"

        assertAll(
            { assertEquals(expected, series.configureReview(likeCommand)) },
            { assertEquals(expected, series.configureReview(skipCommand)) },
            { assertEquals(expected, series.configureReview(fakeCommand)) },
        )
    }
}