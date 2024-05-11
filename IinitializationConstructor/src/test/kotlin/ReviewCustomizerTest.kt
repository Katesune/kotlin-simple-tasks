import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ReviewCustomizerTest {

    private val testReview = "I've always wanted to watch this, but I didn't know I'd be so drawn in."

    private val likeCommand = "like"
    private val skipCommand = "skip"
    private val fakeCommand = "fake"

    @Test
    fun changeCustomizerWithPositiveCommand() {
        changeCustomizerByCommand(likeCommand)
        assertEquals(Unit, customizeReview(testReview))
    }

    @Test
    fun changeCustomizerWithNegativeCommand() {
        changeCustomizerByCommand(skipCommand)
        assertEquals(Unit, customizeReview(testReview))
    }

    @Test
    fun changeCustomizerWithFakeCommand() {
        changeCustomizerByCommand(fakeCommand)
        assertEquals(Unit, customizeReview(testReview))
    }
}