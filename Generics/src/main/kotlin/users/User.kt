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
    override var status: Status = Status.ACTIVE,
    open val role: Role = Role.USER,

): UserManipulative {
    enum class Role {
        USER,
        MODERATOR,
        ADMIN,
    }

    constructor(
        email: String,
        nickName: String,
        password: String,
        status: Status,
    ) : this (
        email = email,
        nickName = nickName,
        password = password,
        status = status,
        role = Role.USER,
    )

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

    fun toModerator(): Moderator {
        return Moderator(email, nickName, password, status)
    }
    fun toAdmin(): Admin {
        return Admin(email, nickName, password, status)
    }
}
class Moderator(email: String, nickName: String, password: String, status: Status = Status.ACTIVE) :
    User(email, nickName, password, status) {
    override val role: Role = Role.MODERATOR

}


class Admin(email: String, nickName: String, password: String, status: Status = Status.ACTIVE) :
    User(email, nickName, password) {
    override val role: Role = Role.ADMIN
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
