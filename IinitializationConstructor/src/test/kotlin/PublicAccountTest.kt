import org.junit.jupiter.api.Assertions.*
import kotlin.math.exp
import kotlin.test.Test

class PublicAccountTest {
    // User
    private val user = User("nina@gmail.com", "nina")
    private val storage = Storage()

    private val movie = storage.movies[0]
    private val series = storage.series[0]

    @Test
    fun controlEmptyUserProperties() {
        val emptyEmailException = assertThrows(IllegalArgumentException::class.java) {
            User("", "nina")
        }
        val emptyNickNameException = assertThrows(IllegalArgumentException::class.java) {
            User("nina@gmail.com", "")
        }

        assertAll(
            { assertEquals("The email should not be blank.", emptyEmailException.message) },
            { assertEquals("The nick name should not be blank.", emptyNickNameException.message) }
        )
    }

    @Test
    fun userLikesIncreasing() {
        val expected = 1

        user.likeIt(movie)
        user.likeIt(series)

        assertAll(
            { assertEquals(expected, movie.likesCount) },
            { assertEquals(expected, series.likesCount) },
        )
    }

    @Test
    fun displayUserData() {
        val expected = Unit
        assertEquals(expected, user.displayMainInformation())
    }

    @Test
    fun displayUserFavoriteContent() {
        val expected = Unit

        user.addToFavoriteContent(movie)
        user.addToFavoriteContent(series)

        assertEquals(expected, user.displayLikedContent())
    }

    // Community

    @Test
    fun controlEmptyCommunityProperties() {
        val emptyTitleException = assertThrows(IllegalArgumentException::class.java) {
            Community("")
        }

        assertAll(
            { assertEquals("The title should not be blank.", emptyTitleException.message) },
        )
    }

    private val communityPeopleCount = 25
    private val community = Community("The best movies",communityPeopleCount)
    @Test
    fun communityLikesIncreasing() {
        val expectedForMovie = movie.likesCount + communityPeopleCount
        val expectedForSerial = series.likesCount + communityPeopleCount

        community.likeIt(movie)
        community.likeIt(series)

        assertAll(
            { assertEquals(expectedForMovie, movie.likesCount) },
            { assertEquals(expectedForSerial, series.likesCount) },
        )
    }

    @Test
    fun displayCommunityData() {
        val expected = Unit

        assertEquals(expected, community.displayMainInformation())
    }

    @Test
    fun displayCommunityFavoriteContent() {
        val expected = Unit

        community.addToFavoriteContent(movie)
        community.addToFavoriteContent(series)

        assertEquals(expected, community.displayLikedContent())
    }

    @Test
    fun checkContentAccessNotAvailable() {
        val expectedNotAvailable = AccessForWatching.UNAVAILABLE

        movie.accessForWatching = AccessForWatching.UNAVAILABLE
        series.accessForWatching = AccessForWatching.UNAVAILABLE

        assertAll(
            { assertEquals(expectedNotAvailable, movie.accessForWatching) },
            { assertEquals(expectedNotAvailable, series.accessForWatching) },
        )
    }

    @Test
    fun changeContentAccessForAvailable() {
        val expectedAvailable = AccessForWatching.AVAILABLE
        community.likesCount = 20

        community.requestForAccess(movie)
        community.requestForAccess(series)

        assertAll(
            { assertEquals(expectedAvailable, movie.accessForWatching) },
            { assertEquals(expectedAvailable, series.accessForWatching) },
        )
    }

    // PublicAccountTest

    @Test
    fun addContentToFavorite() {
        val expectedFavoriteContent = mutableSetOf(movie, series)

        user.addToFavoriteContent(movie)
        user.addToFavoriteContent(series)
        community.addToFavoriteContent(movie)
        community.addToFavoriteContent(series)

        assertAll(
            { assertEquals(expectedFavoriteContent, user.favoriteContent) },
            { assertEquals(expectedFavoriteContent, community.favoriteContent) }
        )
    }
}