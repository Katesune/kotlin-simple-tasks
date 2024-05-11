import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.Serial

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

    @Test
    fun displayMovieData() {
        val movie = Movie(fullMovieTitle, fullMovieYear, fullMovieGenres, fullMovieDescription, fullMovieDuration)

        assertAll(
            { assertEquals(Unit, movie.displayMainInformation()) },
            { assertEquals(Unit, movie.displayLikes()) },
        )
    }

    @Test
    fun displaySerialData() {
        val series = TvSeries(fullSeriesTitle, fullSeriesGenres, fullSeriesDescription, fullSeriesEpisodesNum, fullSeriesEpisodeDuration)

        assertAll(
            { assertEquals(Unit, series.displayMainInformation()) },
            { assertEquals(Unit, series.displayLikes()) },
        )
    }
}