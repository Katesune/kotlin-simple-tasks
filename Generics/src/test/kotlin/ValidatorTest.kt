import org.example.UserDataException
import org.example.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.swing.ViewportLayout
import kotlin.math.exp

internal class ValidatorTest {
    private val failPassword = "mustang"
    private val successPassword = "Metg7gsD7^[2febn22S"

    private val validator = Validator(failPassword)

    @Test
    fun getExceptionWithEmptyPassword() {
        val exception = assertThrows(UserDataException::class.java) {
            Validator("")
        }
        assertEquals("Для работы программы необходимо ввести пароль", exception.message)
    }

    @Test
    fun printAllRulesFailMessages() {
        val expected = Unit
        validator.password = failPassword

        assertAll(
            { assertEquals(expected, validator.checkRule(0)) },
            { assertEquals(expected, validator.checkRule(1)) },
            { assertEquals(expected, validator.checkRule(2)) },
            { assertEquals(expected, validator.checkRule(3)) },
            { assertEquals(expected, validator.checkRule(4)) },
        )
    }

    @Test
    fun getFailAllRulesResult() {
        assertFalse(validator.checkAllRules())
    }

    @Test
    fun getSuccessAllRulesResult() {
        validator.password = successPassword
        assertTrue(validator.checkAllRules())
    }
}
