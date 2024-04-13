import version_with_interface.RulesPack
import version_with_interface.UserDataException
import version_with_interface.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ValidatorTest {
    private val validator = Validator()

    @Test
    fun testEmptyPassword() {
        val exception = assertThrows(UserDataException::class.java) {
            validator.checkPasswordExists("")
        }
        assertEquals("Для работы программы необходимо ввести пароль", exception.message)
    }

    @Test
    fun testEmptyUserRules() {
        val exception = assertThrows(UserDataException::class.java) {
            validator.checkUserRulesExists("")
        }
        assertEquals("Для работы программы необходимо минимум одно правило", exception.message)
    }

    @Test
    fun testWrongRule() {
        val exception = assertThrows(UserDataException::class.java) {
            validator.userRuleToNumber("HH")
        }
        assertEquals("Обнаружено правило, которое не является числом, проверьте набор правил на наличие лишних символов", exception.message)
    }

    @Test
    fun testPrintRules() {
        val expected = Unit
        assertEquals(expected, validator.printRules())
    }

    @Test
    fun testWrongRuleNumber() {
        val expected = false
        assertEquals(expected, validator.getCheckResult(5))
    }

    @Test
    fun checkRulesFailMessages() {
        val rulesFailPack = RulesPack("mustang")
        val expected = Unit

        assertAll(
            { assertEquals(expected, rulesFailPack.checkRule( validator, 0)) },
            { assertEquals(expected, rulesFailPack.checkRule(validator, 1)) },
            { assertEquals(expected, rulesFailPack.checkRule(validator, 2)) },
            { assertEquals(expected, rulesFailPack.checkRule(validator, 3)) },
            { assertEquals(expected, rulesFailPack.checkRule(validator, 4)) }
        )
    }

}