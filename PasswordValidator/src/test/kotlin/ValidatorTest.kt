import version_with_interface.UserDataException
import version_with_interface.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ValidatorTest {
    private val validator = Validator()

    private val failPassword = "mustang"
    private val successPassword = "Metg7gsD7^[2febn22S"

    @Test
    fun testEmptyPassword() {
        val exception = assertThrows(UserDataException::class.java) {
            validator.password = ""
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
    fun checkRulesSuccess() {
        validator.password = successPassword
        val expected = Unit
        assertAll(
            { assertEquals(expected, validator.checkRule(0)) },
            { assertEquals(expected, validator.checkRule(1)) },
            { assertEquals(expected, validator.checkRule(2)) },
            { assertEquals(expected, validator.checkRule(3)) },
            { assertEquals(expected, validator.checkRule(4)) }
        )
    }

    @Test
    fun checkRulesFailMessages() {
        validator.password = failPassword
        val expected = Unit

        assertAll(
            { assertEquals(expected, validator.checkRule(0)) },
            { assertEquals(expected, validator.checkRule(1)) },
            { assertEquals(expected, validator.checkRule(2)) },
            { assertEquals(expected, validator.checkRule(3)) },
            { assertEquals(expected, validator.checkRule(4)) }
        )
    }

}