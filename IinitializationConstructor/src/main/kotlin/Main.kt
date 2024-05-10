import PublicAccount
import Storage

val storage = Storage()

fun main() {
    val contentPlayer = ContentPlayer

    val people = storage.users + storage.communities

    for (person in people.shuffled()) {
        ContentPlayer.addUser(person)
        ContentPlayer.likeOrNot()
    }
}

object ContentPlayer {

    private lateinit var currentUser: PublicAccount

    fun addUser(user: PublicAccount) {
        currentUser = user
    }

    private val contents = storage.movies + storage.series

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
        currentUser.displayFavoriteContent()
    }

    val command: () -> String = {
        readlnOrNull() ?: throw IllegalStateException("The command should not be empty")
    }

}