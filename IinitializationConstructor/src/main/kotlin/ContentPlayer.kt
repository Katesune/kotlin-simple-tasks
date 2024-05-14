import BadgeExchanger
val storage = Storage()

fun main() {
    val contentPlayer = ContentPlayer

    val people = storage.users + storage.communities

    for (person in people.shuffled()) {
        contentPlayer.addUser(person)
        contentPlayer.likeOrNot()
    }

    for (person in storage.communitiesContent.shuffled()) {
        contentPlayer.addUser(person)
        contentPlayer.exchangeOrNot()
    }
}

object ContentPlayer {

    private lateinit var currentUser: PublicAccount

    fun addUser(user: PublicAccount) {
        currentUser = user
        print("Current user - ")
        currentUser.displayMainInformation()
    }

    private val contents = storage.movies + storage.communitiesContent + storage.series

    fun likeOrNot() {
        println("like or skip?")

        for (content in contents) {
            content.displayMainInformation()

            val command = command()

            content.configureReview(command)

            when (command) {
                "like" -> currentUser.likeIt(content)
                "skip" -> println("-------> next \n")
                else -> throw IllegalStateException("No such command")
            }

            content.displayLikes()
        }
        currentUser.displayMainInformation()
        currentUser.displayLikedContent()

    }

    fun exchangeOrNot() {
        val currentUserBadge = currentUser.badgeCollection.badge
        currentUserBadge.printBadgeName()
        if (currentUserBadge is Exchanged) {
            println("exchange or skip?")
            when (command()) {
                "exchange" -> {
                    println("You have ${currentUser.likesCount} likes now")

                    val receivedLikes = exchangeBadge(currentUserBadge)
                    currentUser.likesCount += receivedLikes

                    println("Your badge has been successfully exchanged for likes")
                }
                "skip" -> println("-------> next \n")
                else -> throw IllegalStateException("No such command")
            }

            currentUser.displayMainInformation()
            currentUser.displayLikes()

        } else println("Your badge cannot be exchanged")

    }

    private fun <T> exchangeBadge(badge: T): Int where T: Badge, T: Exchanged {
        val badgeExchanger = BadgeExchanger<T>()
        badge.printValue()

        println("Please, confirm the operation. If you agree with the exchange, please enter \"continue\". " +
                "If you change your mind, please enter \"skip\".")

        return when (command()) {
            "continue" -> badgeExchanger.exchange(badge)
            "skip" -> 0
            else -> throw IllegalStateException("No such command")
        }
    }

    val command: () -> String = {
        readlnOrNull() ?: throw IllegalStateException("The command should not be empty")
    }

}