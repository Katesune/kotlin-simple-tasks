import org.example.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UserBaseTest {
    private val userBase = UserBase()
    private val users = userBase.getUsers()

    private val emailFromBase = "katesune.akk@gmail.com"
    private val nickNameFromBase = "Katesune"
    private val passFromBase = "kEn8djb^Jbcf9"

    private val emailNotFromBase = "new_email@gmail.com"
    private val nickNameNotFromBase = "New Email"
    private val passNotFromBase = "Hts^wp]Qe^fwi"

    private val userFromBase = User(emailFromBase, nickNameFromBase, passFromBase)
    private val userNotFromBase = User(emailNotFromBase, nickNameNotFromBase, passNotFromBase)

    // Test UsersSet<User> with override contains(User)
    @Test
    fun setContainsUser() {
        assertAll(
            { assertTrue(users.contains(userFromBase)) },
            { assertFalse(users.contains(userNotFromBase)) },
        )
    }

    //Test UserBase with UsersSet<User>
    //users: UsersSet<User>

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