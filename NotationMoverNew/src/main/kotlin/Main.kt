fun main() {

    val expression = readlnOrNull()

    if (expression != null) {
        val translator = Translator(expression)
        translator.processExpression()
    } else println("Expression not found")

}

private class Translator(
    var prefixExp: String
) {
    private val regexExpression = """[\s\d+*/-]+""".toRegex()
    private val regexOperators = """[+*/-]+""".toRegex()
    private val regexNum = """\d+""".toRegex()
    private val regexPair = """[+*/-],\d+,\d+""".toRegex()

    private val infixExp = mutableMapOf<String, String>()

    fun processExpression() {
        if (validateExpression() && checkItemsCount()) {
            modifyExp()
            translateExpression()
        }
    }

    private fun translateExpression() {
        collectPairs()

        if (checkExcessItems()) {
            collectRemains()
        }
    }

    private fun collectRemains() {
        for ((index, value) in prefixExp.withIndex()) {
            println("index = $index  value $value")
        }
    }

    private fun collectPairs() {
        val pairs = regexPair.findAll(prefixExp)

        for (pair in pairs) {
            val parts = pair.value.split(',')
            val part = "( ${parts[1]} ${parts[0]} ${parts[2]} )"

            val key = "#".repeat(infixExp.size + 1)
            infixExp[key] = part
            prefixExp = prefixExp.replaceFirst(pair.value, key)
        }

    }

    private fun checkExcessItems(): Boolean {

        val items = prefixExp.split(" ").reversed()
        val regexInvalidPair = """([\d]+)([\s]+)([-+/*])([\s]+)([\d]+)""".toRegex()

        if (regexInvalidPair.findAll(prefixExp).toList().isNotEmpty()) {
            println("In the expression, the part 'Number Sign Number' was found, which refers to prefix notation")
            return false
        }

        for (index in items.indices) {
            if (items[index].matches(regexOperators)) {
                println("Operators should not be at the end of an expression")
                return false
            }
            if (items[index].contains("#")) return true
        }

        return true
    }


    private fun validateExpression(): Boolean {
        if (regexExpression.matches(prefixExp)) {
            return true
        } else {
            println("The expression must contain only digits and operators '+ - / *'")
            return false
        }
    }

    private fun checkItemsCount(): Boolean {

        val countNum = regexNum.findAll(prefixExp).toList().size
        val countOperators = regexOperators.findAll(prefixExp).toList().size

        if (countNum - 1 > countOperators) {
            println("There are not enough operators in the expression")
            return false
        }

        if (countNum - 1 < countOperators) {
            println("There are excess operators in the expression")
            return false
        }

        return true
    }

    private fun modifyExp() {
        prefixExp = prefixExp.split(" ").toString()
        prefixExp = prefixExp.replace("[", "").replace("]", "").replace(" ", "")
    }

}