package version_with_interface

class Validator(
) : CheckRules {
    override var password: String = ""
        set(value) {
            field = checkPasswordExists(value)
        }

    private var fullCheckResult = true

    private val mapMessages = mapOf(
        0 to "Слишком короткий пароль",
        1 to "Пароль должен содержать минимум по одной букве в верхнем и нижнем регистре",
        2 to "Пароль должен содержать минимум одну цифру и один специальный символ",
        3 to "Слишком простой пароль",
        4 to "Низкий уровень энтропии"
    )

    private val mapRules = mapOf(
        0 to "Проверить длину пароля",
        1 to "Проверить наличие символов в верхнем и нижнем регистре",
        2 to "Проверить наличие цифр и спец. символов",
        3 to "Проверить наличие словарных слов",
        4 to "Проверить уровень энтропии"
    )

    fun printRules() {
        val rulesTitle = "Набор правил, на которые вы можете проверить свой пароль:"
        println(rulesTitle)
        mapRules.forEach{ (number, ruleDescription) ->
            val ruleSell = ruleDescription +
                    ".".repeat(rulesTitle.length - number.toString().length - ruleDescription.length ) +
                    number

            println(ruleSell)
        }
        println("")
    }

    override fun getFailMessage(ruleNumber: Int): String {
        fullCheckResult = false
        return (mapMessages.getOrElse(ruleNumber) {
            "Такого правила не существует"
        })
    }

    private fun checkPasswordExists (pass: String): String {
        if (pass.isEmpty()) {
            throw UserDataException("Для работы программы необходимо ввести пароль")
        } else return pass
    }

    fun checkUserRulesExists(userRules: String): Set<String> {
        if (userRules == "") {
            throw UserDataException("Для работы программы необходимо минимум одно правило")
        } else return userRules.split(" ").toSet()
    }

    fun userRuleToNumber(userRule: String): Int {
        if (userRule.toIntOrNull() == null) {
            throw UserDataException("Обнаружено правило, которое не является числом, проверьте набор правил на наличие лишних символов")
        } else return userRule.toInt()
    }

    fun printFullCheckResult() {
        if (fullCheckResult) println("Проверка прошла успешно")
        else println("Проверка прошла неудачно, выполнены не все требования к паролю")
    }
}

class UserDataException(message: String) : IllegalStateException(message)