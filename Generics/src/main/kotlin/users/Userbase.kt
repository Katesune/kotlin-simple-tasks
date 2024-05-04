import org.example.*
import java.io.File

private val usersFromFile = File("data/users.txt")
    .readText()
    .split("\n")

class UserBase() {
    private var users: MutableSet<User> = usersFromFile.map { convertToNewUserByRole(it) }.toMutableSet()

    fun convertToNewUserByRole(newUserData: String): User {
        val userData = newUserData.split(",").map { it.trim() }

        val user = convertDataToUser(userData)

        return when (user.role) {
            User.Role.USER -> user
            User.Role.MODERATOR -> user.toModerator()
            else -> user.toAdmin()
        }

    }

    private fun convertDataToUser(userData: List<String>): User  {
        return when (userData.size) {
            in 0..2 -> throw InputDataException("There is not enough data to convert the user")
            3 -> User(
                email = userData[0],
                nickName = userData[1],
                password = userData[2])
            4 -> User(
                email = userData[0],
                nickName = userData[1],
                password = userData[2],
                status = Status.valueOf(userData[3]))
            5 -> User(
                email = userData[0],
                nickName = userData[1],
                password = userData[2],
                status = Status.valueOf(userData[3]),
                role = User.Role.valueOf(userData[4]))
            else -> throw throw InputDataException("The data to convert to a user is redundant")
        }
    }

    fun getUsers(): MutableSet<User> {
        return users
    }

    operator fun contains(email: String): Boolean {
        return users.find { it.equals(email) } != null
    }

    operator fun get(user: User): User {
        return userOrException(users.find { it.equals(user.email) })
    }

    private val userOrException: (user: User?) -> User = { user ->
        user ?: throw UserBaseEditException("The user was not found.")
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
}

class UserBaseEditException(message: String): IllegalStateException(message)

interface UserBaseManipulative: UserManipulative {
    val userBase: UserBase
    val currentUser: User

    override var email: String
        get() = currentUser.email
        set(value) {
            if (value.isEmpty()) throw InputDataException("New data must not be blank")
            userBase.getUserByEmail(email).email = value
            currentUser.email = value
        }

    override var nickName: String
        get() = currentUser.nickName
        set(value) {
            if (value.isEmpty()) throw InputDataException("New data must not be blank")
            userBase.getUserByEmail(email).nickName = value
            currentUser.nickName = value
        }

    override var password: String
        get() = currentUser.password
        set(value) {
            if (value.isEmpty()) throw InputDataException("New data must not be blank")
            userBase.getUserByEmail(email).password = value
            currentUser.password = value
        }

    override var status: Status
        get() = currentUser.status
        set(value) {
            userBase.getUserByEmail(email).status = value
            currentUser.status = value
        }

    override fun changeEmail(newEmail: String) {
        if (userBase.contains(newEmail)) println("A user with such an email already exists")
        else super.changeEmail(newEmail)
    }

    override fun changePass(newPassword: String) {
        if (verifyPass()) super.changePass(newPassword)
        else println("Invalid password")
    }
    fun verifyPass(): Boolean {
        println("Please enter the old password")
        val inputOldPass = readlnOrNull() ?: ""
        return super.verifyPass(inputOldPass)
    }
}

class InputDataException(message: String): IllegalStateException(message)