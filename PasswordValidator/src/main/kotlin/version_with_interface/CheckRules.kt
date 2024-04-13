package version_with_interface

import java.io.File
import kotlin.math.abs
import kotlin.math.log2

interface CheckRules {
    val password: String
    val ruleLength: Int
        get() = 8

    val dict: Set<String>
        get() = File("src/main/resources/pswd-dict.txt")
            .readText()
            .split("\n")
            .toSet()

    fun getCheckResult(ruleNumber: Int): Boolean {
        return when (ruleNumber) {
            0 -> checkLength()
            1 -> checkCase()
            2 -> checkSpecialSymbols()
            3 -> checkDictWord()
            4 -> checkEntropy()
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

    private fun checkDictWord() : Boolean {
        val wordsInPass = password.split(" ")
        for (word in wordsInPass) {
            if (word in dict) return false
        }
        return true
    }

    private fun checkEntropy() : Boolean {
        val frequencyDict = password.associate { symbol ->
            symbol to password.count{ it == symbol }
        }

        val entropy = frequencyDict.values.sumOf {
            val valueDouble = it.toDouble() / frequencyDict.size.toDouble()
            valueDouble / log2(valueDouble)
        }

        return abs(entropy) > 0.4
    }
}