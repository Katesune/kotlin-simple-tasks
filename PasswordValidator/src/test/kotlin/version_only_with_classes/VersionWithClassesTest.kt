package version_only_with_classes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import version_with_interface.UserDataException

class VersionWithClassesTest {
    private val failValidator = Validator("mustang")
    private val successValidator = Validator("Metg7gsD7^[2febn22S")

    @Test
    fun testEmptyPassword() {
        val validator = Validator("")
        val exception = assertThrows(UserDataException::class.java) {
            validator.checkPasswordExists()
        }
        assertEquals("Для работы программы необходимо ввести пароль", exception.message)
    }

    @Test
    fun testPreparingRules() {
        val exception = assertThrows(UserDataException::class.java) {
            failValidator.prepareUserRules("")
        }
        assertEquals("Для работы программы необходимо минимум одно правило", exception.message)
    }

    @Test
    fun invalidRuleNumber() {
        val exception = assertThrows(UserDataException::class.java) {
            failValidator.prepareUserRules("ei5kw6nf")
            failValidator.checkRules()
        }
        assertEquals("Обнаружено правило, которое не является числом, проверьте набор правил на наличие лишних символов", exception.message)
    }

    @Test
    fun testAllFailRules() {
        assertAll(
            { assertFalse(failValidator.checkRule(0)) },
            { assertFalse(failValidator.checkRule(1)) },
            { assertFalse(failValidator.checkRule(2)) },
            { assertFalse(failValidator.checkRule(3)) },
            { assertFalse(failValidator.checkRule(4)) },
            { assertFalse(failValidator.checkRule(5)) }
        )
    }

    @Test
    fun testAllSuccessRules() {
        assertAll(
            { assertFalse(successValidator.checkRule(0)) },
            { assertFalse(successValidator.checkRule(1)) },
            { assertFalse(successValidator.checkRule(2)) },
            { assertFalse(successValidator.checkRule(3)) },
            { assertFalse(successValidator.checkRule(4)) }
        )
    }

}