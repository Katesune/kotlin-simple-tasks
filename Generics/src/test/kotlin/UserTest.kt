import org.example.User
import org.example.UserDataException
import org.example.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UserTest {

    private val successPassword = "Metg7gsD7^[2febn22S"
    private val failPassword = "mustang"

    @Test
    fun validatePassCorrect() {
        val successValidator = Validator(successPassword)
        val failValidator = Validator(failPassword)

        assertAll(
            { assertTrue(successValidator.checkAllRules()) },
            { assertFalse(failValidator.checkAllRules()) },
        )
    }

    private val email = "katesune.akk@gmail.com"
    private val nickName = "Katesune"
    private val pass = "Hdiw{w^fDe"

    private val rightUser = User(email, nickName, pass)
    private val rightUserWithDiffEmail = User("katrin.akk@gmail.com", nickName, "Hdiw{w^fDe")
    private val rightUserWithDiffNickName = User(email, "Katrin", pass)
    private val rightUserWithDiffPass = User(email, nickName, "MegsD7^[2febn22S")
    private val rightUserWithDiffStatus = User(email, nickName, pass, User.Status.REMOVED)

    @Test
    fun createUserWithEmptyProperties() {
        val emptyProperty = ""

        val exceptionEmptyEmail = assertThrows(IllegalArgumentException::class.java) {
            User(emptyProperty, nickName, pass)
        }

        val exceptionEmptyNickName = assertThrows(IllegalArgumentException::class.java) {
            User(email, emptyProperty, pass)
        }

        val exceptionEmptyPass = assertThrows(IllegalArgumentException::class.java) {
            User(email, nickName, emptyProperty)
        }

        assertAll(
            { assertEquals("Email must not be empty", exceptionEmptyEmail.message) },
            { assertEquals("Nickname must not be empty", exceptionEmptyNickName.message) },
            { assertEquals("Password must not be empty", exceptionEmptyPass.message) },
        )

    }

    @Test
    fun checkCorrectUserDataWithoutPass() {
        val expected = "$email,$nickName,${User.Status.ACTIVE}"
        assertEquals(expected, rightUser.getUserDataWithoutPass())
    }

    @Test
    fun userEqualsUserOrEmail() {
        assertAll(
            { assertFalse(rightUser == rightUserWithDiffEmail) },
            { assertTrue(rightUser == rightUserWithDiffNickName) },
            { assertTrue(rightUser == rightUserWithDiffPass) },
            { assertTrue(rightUser == rightUserWithDiffStatus) },

            { assertFalse(rightUser.equals(rightUserWithDiffEmail.getEmail())) },
            { assertTrue(rightUser.equals(rightUserWithDiffNickName.getEmail())) },
            { assertTrue(rightUser.equals(rightUserWithDiffPass.getEmail())) },
            { assertTrue(rightUser.equals(rightUserWithDiffStatus.getEmail())) },
        )
    }

    @Test
    fun verifyInputPass() {
        assertAll(
            { assertTrue(rightUser.verifyPass(pass)) },
            { assertFalse(rightUser.verifyPass("Metg7gsD")) },
        )
    }

    @Test
    fun validateNewPass() {
        val expected = Unit

        assertAll(
            { assertEquals(expected, rightUser.changePass("MegsD7^[2feb")) },
            { assertEquals(expected, rightUser.changePass("mustang")) }
        )
    }

    @Test
    fun changeUserStatus() {
        val expectedActive = User.Status.ACTIVE
        val expectedInActive = User.Status.INACTIVE
        val expectedRemoved = User.Status.REMOVED

        assertAll(
            { assertEquals(expectedActive, rightUserWithDiffStatus.changeStatusToActive()) },
            { assertEquals(expectedInActive, rightUser.changeStatusToInActive()) },
            { assertEquals(expectedRemoved, rightUser.changeStatusToRemoved()) },
        )
    }

}