package org.example

enum class Status {
    ACTIVE,
    INACTIVE,
    REMOVED
}
open class User(
    override var email: String,
    override var nickName: String,
    override var password: String,
    override var status: Status = Status.ACTIVE,
): UserManipulative {
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
//
//    fun changeEmail(newEmail: String) {
//        email = newEmail
//    }
//
//    fun changeNickName(newNickName: String) {
//        nickName = newNickName
//    }
//
//    fun verifyPass(inputPassword: String): Boolean {
//        return inputPassword == password
//    }
//
//    fun changePass(newPassword: String) {
//        if (validatePassResult(newPassword)) password = newPassword
//        else println("Password does not comply with the rules")
//    }
//
//    fun changeStatusToActive() {
//        status = Status.ACTIVE
//    }
//
//    fun changeStatusToInActive() {
//        status = Status.INACTIVE
//    }
//
//    fun changeStatusToRemoved() {
//        status = Status.REMOVED
//    }

    // вынести все функции из юзера в интерфейс
    // сделать так, чтобы параметры пользователя определялись сами
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

    fun verifyPass(inputPassword: String): Boolean {
        return inputPassword == password
    }

    fun changePass(newPassword: String) {
        if (validatePassResult(newPassword)) password = newPassword
        else println("Password does not comply with the rules")
    }

    fun changeStatusToActive() {
        status = Status.ACTIVE
    }

    fun changeStatusToInActive() {
        status = Status.INACTIVE
    }

    fun changeStatusToRemoved() {
        status = Status.REMOVED
    }
}

class Admin(email: String, nickName: String, password: String) :
    User(email, nickName, password) {
}

class Moderator(email: String, nickName: String, password: String) :
    User(email, nickName, password) {
}
