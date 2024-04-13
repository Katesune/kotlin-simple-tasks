package version_only_with_classes

import version_with_interface.UserDataException

class Validator (
    val password: String,
){
    private var userRules = setOf<String>()
    private val rulesChecker: RulesChecker = RulesChecker(password)

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

    fun checkPasswordExists () {
        if (password.isEmpty()) {
            throw UserDataException("Для работы программы необходимо ввести пароль")
        }
    }

    fun prepareUserRules(userRulesData: String) {
        if (userRulesData == "") {
            throw UserDataException("Для работы программы необходимо минимум одно правило")
        } else {
            userRules = userRulesData.split(" ").toSet()
        }
    }

    private fun userRuleToNumber(userRule: String): Int {
        if (userRule.toIntOrNull() == null) {
            throw UserDataException("Обнаружено правило, которое не является числом, проверьте набор правил на наличие лишних символов")
        } else return userRule.toInt()
    }

    private fun getFailRuleMessage(ruleNumber: Int): String {
        return(mapMessages.getOrElse(ruleNumber) {
            "Такого правила не существует"
        })
    }

    fun checkRule(ruleNumber: Int): Boolean {
        return if (rulesChecker.checkRule(ruleNumber)) true
        else {
            println(getFailRuleMessage(ruleNumber))
            return false
        }
    }

    fun checkRules() {
        for (rule in userRules) {
            val ruleNumber = userRuleToNumber(rule)
            checkRule(ruleNumber)
        }
    }

}