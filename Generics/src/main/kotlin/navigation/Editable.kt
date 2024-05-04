import org.example.User
import kotlin.reflect.KFunction0

interface Editable {
    val userBase: UserBase
    val nextNumCatalog: Int
    var currentPage: Page<out Any>

    class EditCommand(
        val description: String,
        private val getEditPage: KFunction0<PersonalPage<User>>
    ) {

        fun getCurrentEditPage(): PersonalPage<User> {
            return getEditPage()
        }
        fun runChangingDataProcess(editPage: PersonalPage<User>, commandNum: Int) {
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
            is PersonalPage<*> -> mapOf(
                nextNumCatalog to EditCommand("Edit personal data", ::convertCurrentPageToPersonal)
            )
            else -> mapOf()
        }
    }

    fun getEditCommand(commandNum: Int): EditCommand {
        return getEditCommandsCatalog()[commandNum] ?: throw IllegalStateException("No such command")
    }

    private fun getEditPageByEmail(): PersonalPage<User> {
        val editUserEmail = readEditUserEmail()
        val editUser = userBase.getUserByEmail(editUserEmail)

        return PersonalPage(editUser, userBase)
    }

    private fun readEditUserEmail(): String {
        println("Please enter the address of the user whose data you want to edit")
        return readlnOrNull() ?: throw IllegalStateException("Invalid email address")
    }

    private fun createNewUserPage(): PersonalPage<User> {
        val newUserInputData = readNewUserInputData()
        val currentAdminPage = currentPage as AdminPage

        val newUser = currentAdminPage.addNewUser(newUserInputData)
        return PersonalPage(newUser, userBase)
    }

    private fun readNewUserInputData(): String {
        println("Please enter the new user's details separated by commas in the following order: \n" +
                "email, nick name, password, status, role\n" +
                "You can omit the status and role, then by default the status will be ACTIVE and the role will be USER.")
        return readlnOrNull() ?: throw IllegalStateException("The new user data must not be empty")
    }

    private fun convertCurrentPageToPersonal(): PersonalPage<User> {
        return currentPage as? PersonalPage<User>
            ?: throw IllegalStateException("Failed to return personal page")
    }
}