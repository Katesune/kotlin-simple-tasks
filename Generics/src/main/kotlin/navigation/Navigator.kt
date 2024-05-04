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
        return switchCommandsCatalog[commandNumber]?.switchPage ?: throw SearchPagesException("There is no page with this number")
    }

    fun switchCommandsKeys(): Set<Int> {
        return switchCommandsCatalog.keys
    }

    fun optionalCommandsKeys(): Set<Int> {
        return getEditCommandsCatalog().keys
    }
}

class SearchPagesException(message: String): IllegalStateException(message)

