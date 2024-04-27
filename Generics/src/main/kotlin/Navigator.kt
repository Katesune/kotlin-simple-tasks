import org.example.Admin
import org.example.Moderator
import org.example.User
import java.security.spec.EllipticCurve
import kotlin.reflect.KFunction1

val news = mutableListOf(
    News("Two singers have released a new fit", "A lot of people liked the new song", "singer,fit,release"),
    News("The city needs volunteers", "Volunteers will help toads cross the road", "volunteer,road,toads"),
    News("The theater will show a reinterpretation of Hamlet", "The reinterpretation will be very different from the original", "theater,Hamlet")
)

val comments = mutableListOf(
    Comment("naco4@gmail.com", "Add more colors", 25),
    Comment("dopemm4@gmail.com", "Add reactions to comments", 23),
    Comment("sagreo-6749@gmail.com", "Publish more news about animals", 17),
    Comment("dellaye952@gmail.com", "Change the font on the main page", 9),
)

class Navigator <out T: User> (
    currentUser: T,
    userBase: UserBase
){
    class SwitchCommand(
        val description: String,
        val page: Page<out Any>) {}

    private val basicSwitchCommands : () -> Map <Int, SwitchCommand> = {
        val newsPage = NewsPage("Latest news", news)
        val personalPage = PersonalPage<T, Any>(currentUser, userBase)

        mapOf(
            1 to SwitchCommand("Switch to News Feed", newsPage),
            2 to SwitchCommand("Switch to Personal Account", personalPage),
        )
    }

    private val moderatorSwitchCommand: () -> SwitchCommand = {
        val commentsPage = CommentsPage("Latest comments", comments)

        SwitchCommand("Switch to Comments Page", commentsPage)
    }

    private val adminSwitchCommand: (initialNumber: Int) -> Map <Int, SwitchCommand> = { initialNumber ->
        val commentsPage = CommentsPage("Latest comments", comments)
        val adminPage = AdminPage(currentUser as Admin, userBase)

        mapOf(
            initialNumber to SwitchCommand("Switch to Comments Page", commentsPage),
            initialNumber + 1 to SwitchCommand("Switch to the Admin Page", adminPage)
        )
    }

    val availableSwitchCommands: () -> Map <Int, SwitchCommand> = {
        val basicSwitchCommands = basicSwitchCommands()
        val additionalMapNumber = basicSwitchCommands.keys.max() + 1

        when (currentUser) {
            is Admin -> basicSwitchCommands + adminSwitchCommand(additionalMapNumber)
            is Moderator -> basicSwitchCommands + mapOf(
                additionalMapNumber to moderatorSwitchCommand()
            )
            else -> basicSwitchCommands
        }
    }

//    val availablePagesMap: () -> Map<Int, Page<out Any>> = {
//        when (currentUser) {
//            is Admin -> mapOf(
//                1 to NewsPage("Latest news", news),
//                2 to PersonalPage<T, Any>(currentUser, userBase),
//                3 to CommentsPage("Latest comments", comments),
//                4 to AdminPage(currentUser as Admin, userBase),
//            )
//            is Moderator -> mapOf(
//                1 to NewsPage("Latest news", news),
//                2 to PersonalPage<T, Any>(currentUser, userBase),
//                3 to CommentsPage("Latest comments", comments),
//            )
//            else -> mapOf(
//                1 to NewsPage("Latest news", news),
//                2 to PersonalPage<T, Any>(currentUser, userBase),
//            )
//        }
//    }
//
//    private val switchPagesMenu: () -> Map<Int, String> = {
//        val basicUserCommandNames = mapOf(
//            0 to "Exit",
//            1 to "Switch to News Feed",
//            2 to "Switch to Personal Account",
//        )
//
//        val commentsCommandNames = 3 to "Switch to Comments Page"
//
//        val adminCommandNames = mapOf(
//            4 to "Switch to the Admin Page", // добавить доп команды для страницы тут, либо в самой админке и личном кабинете
//        )
//
//        when (currentUser) {
//            is Admin -> basicUserCommandNames + commentsCommandNames + adminCommandNames
//            is Moderator -> basicUserCommandNames + commentsCommandNames
//            else -> basicUserCommandNames
//        }
//    }

    private val optionalCommandsMenu: (currentPage: Page<out Any>) -> Map<Int, String> = {currentPage ->
        val initialNum = availableSwitchCommands().keys.max() + 1

        when (currentPage) {
            is AdminPage -> mapOf(
                initialNum to "Edit users data"
            )
            is PersonalPage<*, *> -> mapOf(
                initialNum to "Edit user data"
            )
            else -> mapOf()
        }
    }

    fun switchCommandsKeys(): Set<Int> {
        return availableSwitchCommands().keys
    }

    fun optionalCommandsKeys(currentPage: Page<out Any>): Set<Int> {
        return optionalCommandsMenu(currentPage).keys
    }

    fun printCurrentPagesMenu(currentPage: Page<out Any>) {
        println("Enter the number of the command you want to run: ")

        println("0 - Exit")

        for ((commandNumber, command) in availableSwitchCommands()) {
            println("$commandNumber - ${command.description}")
        }

        for ((commandNumber, description) in optionalCommandsMenu(currentPage)) {
            println("$commandNumber - $description")
        }
    }

    fun getPageByCommand(commandNumber: Int): Page<out Any> {
        return availableSwitchCommands()[commandNumber]?.page ?: throw pagesException("There is no page with this number")
    }

    fun getPersonalPage(): PersonalPage<User, UserBase> {
        return getPageByCommand(2) as PersonalPage<User, UserBase>
    }
    fun getPersonalPageFromAdminPage(editUserEmail: String): PersonalPage<User, UserBase> {
        val currentAdminPage = getPageByCommand(4) as AdminPage
        return currentAdminPage.getEditUserPage(editUserEmail)
    }
}

class pagesException(val messsage: String): IllegalStateException(messsage)

