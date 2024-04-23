import org.example.User
import java.io.File
import kotlin.contracts.contract

private val usersFromFile = File("data/users.txt")
    .readText()
    .split("\n")
    .map {
        val data = it.trim().split(",")
        User(data[0], data[1], data[2]) }.toMutableSet()


class MutableUsersSet<User> : MutableSet<User> by mutableSetOf() {
    override fun contains(element: User): Boolean {
        return this.find { it == element } != null
    }
}

//operator fun MutableSet<User>.contains(element: User): Boolean = this.find { it == element } != null

class UserBase() {
    private var users: MutableUsersSet<User> = try {
        usersFromFile.toCollection(MutableUsersSet<User>())
    } catch (e: UserBaseEditException) { MutableUsersSet<User>() }

    operator fun contains(email: String): Boolean {
        return users.find { it.equals(email) } != null
    }

    private val userOrException: (user: User?) -> User = { user ->
        user ?: throw UserBaseEditException("The user was not found.")
    }

    operator fun get(user: User): User {
        return userOrException(users.find { it.equals(user.getEmail()) })
    }

    fun getUserByEmail(email: String): User {
        return userOrException(users.find { it.equals(email) })
    }

    operator fun plusAssign(newUser: User) {
        if (users.add(newUser)) println("The user has been added.")
        else println("A user with such an email already exists.")
    }

    operator fun minusAssign(userToRemove: User) {
        if (users.remove(userToRemove)) println("The user has been removed.")
        else throw UserBaseEditException("The user you are trying to remove does not exist.")
    }

    fun getUsers(): MutableUsersSet<User> {
        return users
    }
}

interface Manipulative {
    val currentUser: User

    fun changeCurrentEmail(newEmail: String) {
        currentUser.changeEmail(newEmail)
    }

    fun changeCurrentNickName(newNickName: String) {
        currentUser.changeNickName(newNickName)
    }

    fun changeCurrentPassword(oldPassword: String, newPassword: String) {
        if (currentUser.verifyPass(oldPassword)) {
            currentUser.changePass(newPassword)
        } else println("Incorrect password")
    }

    fun changeCurrentStatusToActive() {
        currentUser.changeStatusToActive()
    }

    fun changeCurrentStatusToInActive() {
        currentUser.changeStatusToInActive()
    }

    fun changeCurrentStatusToRemoved() {
        currentUser.changeStatusToRemoved()
    }
}


class UserBaseEditException(message: String): IllegalStateException(message)