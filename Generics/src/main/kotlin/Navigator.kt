import org.example.Admin
import org.example.Moderator
import org.example.User
import kotlin.math.E
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1

val news = mutableListOf(
    News("Two singers have released a new fit", "A lot of people liked the new song", "singer,fit,release"),
    News("The city needs volunteers", "Volunteers will help toads cross the road", "volunteer,road,toads"),
    News("The theater will show a reinterpretation of Hamlet", "The reinterpretation will be very different from the original", "theater,Hamlet"),
)

val comments = mutableListOf(
    Comment("naco4@gmail.com", "Add more colors", 25),
    Comment("dopemm4@gmail.com", "Add reactions to comments", 23),
    Comment("sagreo-6749@gmail.com", "Publish more news about animals", 17),
    Comment("dellaye952@gmail.com", "Change the font on the main page", 9),
)

class AvailableToSwitchPages<out T: User> (
    private val currentUser: T,
    private val userBase: UserBase
){
    class SwitchCommand(
        val description: String,
        val page: Page<out Any>
    )

    private var additionalPages = mapOf<Int, SwitchCommand> ()

    fun getSwitchCommandsByUserRole(): Map <Int, SwitchCommand> {
        val basicSwitchCommands = getBasicSwitchCommands()
        setSwitchCommandsByUserRole(basicSwitchCommands.keys.max() + 1)
        return basicSwitchCommands + additionalPages
    }

    private fun getBasicSwitchCommands(): Map <Int, SwitchCommand> {
        val newsPage = NewsPage("Latest news", news)
        val personalPage = PersonalPage<T, Any>(currentUser, userBase)

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

    private fun setOptionalModeratorSwitchCommands(initialNumber: Int) {
        val commentsPage = CommentsPage("Latest comments", comments)

        additionalPages = mapOf(
            initialNumber to SwitchCommand("Switch to Comments Page", commentsPage)
        )
    }

    private fun setOptionalAdminSwitchCommands(initialNumber: Int) {
        val commentsPage = CommentsPage("Latest comments", comments)
        val adminPage = AdminPage(currentUser as Admin, userBase)

        additionalPages = mapOf(
            initialNumber to SwitchCommand("Switch to Comments Page", commentsPage),
            initialNumber + 1 to SwitchCommand("Switch to the Admin Page", adminPage)
        )
    }
}

class Navigator <out T: User> (
    availablePages: AvailableToSwitchPages<T>
) {
    private val availableSwitchCommands = availablePages.getSwitchCommandsByUserRole()

    fun printCurrentPagesMenu(currentPage: Page<out Any>) {
        println("Enter the number of the command you want to run: ")

        println("0 - Exit")

        for ((commandNumber, command) in availableSwitchCommands) {
            println("$commandNumber - ${command.description}")
        }

        for ((commandNumber, command) in editDataCommandsMenu(currentPage)) {
            println("$commandNumber - ${command.description}")
        }
    }

    fun getPageByCommand(commandNumber: Int): Page<out Any> {
        return availableSwitchCommands[commandNumber]?.page ?: throw PagesException("There is no page with this number")
    }

//    private val optionalCommandsMenu: (currentPage: Page<out Any>) -> Map<Int, String> = { currentPage ->
//        val initialNum = availableSwitchCommands.keys.max() + 1
//
//        when (currentPage) {
//            is AdminPage -> mapOf(
//                initialNum to "Edit users data"
//            )
//            is PersonalPage<*, *> -> mapOf(
//                initialNum to "Edit personal data"
//            )
//            else -> mapOf()
//        }
//    }

    class EditCommand(
        val description: String,
        val getEditPage: KFunction1<Page<out Any>, PersonalPage<User, UserBase>>
    ) {
        fun executeCommand(currentPage: Page<out Any>): PersonalPage<User, UserBase>  {
            return getEditPage(currentPage)
        }
    }

    val editDataCommandsMenu: (currentPage: Page<out Any>) -> Map<Int, EditCommand> = { currentPage ->
        val initialNum = availableSwitchCommands.keys.max() + 1
        // Можно же просто возвращать страницу?

        when (currentPage) {
            is AdminPage -> mapOf(
                initialNum to EditCommand("Edit users data", ::getEditPageFromAdminPage),
                initialNum + 1 to EditCommand("Add new user", ::getNewUserPersonalPage),
            )
            is PersonalPage<*, *> -> mapOf(
                initialNum to EditCommand("Edit personal data", ::getPersonalPage)
            )
            else -> mapOf()
        }
    }

    private fun getEditPageFromAdminPage(currentPage: Page<out Any>): PersonalPage<User, UserBase> {
        val editUserEmail = getEditUserEmail()
        val currentAdminPage = currentPage as AdminPage

        return currentAdminPage.getEditUserPage(editUserEmail)
    }

    private fun getEditUserEmail(): String{
        println("Please enter the address of the user whose data you want to edit")
        return readlnOrNull() ?: throw IllegalStateException("Invalid email adress")
    }

    private fun getNewUserPersonalPage(currentPage: Page<out Any>): PersonalPage<User, UserBase> {
        val newUserInputData = getNewUserInputData()
        val currentAdminPage = currentPage as AdminPage

        val newUser = currentAdminPage.addNewUser(newUserInputData)
        return currentAdminPage.getEditUserPage(newUser.email)
    }

    private fun getNewUserInputData(): String {
        println("Please enter the new user's details separated by commas in the following order: \n" +
                "email, nick name, password, status, role\n" +
                "You can omit the status and role, then by default the status will be ACTIVE, and the role will be USER")
        return readlnOrNull() ?: throw IllegalStateException("The new user data must not be empty")
    }

    private fun getPersonalPage(currentPage: Page<out Any>): PersonalPage<User, UserBase> {
        return currentPage as? PersonalPage<User, UserBase>
            ?: throw IllegalStateException("Failed to return personal page")
    }

    fun switchCommandsKeys(): Set<Int> {
        return availableSwitchCommands.keys
    }

    fun optionalCommandsKeys(currentPage: Page<out Any>): Set<Int> {
        return editDataCommandsMenu(currentPage).keys
    }
}

class PagesException(val messsage: String): IllegalStateException(messsage)

