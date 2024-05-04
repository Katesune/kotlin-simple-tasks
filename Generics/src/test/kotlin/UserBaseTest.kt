import org.example.Admin
import org.example.Moderator
import org.example.Status
import org.example.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.management.relation.Role

internal class UserBaseTest {
    private val userBase = UserBase()

    private val emailFromBase = "frog@mail.com"
    private val nickNameFromBase = "frog"
    private val passFromBase = "kE[H^gTf9"

    private val emailNotFromBase = "rabbit@gmail.com"
    private val nickNameNotFromBase = "rabbit"
    private val passNotFromBase = "kEn8djb^Jbcf9"

    private val userFromBase = User(emailFromBase, nickNameFromBase, passFromBase)
    private val userNotFromBase = User(emailNotFromBase, nickNameNotFromBase, passNotFromBase)

    @Test
    fun successConvertingDataToUser() {
        val userData = listOf(emailNotFromBase,nickNameNotFromBase, passNotFromBase)

        val fullUser = User(userData[0], userData[1], userData[2], Status.ACTIVE, User.Role.USER)
        val fullUserData = "${userData.joinToString(",")}, ACTIVE, USER"

        val userWithoutRole = User(userData[0], userData[1], userData[2],  Status.ACTIVE)
        val userDataWithoutRole = "${userData.joinToString(",")}, ACTIVE"

        val userWithoutStatusAndRole = User(userData[0], userData[1], userData[2])
        val userDataWithoutStatusAndRole = userData.joinToString(",")

        assertAll(
            { assertEquals(fullUser, userBase.convertToNewUserByRole(fullUserData)) },
            { assertEquals(userWithoutRole, userBase.convertToNewUserByRole(userDataWithoutRole)) },
            { assertEquals(userWithoutStatusAndRole, userBase.convertToNewUserByRole(userDataWithoutStatusAndRole)) },
        )
    }

    @Test
    fun convertingDataToUserByRole() {
        val basicUserData = listOf(emailNotFromBase,nickNameNotFromBase, passNotFromBase)

        assertAll(
            { assertTrue(userBase.convertToNewUserByRole("$basicUserData, ACTIVE, MODERATOR") is Moderator)},
            { assertTrue(userBase.convertToNewUserByRole("$basicUserData, ACTIVE, ADMIN") is Admin) },
        )
    }

    @Test
    fun failConvertingDataToUser() {
        val exceptionNotEnoughData = assertThrows(InputDataException::class.java) {
            userBase.convertToNewUserByRole("$emailNotFromBase, $nickNameNotFromBase")
            userBase.convertToNewUserByRole("barbos@gmail.com, Barbos")
        }

        val exceptionTooMuchData = assertThrows(InputDataException::class.java) {
            userBase.convertToNewUserByRole("$emailNotFromBase, $nickNameNotFromBase, $passNotFromBase, USER, ACTIVE, wiifw;d")
        }

        assertAll(
            { assertEquals("There is not enough data to convert the user",
                exceptionNotEnoughData.message) },
            { assertEquals("The data to convert to a user is redundant",
                exceptionTooMuchData.message) },
        )
    }

    @Test
    fun userBaseContainsEmail() {
        assertAll(
            { assertTrue(userBase.contains(emailFromBase)) },
            { assertFalse(userBase.contains(emailNotFromBase)) },
        )
    }

    @Test
    fun getUserIfExistsElseException() {
        val exceptionUserNotExists = assertThrows(UserBaseEditException::class.java) {
            userBase[userNotFromBase]
        }

        assertAll(
            { assertEquals(userFromBase, userBase[userFromBase]) },
            { assertEquals("The user was not found.", exceptionUserNotExists.message) },
        )
    }

    @Test
    fun getUserByEmailElseException() {
        val exceptionUserNotExists = assertThrows(UserBaseEditException::class.java) {
            userBase.getUserByEmail(emailNotFromBase)
        }

        assertAll(
            { assertEquals(userFromBase, userBase.getUserByEmail(emailFromBase)) },
            { assertEquals("The user was not found.", exceptionUserNotExists.message) },
        )
    }

    @Test
    fun addUserIfNotExists() {
        assertAll(
            { assertEquals(Unit, userBase.plusAssign(userNotFromBase)) },
            { assertEquals(userNotFromBase, userBase[userNotFromBase]) },

            { assertEquals(Unit, userBase.plusAssign(userFromBase)) },
        )
    }

    @Test
    fun removeUserIfNotExists() {
        val userToRemove = userBase[userFromBase]

        val userNotExistsAfterRemove = assertThrows(UserBaseEditException::class.java) {
            userBase.minusAssign(userToRemove)
            userBase[userFromBase]
        }

        val userForRemoveDoesntExists = assertThrows(UserBaseEditException::class.java) {
            userBase.minusAssign(userNotFromBase)
        }

        assertAll(
            { assertEquals("The user was not found.", userNotExistsAfterRemove.message) },
            { assertEquals("The user you are trying to remove does not exist.", userForRemoveDoesntExists.message) },
        )
    }
}