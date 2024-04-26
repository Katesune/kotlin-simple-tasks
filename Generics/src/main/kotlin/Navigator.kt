import org.example.Admin
import org.example.Moderator
import org.example.User

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

    val availablePagesMap: () -> Map<Int, Page<out Any>> = {
        when (currentUser) {
            is Admin -> mapOf(
                1 to NewsPage("Latest news", news),
                2 to PersonalPage<T, Any>(currentUser, userBase),
                3 to CommentsPage("Latest comments", comments),
                4 to AdminPage(currentUser as Admin, userBase),
            )
            is Moderator -> mapOf(
                1 to NewsPage("Latest news", news),
                2 to PersonalPage<T, Any>(currentUser, userBase),
                3 to CommentsPage("Latest comments", comments),
            )
            else -> mapOf(
                1 to NewsPage("Latest news", news),
                2 to PersonalPage<T, Any>(currentUser, userBase),
            )
        }
    }

    val currentPagesMenu: () -> Map<Int, String> = {
        val basicUserCommandNames = mutableMapOf(
            0 to "Exit",
            1 to "Switch to News Feed",
            2 to "Switch to Personal Account",
        )

        val commentsCommandNames = 3 to "Switch to Comments Page"

        val adminCommandNames = mapOf(
            4 to "Switch to the Admin Page", // добавить доп команды для страницы тут, либо в самой админке и личном кабинете
        )

        when (currentUser) {
            is Admin -> basicUserCommandNames + commentsCommandNames + adminCommandNames
            is Moderator -> basicUserCommandNames + commentsCommandNames
            else -> basicUserCommandNames
        }
    }

    fun printCurrentPagesMenu() {
        println("Enter the number corresponding to the page you want to switch: ")
        for ((commandNumber, command) in currentPagesMenu()) {
            println("$commandNumber - $command")
        }
    }

    fun getPageByCommand(commandNumber: Int): Page<out Any> {
        return availablePagesMap()[commandNumber] ?: throw pagesException("There is no page with this number")
    }

    fun getEditPersonalPage(editUserEmail: String): PersonalPage<User, UserBase> {
        val currentAdminPage = getPageByCommand(4) as AdminPage
        return currentAdminPage.getEditUserPage(editUserEmail)
    }
}

class pagesException(val messsage: String): IllegalStateException(messsage)

