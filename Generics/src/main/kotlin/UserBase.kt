import org.example.Status
import org.example.User
import org.example.UserManipulative
import java.io.File

private val usersFromFile = File("data/users.txt")
    .readText()
    .split("\n")
    .map {
        val data = it.trim().split(",")
        if (data.size < 5) throw InputDataException("It is not possible to convert a string to a user class, there is not enough data")
        User(data[0], data[1], data[2], Status.valueOf(data[3]), User.Role.valueOf(data[4])) }.toMutableSet()

//class MutableUsersSet<User> : MutableSet<User> by mutableSetOf() {
//    override fun contains(element: User): Boolean {
//        return this.find { it == element } != null
//    }
//}
operator fun MutableSet<User>.contains(element: User): Boolean = this.find { it == element } != null

class UserBase() {
    private var users: MutableSet<User> = usersFromFile

    operator fun contains(email: String): Boolean {
        return users.find { it.equals(email) } != null
    }

    private val userOrException: (user: User?) -> User = { user ->
        user ?: throw UserBaseEditException("The user was not found.")
    }

    operator fun get(user: User): User {
        return userOrException(users.find { it.equals(user.email) })
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

    fun getUsers(): MutableSet<User> {
        return users
    }
}

interface UserBaseManipulative: UserManipulative {
    val userBase: UserBase
    val currentUser: User

    override var email: String
        get() = currentUser.email
        set(value) {
            currentUser.email = value
        }

    override var nickName: String
        get() = currentUser.nickName
        set(value) {
            currentUser.nickName = value
        }

    override var password: String
        get() = currentUser.password
        set(value) {
            currentUser.password = value
        }

    override var status: Status
        get() = currentUser.status
        set(value) {
            currentUser.status = value
        }

    override fun changeEmail(newEmail: String) {
        if (userBase.contains(newEmail)) println("A user with such an email already exists")
        else super.changeEmail(newEmail)
    }

    override fun changePass(newPassword: String) {
        println("Please enter the old password")
        val inputOldPass = readlnOrNull() ?: ""
        if (verifyPass(inputOldPass)) super.changePass(newPassword)
        else println("Invalid password")
    }
}

class InputDataException(message: String): IllegalStateException(message)
class UserBaseEditException(message: String): IllegalStateException(message)