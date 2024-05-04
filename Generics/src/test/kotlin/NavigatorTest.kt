
import org.example.Admin
import org.example.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
internal class NavigatorTest {

    private val userBase = UserBase()

    private val admin = userBase.getUserByEmail("frog@mail.com").toAdmin()
    private val moderator = userBase.getUserByEmail("cat@gmail.com").toModerator()
    private val basicUser = userBase.getUserByEmail("lizard@gmail.com")

    private val adminNavigator = Navigator(admin, userBase)
    private val moderatorNavigator = Navigator(moderator, userBase)
    private val basicNavigator = Navigator(basicUser, userBase)

    @Test
    fun getPagesByCommandForAdmin() {

        val getPagesException = assertThrows(SearchPagesException::class.java) {
            adminNavigator.getPageByCommand(5)
        }

        assertAll(
            { assertTrue(adminNavigator.getPageByCommand(1) is NewsPage) },
            { assertTrue(adminNavigator.getPageByCommand(2) is PersonalPage<*>) },
            { assertTrue(adminNavigator.getPageByCommand(3) is CommentsPage) },
            { assertTrue(adminNavigator.getPageByCommand(4) is AdminPage) },
            { assertEquals("There is no page with this number", getPagesException.message) }
        )
    }

    @Test
    fun getPagesByCommandForModerator() {

        val getPagesException = assertThrows(SearchPagesException::class.java) {
            moderatorNavigator.getPageByCommand(4)
        }

        assertAll(
            { assertTrue(moderatorNavigator.getPageByCommand(1) is NewsPage) },
            { assertTrue(moderatorNavigator.getPageByCommand(2) is PersonalPage<*>) },
            { assertTrue(moderatorNavigator.getPageByCommand(3) is CommentsPage) },
            { assertEquals("There is no page with this number", getPagesException.message) }
        )
    }

    @Test
    fun getPagesByCommandForBasicUser() {

        val getPagesException = assertThrows(SearchPagesException::class.java) {
            basicNavigator.getPageByCommand(3)
        }

        assertAll(
            { assertTrue(basicNavigator.getPageByCommand(1) is NewsPage) },
            { assertTrue(basicNavigator.getPageByCommand(2) is PersonalPage<*>) },
            { assertEquals("There is no page with this number", getPagesException.message) }
        )
    }

    // interface Switchable
    // Testing the display of available commands for the pages.

    @Test
    fun printCommandCatalogForBasicPage() {
        val expected = Unit

        assertAll(
            { assertEquals(expected, println("ADMIN")) },
            { assertEquals(expected, adminNavigator.printCommandsCatalog()) },
            { assertEquals(expected, println("MODERATOR")) },
            { assertEquals(expected, moderatorNavigator.printCommandsCatalog()) },
            { assertEquals(expected, println("USER")) },
            { assertEquals(expected, basicNavigator.printCommandsCatalog()) },
        )
    }

    @Test
    fun printCommandCatalogForPersonalPage() {

        adminNavigator.currentPage = adminNavigator.getPageByCommand(2)
        moderatorNavigator.currentPage = moderatorNavigator.getPageByCommand(2)
        basicNavigator.currentPage = basicNavigator.getPageByCommand(2)

        val expected = Unit

        assertAll(
            { assertEquals(expected, println("ADMIN")) },
            { assertEquals(expected, adminNavigator.printCommandsCatalog()) },
            { assertEquals(expected, println("MODERATOR")) },
            { assertEquals(expected, moderatorNavigator.printCommandsCatalog()) },
            { assertEquals(expected, println("USER")) },
            { assertEquals(expected, basicNavigator.printCommandsCatalog()) },
        )
    }

    @Test
    fun printCommandCatalogForCommentsPage() {

        adminNavigator.currentPage = adminNavigator.getPageByCommand(3)
        moderatorNavigator.currentPage = moderatorNavigator.getPageByCommand(3)

        val expected = Unit

        val accessCommentsForUserException = assertThrows(SearchPagesException::class.java) {
            basicNavigator.getPageByCommand(3)
        }

        assertAll(
            { assertEquals(expected, println("ADMIN")) },
            { assertEquals(expected, adminNavigator.printCommandsCatalog()) },
            { assertEquals(expected, println("MODERATOR")) },
            { assertEquals(expected, moderatorNavigator.printCommandsCatalog()) },
            // User without access
            { assertEquals("There is no page with this number", accessCommentsForUserException.message) }
        )
    }
    @Test
    fun printCommandCatalogForAdminPage() {

        adminNavigator.currentPage = adminNavigator.getPageByCommand(4)

        val expected = Unit

        val accessAdminForUserException = assertThrows(SearchPagesException::class.java) {
            basicNavigator.getPageByCommand(4)
        }

        val accessAdminForModeratorException = assertThrows(SearchPagesException::class.java) {
            moderatorNavigator.getPageByCommand(4)
        }

        assertAll(
            { assertEquals(expected, println("ADMIN")) },
            { assertEquals(expected, adminNavigator.printCommandsCatalog()) },
            // MODERATOR without access
            { assertEquals("There is no page with this number", accessAdminForUserException.message) },
            // USER without access
            { assertEquals("There is no page with this number", accessAdminForModeratorException.message) },
        )
    }

    @Test
    fun changeAdminDataAndAdminPageTogether() {
        val personalPage = adminNavigator.getPageByCommand(2) as PersonalPage<User>
        personalPage.executeChangeCommand(1, "barbos@gmail.com")

        val changedUser = personalPage.currentUser
        adminNavigator.updateSwitchCatalog()

        assertEquals(changedUser, (adminNavigator.getPageByCommand(2) as PersonalPage<User>).currentUser)
    }

    // interface Editable
    @Test
    fun printEditCommandsForPersonalPage() {
        adminNavigator.currentPage = adminNavigator.getPageByCommand(2) as PersonalPage<User>
        assertEquals(Unit, adminNavigator.printEditCatalog())
    }

    @Test
    fun printEditCommandsForAdminPage() {
        adminNavigator.currentPage = adminNavigator.getPageByCommand(4) as AdminPage
        assertEquals(Unit, adminNavigator.printEditCatalog())
    }

    @Test
    fun invalidEditCommandPersonalPage() {
        adminNavigator.currentPage = adminNavigator.getPageByCommand(2) as PersonalPage<User>

        val editCommandExceptionBeforeEditable = assertThrows(IllegalStateException::class.java) {
            adminNavigator.getEditCommand(4)
        }

        val editCommandException = assertThrows(IllegalStateException::class.java) {
            adminNavigator.getEditCommand(6)
        }

        assertAll(
            { assertEquals("No such command", editCommandExceptionBeforeEditable.message) },
            { assertEquals("No such command", editCommandException.message) },
        )
    }

    @Test
    fun invalidEditCommandAdminPage() {
        adminNavigator.currentPage = adminNavigator.getPageByCommand(2) as PersonalPage<User>

        val editCommandExceptionBeforeEditable = assertThrows(IllegalStateException::class.java) {
            adminNavigator.getEditCommand(4)
        }

        val editCommandException = assertThrows(IllegalStateException::class.java) {
            adminNavigator.getEditCommand(7)
        }

        assertAll(
            { assertEquals("No such command", editCommandExceptionBeforeEditable.message) },
            { assertEquals("No such command", editCommandException.message) },
        )
    }

}