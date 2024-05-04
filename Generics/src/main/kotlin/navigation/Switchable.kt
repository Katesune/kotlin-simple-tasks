import org.example.Admin
import org.example.Moderator
import org.example.User

interface Switchable<out T: User> {
    val currentUser: T
    val userBase: UserBase

    class SwitchCommand(
        val description: String,
        val switchPage: Page<out Any>
    )

    var switchCommandsCatalog: Map<Int, SwitchCommand>

    fun printSwitchCatalog() {
        for ((commandNum, switchCommand) in switchCommandsCatalog) {
            println("$commandNum - ${switchCommand.description}")
        }
    }
    fun getSwitchCommandsByUserRole(): Map<Int, SwitchCommand> {
        switchCommandsCatalog = getBasicSwitchCommands()
        setSwitchCommandsByUserRole(switchCommandsCatalog.keys.max() + 1)
        return switchCommandsCatalog
    }

    private fun getBasicSwitchCommands(): Map <Int, SwitchCommand> {
        val newsPage = NewsPage("Latest news", news)
        val personalPage = PersonalPage<T>(currentUser, userBase)

        return mapOf(
            1 to SwitchCommand("Switch to News Feed", newsPage),
            2 to SwitchCommand("Switch to Personal Account", personalPage),
        )
    }

    private fun setSwitchCommandsByUserRole(nextKey: Int) {
        when (currentUser) {
            is Admin -> setOptionalAdminSwitchCommands(nextKey)
            is Moderator -> setOptionalModeratorSwitchCommands(nextKey)
        }
    }

    private fun setOptionalAdminSwitchCommands(initialNumber: Int) {
        val commentsPage = CommentsPage("Latest comments", comments)
        val adminPage = AdminPage(userBase)

        switchCommandsCatalog += mapOf(
            initialNumber to SwitchCommand("Switch to Comments Page", commentsPage),
            initialNumber + 1 to SwitchCommand("Switch to Admin Page", adminPage)
        )
    }

    private fun setOptionalModeratorSwitchCommands(initialNumber: Int) {
        val commentsPage = CommentsPage("Latest comments", comments)

        switchCommandsCatalog += mapOf(
            initialNumber to SwitchCommand("Switch to Comments Page", commentsPage)
        )
    }

    fun updateSwitchCatalog() {
        switchCommandsCatalog = getSwitchCommandsByUserRole()
    }
}