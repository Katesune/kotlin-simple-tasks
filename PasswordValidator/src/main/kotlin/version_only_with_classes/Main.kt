package version_only_with_classes


fun main() {
    println("Добрый день")

    println("Введите пароль для проверки")
    val password = readlnOrNull() ?: ""

    val validator = Validator(password)
    validator.checkPasswordExists()

    validator.printRules()
    println("Введите номера правил, которые вы хотите проверить, через пробел")

    validator.prepareUserRules(readlnOrNull() ?: "")

    validator.checkRules()
}