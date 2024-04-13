package version_with_interface

fun main() {
    println("Добрый день")

    println("Введите пароль для проверки")

    val validator = Validator()
    val password = readlnOrNull() ?: ""

    validator.checkPasswordExists(password)
    val rulesPack = RulesPack(password)

    validator.printRules()

    println("Введите номера правил, которые вы хотите проверить, через пробел")

    val userRules = validator.checkUserRulesExists(readlnOrNull() ?: "")

    for (userRule in userRules) {
        val ruleNumber = validator.userRuleToNumber(userRule)
        rulesPack.checkRule(validator, ruleNumber)
    }
}


