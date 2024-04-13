import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RulesPackTest {

    private val rulesPackFail = RulesPack("mustang")
    private val rulesPackSuccess = RulesPack("Metg7gsD7^[2febn22S")

    @Test
    fun testCheckLength() {
        assertAll(
            { assertFalse(rulesPackFail.getCheckResult(0)) },
            { assertTrue(rulesPackSuccess.getCheckResult(0)) },
        )
    }

    @Test
    fun testCheckCase() {
        assertAll(
            { assertFalse(rulesPackFail.getCheckResult(1)) },
            { assertTrue(rulesPackSuccess.getCheckResult(1)) },
        )
    }

    @Test
    fun testCheckSpecialSymbols() {
        assertAll(
            { assertFalse(rulesPackFail.getCheckResult(2)) },
            { assertTrue(rulesPackSuccess.getCheckResult(2)) },
        )
    }

    @Test
    fun testCheckDictWord() {
        assertAll(
            { assertFalse(rulesPackFail.getCheckResult(3)) },
            { assertTrue(rulesPackSuccess.getCheckResult(3)) },
        )
    }

    @Test
    fun testCheckEntropy() {
        assertAll(
            { assertFalse(rulesPackFail.getCheckResult(4)) },
            { assertTrue(rulesPackSuccess.getCheckResult(4)) },
        )
    }

    @Test
    fun testWrongRuleNumber() {
        assertFalse(rulesPackFail.getCheckResult(5))
    }

    @Test
    fun testAllRulesFail() {
        assertAll(
            { assertFalse(rulesPackFail.getCheckResult(0)) },
            { assertFalse(rulesPackFail.getCheckResult(1)) },
            { assertFalse(rulesPackFail.getCheckResult(2)) },
            { assertFalse(rulesPackFail.getCheckResult(3)) },
            { assertFalse(rulesPackFail.getCheckResult(4)) }
        )
    }

    @Test
    fun testAllRulesSuccess() {
        assertAll(
            { assertTrue(rulesPackSuccess.getCheckResult(0)) },
            { assertTrue(rulesPackSuccess.getCheckResult(1)) },
            { assertTrue(rulesPackSuccess.getCheckResult(2)) },
            { assertTrue(rulesPackSuccess.getCheckResult(3)) },
            { assertTrue(rulesPackSuccess.getCheckResult(4)) }
        )
    }

}