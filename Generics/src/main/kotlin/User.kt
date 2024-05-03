package org.example

enum class Status {
    ACTIVE,
    INACTIVE,
    REMOVED,
}

open class User(
    override var email: String,
    override var nickName: String = "NickName",
    override var password: String,
    val role: Role = Role.USER,
    override var status: Status = Status.ACTIVE,

): UserManipulative {
    enum class Role {
        USER,
        MODERATOR,
        ADMIN,
    }

    private val validatePassResult : (passwordToCheck: String) -> Boolean = {pass ->
        val validator = Validator(pass)
        validator.checkAllRules()
    }

    init {
        require(email.isNotEmpty()) {"Email must not be empty"}
        require(nickName.isNotEmpty()) {"Nickname must not be empty"}
        require(password.isNotEmpty()) {"Password must not be empty"}
        require(validatePassResult(password)) {"Password does not comply with the rules"}
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is User -> (this.email == other.email)
            is String -> (this.email == other)
            else -> false
        }
    }

    fun getUserDataWithoutPass(): String {
        return "$email,$nickName,$status"
    }

    fun toAdmin(): Admin {
        return Admin(email, nickName, password)
    }

    fun toModerator(): Moderator {
        return Moderator(email, nickName, password)
    }
}

class Admin(email: String, nickName: String, password: String) :
    User(email, nickName, password) {
}

class Moderator(email: String, nickName: String, password: String) :
    User(email, nickName, password) {
}

interface UserManipulative {
    var email: String
    var nickName: String
    var password: String
    var status: Status

    private val validatePassResult : (passwordToCheck: String) -> Boolean
        get() = { pass ->
            val validator = Validator(pass)
            validator.checkAllRules()
        }

    fun changeEmail(newEmail: String) {
        email = newEmail
    }

    fun changeNickName(newNickName: String) {
        nickName = newNickName
    }

    fun changePass(newPassword: String) {
        if (validatePassResult(newPassword)) password = newPassword
        else println("Password does not comply with the rules")
    }

    fun verifyPass(inputPassword: String): Boolean {
        return inputPassword == password
    }

    fun changeStatus(newStatus: String) {
        status = Status.valueOf(newStatus)
    }
}
