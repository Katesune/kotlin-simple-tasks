import org.example.*
import kotlin.reflect.KFunction0

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

class Navigator <out T: User> (
    override val currentUser: T,
    override val userBase: UserBase

): Switchable<T>, Editable {
    override var switchCommandsCatalog = getSwitchCommandsByUserRole()

    override val nextNumCatalog = switchCommandsCatalog.keys.max() + 1
    override var currentPage = getPageByCommand(1)

    fun printExitCommand() {
        println("Please enter the command number")
        println("0 - Exit")
    }

    fun printCommandsCatalog() {
        printExitCommand()

        printSwitchCatalog()
        printEditCatalog()
    }

    fun getPageByCommand(commandNumber: Int): Page<out Any> {
        return switchCommandsCatalog[commandNumber]?.switchPage ?: throw PagesException("There is no page with this number")
    }

//    override fun getEditCommand(commandNum: Int): EditCommand {
//        return currentPage.getEditCommandsCatalog(nextNumCatalog)[commandNum] ?: throw IllegalStateException("No such command")
//    }

    fun switchCommandsKeys(): Set<Int> {
        return switchCommandsCatalog.keys
    }

    fun optionalCommandsKeys(): Set<Int> {
        return getEditCommandsCatalog().keys
    }
}

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

    private fun setOptionalAdminSwitchCommands(initialNumber: Int) {
        val commentsPage = CommentsPage("Latest comments", comments)
        val adminPage = AdminPage(userBase)

        switchCommandsCatalog += mapOf(
            initialNumber to SwitchCommand("Switch to Comments Page", commentsPage),
            initialNumber + 1 to SwitchCommand("Switch to the Admin Page", adminPage)
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

interface Editable {
    val userBase: UserBase
    val nextNumCatalog: Int
    var currentPage: Page<out Any>

    class EditCommand(
        val description: String,
        private val getEditPage: KFunction0<PersonalPage<User, UserBase>>
    ) {
        fun getCurrentEditPage(): PersonalPage<User, UserBase> {
            return getEditPage()
        }
        fun runChangingDataProcess(editPage: PersonalPage<User, UserBase>, commandNum: Int) {
            editPage.processChangeUser(commandNum)
            editPage.printWholePage()
        }
    }

    fun printEditCatalog() {
        for ((commandNumber, command) in getEditCommandsCatalog()) {
            println("$commandNumber - ${command.description}")
        }
    }

    fun getEditCommandsCatalog(): Map<Int, EditCommand> {
        return when (currentPage) {
            is AdminPage -> mapOf(
                nextNumCatalog to EditCommand("Edit users data", ::getEditPageByEmail),
                nextNumCatalog + 1 to EditCommand("Add new user", ::createNewUserPage),
                )
            is PersonalPage<*, *> -> mapOf(
                nextNumCatalog to EditCommand("Edit personal data", ::convertCurrentPageToPersonal)
            )
            else -> mapOf()
        }
    }

    fun getEditCommand(commandNum: Int): EditCommand {
        return getEditCommandsCatalog()[commandNum] ?: throw IllegalStateException("No such command")
    }

    private fun getEditPageByEmail(): PersonalPage<User, UserBase> {
        val editUserEmail = readEditUserEmail()
        val editUser = userBase.getUserByEmail(editUserEmail)

        return PersonalPage(editUser, userBase)
    }

    private fun readEditUserEmail(): String {
        println("Please enter the address of the user whose data you want to edit")
        return readlnOrNull() ?: throw IllegalStateException("Invalid email address")
    }

    private fun createNewUserPage(): PersonalPage<User, UserBase> {
        val newUserInputData = readNewUserInputData()
        val currentAdminPage = currentPage as AdminPage

        val newUser = currentAdminPage.addNewUser(newUserInputData)
        return PersonalPage(newUser, userBase)
    }

    private fun readNewUserInputData(): String {
        println("Please enter the new user's details separated by commas in the following order: \n" +
                "email, nick name, password, role, status\n" +
                "You can omit the status and role, then by default the role will be USER and the status will be ACTIVE.")
        return readlnOrNull() ?: throw IllegalStateException("The new user data must not be empty")
    }

    private fun convertCurrentPageToPersonal(): PersonalPage<User, UserBase> {
        return currentPage as? PersonalPage<User, UserBase>
            ?: throw IllegalStateException("Failed to return personal page")
    }
}

class PagesException(message: String): IllegalStateException(message)

