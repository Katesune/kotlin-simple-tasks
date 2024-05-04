import org.example.Admin
import org.example.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

internal class PageTest {
    // NewsPage
    private val news = mutableListOf(
        News("Two singers have released a new fit", "A lot of people liked the new song", "singer,fit,release"),
        News("The city needs volunteers", "Volunteers will help toads cross the road", "volunteer,road,toads"),
    )

    private val newsPage = NewsPage("Latest news", news)

    @Test
    fun displayEntireNewPage() {
        assertAll(
            { assertEquals(Unit, newsPage.openPage()) },
            { assertEquals(Unit, newsPage.printContent()) },
            { assertEquals(Unit, newsPage.printWholePage()) },
        )
    }

    //CommentsPage
    private val comments = mutableListOf(
        Comment("cat@gmail.com", "Add more colors", 25),
        Comment("cat@gmail.com", "Add reactions to comments", 23),
    )

    private val commentsPage = CommentsPage("Latest comments", comments)

    @Test
    fun displayEntireCommentsPage() {
        assertAll(
            { assertEquals(Unit, commentsPage.openPage()) },
            { assertEquals(Unit, commentsPage.printContent()) },
        )
    }

    //PersonalPage
    private val currentUser = User("frog@mail.com", "frog", "kE[H^gTf9")
    private val userBase = UserBase()
    private val personalPage = PersonalPage<User>(currentUser, userBase)

    @Test
    fun displayEntirePersonalPage() {
        assertAll(
            { assertEquals(Unit, personalPage.openPage()) },
            { assertEquals(Unit, personalPage.printContent()) },
            { assertEquals(Unit, personalPage.printWholePage()) },
        )
    }

    // Testing change user data functions from UserManipulative interface
    @Test
    fun changePersonalEmail() {
        val expected = Unit
        val emptyEmailException = assertThrows(InputDataException::class.java) {
            personalPage.changeEmail("")
        }

        assertAll(
            { assertEquals("New data must not be blank", emptyEmailException.message) },
            { assertEquals(expected, personalPage.changeEmail("bobaGin@gmail.com")) },
            { assertEquals(expected, personalPage.printContent()) },
        )
    }

    @Test
    fun changePersonalNick() {
        val expected = Unit
        val emptyNickException = assertThrows(InputDataException::class.java) {
            personalPage.changeNickName("")
        }

        assertAll(
            { assertEquals("New data must not be blank", emptyNickException.message) },
            { assertEquals(expected, personalPage.changeNickName("Yola")) },
            { assertEquals(expected, personalPage.printContent()) },
        )
    }

    @Test
    fun changePersonalStatus() {
        val expected = Unit

        assertAll(
            { assertEquals(expected, personalPage.changeStatus("INACTIVE")) },
            { assertEquals(expected, println(personalPage.status)) },
            { assertEquals(expected, personalPage.changeStatus("REMOVED")) },
            { assertEquals(expected, println(personalPage.status)) },
            { assertEquals(expected, personalPage.changeStatus("ACTIVE")) },
            { assertEquals(expected, println(personalPage.status)) },
        )
    }

    // Testing change command catalog

    @Test
    fun displayChangeCatalog() {
        val expected = Unit
        assertEquals(expected, personalPage.printChangeCatalog())
    }

    @Test
    fun changeEmailWithChangeCatalog() {
        val successReplacementEmail = "rabbit@gmail.com"

        val expected = Unit
        val emptyEmailException = assertThrows(InputDataException::class.java) {
            personalPage.executeChangeCommand(1, "")
        }

        assertAll(
            { assertEquals(expected, personalPage.executeChangeCommand(1, successReplacementEmail)) },
            { assertEquals("New data must not be blank", emptyEmailException.message) },
        )
    }

    @Test
    fun changeNickNameWithChangeCatalog() {
        val successReplacementNickName = "rabbit"

        val expected = Unit
        val emptyNickNameException = assertThrows(InputDataException::class.java) {
            personalPage.executeChangeCommand(2, "")
        }

        assertAll(
            { assertEquals(expected, personalPage.executeChangeCommand(2, successReplacementNickName)) },
            { assertEquals("New data must not be blank", emptyNickNameException.message) },
        )
    }

    @Test
    fun changeStatusWithChangeCatalog() {
        val replacementStatusActive = "ACTIVE"
        val replacementStatusInActive = "INACTIVE"
        val replacementStatusRemoved = "REMOVED"

        val invalidReplacementStatus = "SUSPENDED"

        val expected = Unit

        val emptyStatusException = assertThrows(IllegalArgumentException::class.java) {
            personalPage.executeChangeCommand(4, "")
        }

        val invalidStatusException = assertThrows(IllegalArgumentException::class.java) {
            personalPage.executeChangeCommand(4, invalidReplacementStatus)
        }

        assertAll(
            { assertEquals(expected, personalPage.executeChangeCommand(4, replacementStatusInActive)) },
            { assertEquals(expected, println(personalPage.currentUser.status)) },
            { assertEquals(expected, personalPage.executeChangeCommand(4, replacementStatusRemoved)) },
            { assertEquals(expected, println(personalPage.currentUser.status)) },
            { assertEquals(expected, personalPage.executeChangeCommand(4, replacementStatusActive)) },
            { assertEquals(expected, println(personalPage.currentUser.status)) },
            { assertEquals("No enum constant org.example.Status.", emptyStatusException.message) },
            { assertEquals("No enum constant org.example.Status.SUSPENDED", invalidStatusException.message) },
        )
    }

    @Test
    fun getInvalidChangeCommandMessage() {
        val replacement = "rabbit@gmail"
        val expected = Unit

        assertEquals(expected, personalPage.executeChangeCommand(5, replacement))
    }

    //AdminPage

    private val admin = Admin("frog@mail.com", "frog", "kE[H^gTf9")
    private val adminPage = AdminPage(userBase)

    @Test
    fun displayEntireAdminPage() {
        assertAll(
            { assertEquals(Unit, adminPage.openPage()) },
            { assertEquals(Unit, adminPage.printContent()) },
            { assertEquals(Unit, adminPage.printWholePage()) },
        )
    }

    @Test
    fun testAddNewUser() {
        val newUserData = "rabbit@gmail.com, rabbit, kEn8djb^Jbcf9"
        val newUser = userBase.convertToNewUserByRole(newUserData)

        assertAll(
            { assertEquals(newUser, adminPage.addNewUser(newUserData)) },
            { assertTrue(userBase.contains(newUser.email)) }
        )

    }


}