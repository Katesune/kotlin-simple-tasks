import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class PublicAccountTest {
    // User
    private val user = User("nina@gmail.com", "nina")
    private val storage = Storage()

    private val movie = storage.movies[0]
    private val serial = storage.series[0]

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
        user.likeIt(serial)

        assertAll(
            { assertEquals(expected, movie.likesCount) },
            { assertEquals(expected, serial.likesCount) },
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
        assertEquals(expected, user.displayFavoriteContent())
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
        val expected = communityPeopleCount + 1

        community.likeIt(movie)
        community.likeIt(serial)

        assertAll(
            { assertEquals(expected, movie.likesCount) },
            { assertEquals(expected, serial.likesCount) },
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
        assertEquals(expected, community.displayFavoriteContent())
    }

    @Test
    fun checkContentAccessNotAvailable() {
        val expectedNotAvailable = AccessForWatching.UNAVAILABLE

        movie.accessForWatching = AccessForWatching.UNAVAILABLE
        serial.accessForWatching = AccessForWatching.UNAVAILABLE

        assertAll(
            { assertEquals(expectedNotAvailable, movie.accessForWatching) },
            { assertEquals(expectedNotAvailable, serial.accessForWatching) },
        )
    }

    @Test
    fun changeContentAccessForAvailable() {
        val expectedAvailable = AccessForWatching.AVAILABLE
        community.likesCount = 20

        community.requestForAccess(movie)
        community.requestForAccess(serial)

        assertAll(
            { assertEquals(expectedAvailable, movie.accessForWatching) },
            { assertEquals(expectedAvailable, serial.accessForWatching) },
        )
    }
}