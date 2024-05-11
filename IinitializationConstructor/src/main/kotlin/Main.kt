import PublicAccount
import Storage

val storage = Storage()

fun main() {
    val contentPlayer = ContentPlayer

    val people = storage.users + storage.communities

    for (person in people.shuffled()) {
        contentPlayer.addUser(person)
        contentPlayer.likeOrNot()
    }
}

object ContentPlayer {

    private lateinit var currentUser: PublicAccount

    fun addUser(user: PublicAccount) {
        currentUser = user
        print("Current user - ")
        currentUser.displayMainInformation()
    }

    private val contents = storage.movies + storage.communitiesContent //+ storage.series

    fun likeOrNot() {
        println("like or skip?")

        for (content in contents) {
            content.displayMainInformation()
            when (command()) {
                "like" -> currentUser.likeIt(content)
                "skip" -> println("-------> next \n")
                else -> throw IllegalStateException("No such command")
            }
            content.displayLikes()
        }
        currentUser.displayMainInformation()
        currentUser.displayLikedContent()
    }

    val command: () -> String = {
        readlnOrNull() ?: throw IllegalStateException("The command should not be empty")
    }

}