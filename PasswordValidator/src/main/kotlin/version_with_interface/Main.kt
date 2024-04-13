package version_with_interface

fun main() {
    println("Добрый день")

    println("Введите пароль для проверки")

    val password = readlnOrNull() ?: ""
    val validator = Validator()

    validator.password = password
    validator.printRules()

    println("Введите номера правил, которые вы хотите проверить, через пробел")

    val userRules = validator.checkUserRulesExists(readlnOrNull() ?: "")
    for (userRule in userRules) {
        val ruleNumber = validator.userRuleToNumber(userRule)
        validator.checkRule(ruleNumber)
    }

    validator.printFullCheckResult()
}


