import org.example.Admin
import org.example.User
import kotlin.reflect.KFunction1

open class Page <T> () {
    open val title: String = "Simple Page"
    open val description: String = "A page"

    val resetColor = "\u001B[0m"

    open val contents: MutableList<T> = mutableListOf()

    open val rgbColors = mapOf (
        ColorNames.RESET to resetColor,
        ColorNames.WHITE to ";255;255;255m",
        ColorNames.GRAY to ";118;123;175m",
        ColorNames.BLUE to ";92;99;175m",
        ColorNames.PURPLE to ";108;65;244m",
        ColorNames.RED to ";247;56;56m",
        ColorNames.YELLOW to ";255;184;0m",
    )

    enum class ColorNames {
        RESET,
        WHITE,
        GRAY,
        BLUE,
        PURPLE,
        RED,
        YELLOW,
    }

    fun getTextColor(colorName: ColorNames): String {
        return "\u001b[38;2" + rgbColors[colorName]
    }

    fun getBgColor(colorName: ColorNames): String {
        return "\u001b[48;2" + rgbColors[colorName]
    }

    fun printWholePage() {
        openPage()
        printContent()
    }

    open fun openPage() {
        println(getBgColor(ColorNames.WHITE) + getTextColor(ColorNames.GRAY) + title + resetColor)
        println("$description\n")
    }

    open fun printContent() {
        for (content in contents) {
            println(content.toString())
        }
    }

    val printlnWithColors = { text: Map<String, String> ->
        val resetColor = "\u001B[0m"
        for (t in text) {
            println(t.key + t.value + resetColor)
        }
    }
}

class NewsPage(
    override val title: String = "News Page",
    override val contents: MutableList<News>

): Page<News>() {
    override val description = "News feed"

    override fun printContent() {

        for (content in contents) {

            val mapColor = mapOf(
                getBgColor(ColorNames.GRAY) + getTextColor(ColorNames.WHITE) to content.title,
                getTextColor(ColorNames.BLUE) to content.body,
                getTextColor(ColorNames.PURPLE) to content.readyHashtags
            )

            printlnWithColors(mapColor)
            println()

        }
    }
}

class News(
    val title: String,
    val body: String,
    hashtags: String
) {
    val readyHashtags = hashtags
         get() = field.split(",").joinToString { "#$it " }
}

class CommentsPage(
    override val title: String = "Comments Page",
    override val contents: MutableList<Comment>

): Page<Comment>() {
    override val description = "The page with comments suggestions for improving the service."

    override fun printContent() {

        for (content in contents) {
            val mapColor = mapOf(
                getBgColor(ColorNames.YELLOW) + getTextColor(ColorNames.GRAY) to content.author,
                content.body + " " to getTextColor(ColorNames.RED) + content.likes + " likes"
            )

            printlnWithColors(mapColor)
            println()
        }
    }
}

class Comment(
    val author: String,
    val body: String,
    val likes: Int
) {}

class PersonalPage<out T: User, U>(
    initialUser: T,
    override val userBase: UserBase,

): Page<String>(), UserBaseManipulative {
    override val title = "Personal page"
    override val description: String = "Personal Account"

    override val currentUser = userBase[initialUser]

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
        val command: KFunction1<String, Unit>
    ) {
        fun executeCommand(replacement: String) {
            command(replacement)
        }
    }

    val editCommands: () -> Map<Int, ChangeDataCommand> = {
        val email = currentUser.email
        mapOf(
            1 to ChangeDataCommand("Change $email email" , ::changeEmail),
            2 to ChangeDataCommand("Change $email nick name", ::changeNickName),
            3 to ChangeDataCommand("Change $email password", ::changePass),
            4 to ChangeDataCommand("Change $email status", ::changeStatus),
        )
    }

    fun printEditMenu() {
        for (command in editCommands()) {
            println(command.key.toString() + " - " + command.value.description)
        }
    }

    fun editUserData(commandNum: Int) {
        println("Please enter the new value")
        val replacement = readlnOrNull() ?: ""
        executeEditCommand(commandNum, replacement)
    }

    private fun executeEditCommand(commandNum: Int, replacement: String) {
       editCommands()[commandNum]?.executeCommand(replacement) ?: println("The command was not recognized")
    }
}

class AdminPage(
    initialUser: Admin,
    override val userBase: UserBase

): Page<User>(), UserBaseManipulative {
    override val title: String = "Admin Page"
    override val description = "The admin page allows users to view and edit information about other users who are using the resource."

    override val contents = userBase.getUsers().toMutableList()
    override val currentUser = userBase[initialUser]

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
        val newUser = userBase.convertToNewUser(newUserData)
        userBase += newUser
        return userBase[newUser]
    }

}


