import org.example.User
import kotlin.reflect.KFunction1

class PersonalPage<out T: User>(
    override val currentUser: T,
    override val userBase: UserBase,

    ): Page<String>(), UserBaseManipulative {
    override val title = "Personal page"
    override val description: String = "Personal Account"

    override fun printContent() {
        val currentUserData = currentUser.getUserDataWithoutPass().split(",")

        val mapColor = mapOf(
            getTextColor(ColorNames.PURPLE) to currentUserData[0] + " " + resetColor + currentUserData[1],
            getBgColor(ColorNames.BLUE) to currentUserData[2]
        )

        printlnWithColors(mapColor)
        println()
    }
    class ChangeDataCommand(
        val description: String,
        val changeData: KFunction1<String, Unit>
    ) {
        fun executeCommand(replacement: String) {
            changeData(replacement)
        }
    }

    val changeCommandsCatalog: () -> Map<Int, ChangeDataCommand> = {
        val email = currentUser.email
        mapOf(
            1 to ChangeDataCommand("Change $email email" , ::changeEmail),
            2 to ChangeDataCommand("Change $email nick name", ::changeNickName),
            3 to ChangeDataCommand("Change $email password", ::changePass),
            4 to ChangeDataCommand("Change $email status", ::changeStatus),
        )
    }

    fun printChangeCatalog() {
        for (command in changeCommandsCatalog()) {
            println(command.key.toString() + " - " + command.value.description)
        }
    }

    fun processChangeUser(inputCommandNum: Int) {
        val replacement = getReplacement()
        executeChangeCommand(inputCommandNum, replacement)
    }

    private fun getReplacement(): String {
        println("Please enter the new value")
        return readlnOrNull() ?: throw IllegalStateException("New data must not be blank")
    }

    fun executeChangeCommand(inputCommandNum: Int, replacement: String) {
        changeCommandsCatalog()[inputCommandNum]?.executeCommand(replacement) ?: println("The command was not recognized")
    }
}

class AdminPage(
    private val userBase: UserBase

): Page<User>() {
    override val title: String = "Admin Page"
    override val description = "The admin page allows users to view and edit information about other users who are using the resource."

    override val contents = userBase.getUsers().toMutableList()

    override fun printContent() {
        for (content in contents) {
            val userData = content.getUserDataWithoutPass().split(",")

            val mapColor = mapOf(
                getTextColor(ColorNames.PURPLE) to userData[0] + " " + userData[1],
                getBgColor(ColorNames.BLUE) to userData[2]
            )

            printlnWithColors(mapColor)
            println()
        }
    }

    fun addNewUser(newUserData: String): User {
        val newUser = userBase.convertToNewUserByRole(newUserData)
        userBase += newUser
        return userBase[newUser]
    }
}