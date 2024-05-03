import org.example.Admin
import org.example.Moderator
import org.example.Status
import org.example.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.management.relation.Role

internal class UserBaseTest {
    private val userBase = UserBase()

    private val emailFromBase = "polina"
    private val nickNameFromBase = "Polina"
    private val passFromBase = "kEn8djb^Jbcf9"

    private val emailNotFromBase = "new_email@gmail.com"
    private val nickNameNotFromBase = "New Email"
    private val passNotFromBase = "Hts^wp]Qe^fwi"

    private val userFromBase = User(emailFromBase, nickNameFromBase, passFromBase)
    private val userNotFromBase = User(emailNotFromBase, nickNameNotFromBase, passNotFromBase)

    // Test UsersSet<User> with override contains(User)

    @Test
    fun successConvertingDataToUser() {
        val fullUser = User("barbos@gmail.com", "Barbos", "kEn8djb^Jbcf9", User.Role.USER, Status.ACTIVE)
        val fullUserData = "barbos@gmail.com, Barbos, kEn8djb^Jbcf9, USER, ACTIVE"

        val userWithoutStatus = User("barbos@gmail.com", "Barbos", "kEn8djb^Jbcf9", User.Role.USER)
        val userDataWithoutStatus = "barbos@gmail.com, Barbos, kEn8djb^Jbcf9, USER"

        val userWithoutStatusAndRole = User("barbos@gmail.com", "Barbos", "kEn8djb^Jbcf9")
        val userDataWithoutStatusAndRole = "barbos@gmail.com, Barbos, kEn8djb^Jbcf9"

        assertAll(
            { assertEquals(fullUser, userBase.convertToNewUserByRole(fullUserData)) },
            { assertEquals(userWithoutStatus, userBase.convertToNewUserByRole(userDataWithoutStatus)) },
            { assertEquals(userWithoutStatusAndRole, userBase.convertToNewUserByRole(userDataWithoutStatusAndRole)) },
        )
    }

    @Test
    fun convertingDataToUserByRole() {
        val basicUserData = listOf("barbos@gmail.com", "Barbos", "kEn8djb^Jbcf9")

        assertAll(
            { assertTrue(userBase.convertToNewUserByRole(basicUserData.toString()) is User)},
            { assertTrue(userBase.convertToNewUserByRole("$basicUserData, MODERATOR") is Moderator)},
            { assertTrue(userBase.convertToNewUserByRole("$basicUserData, ADMIN") is Admin) },
        )
    }

    @Test
    fun failConvertingDataToUser() {
        val exceptionNotEnoughData = assertThrows(InputDataException::class.java) {
            userBase.convertToNewUserByRole("barbos@gmail.com, Barbos")
        }

        val exceptionTooMuchData = assertThrows(InputDataException::class.java) {
            userBase.convertToNewUserByRole("barbos@gmail.com, Barbos, kEn8djb^Jbcf9, USER, ACTIVE, wiifw;d")
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