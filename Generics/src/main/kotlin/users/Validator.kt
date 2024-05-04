package org.example

class Validator(
    override var password: String)
    : CheckRules {

    private val fullRulesPack = setOf(0, 1, 2)
    private var checkResult = true

    private val mapMessages = mapOf(
        0 to "Слишком короткий пароль",
        1 to "Пароль должен содержать минимум по одной букве в верхнем и нижнем регистре",
        2 to "Пароль должен содержать минимум одну цифру и один специальный символ",
    )

    init {
        require(password.isNotEmpty()) {
            throw UserDataException("Для работы программы необходимо ввести пароль")
        }
    }

    override fun getFailMessage(ruleNumber: Int): String {
        checkResult = false
        return (mapMessages.getOrElse(ruleNumber) {
            "Такого правила не существует"
        })
    }

    fun checkAllRules(): Boolean {
        for (rule in fullRulesPack) {
            checkRule(rule)
        }
        return checkResult
    }

}

interface CheckRules {
    val password: String
        get() = ""

    val ruleLength: Int
        get() = 8

    fun getCheckResult(ruleNumber: Int): Boolean {
        return when (ruleNumber) {
            0 -> checkLength()
            1 -> checkCase()
            2 -> checkSpecialSymbols()
            else -> false
        }
    }

    fun getFailMessage(ruleNumber: Int): String

    fun checkRule(ruleNumber: Int)  {
        if (!getCheckResult(ruleNumber)) {
            println(getFailMessage(ruleNumber))
        }
    }

    private fun checkLength() : Boolean {
        return password.trim().length >= ruleLength
    }

    private fun checkCase() : Boolean {
        return password.count { it.isUpperCase() } > 0 && password.count { it.isLowerCase() } > 0
    }

    private fun checkSpecialSymbols() : Boolean {
        return password.count { it.isLetter() } > 0 && password.count { it in "[/.^&$#;]" } > 0
    }
}

class UserDataException(message: String) : IllegalStateException(message)