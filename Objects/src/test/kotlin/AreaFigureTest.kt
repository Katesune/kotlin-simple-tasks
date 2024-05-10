import org.example.AreaFigure
import org.example.VideoClip
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.exp

class AreaFigureTest {
    private val areaFigure = AreaFigure(2, 5)
    private val smallerAreaFigure = AreaFigure(1, 3)
    private val biggerAreaFigure = AreaFigure(4, 10)

    @Test
    fun testToString() {
        assertEquals(Unit, println(areaFigure.toString()))
    }

    @Test
    fun joinAreaFigure() {
        val expected = AreaFigure(3, 8)

        assertEquals(expected, areaFigure + smallerAreaFigure)
    }

    @Test
    fun separateAreaFigure() {
        val expected = AreaFigure(1, 2)

        assertEquals(expected, areaFigure - smallerAreaFigure)
    }

    @Test
    fun attachAreaFigure() {
        areaFigure += smallerAreaFigure

        assertEquals(Unit, println("areaFigure += smallerAreaFigure = $areaFigure"))
    }

    @Test
    fun detachAreaFigure() {
        areaFigure -= smallerAreaFigure

        assertEquals(Unit, println("areaFigure -= smallerAreaFigure = $areaFigure"))
    }

    @Test
    fun doubleAreaFigure() {
        var toDoubleAreaFigure = AreaFigure(2, 5)
        toDoubleAreaFigure++
        assertEquals(biggerAreaFigure, toDoubleAreaFigure)
    }

    @Test
    fun halveAreaFigure() {
        var toDoubleAreaFigure = AreaFigure(4, 10)
        toDoubleAreaFigure--
        val expected = AreaFigure(0, 0)

        assertEquals(expected, toDoubleAreaFigure)
    }

    @Test
    fun multiplyAreaFigure() {
        val multiplyArea = AreaFigure(2, 2)

        assertEquals(biggerAreaFigure, areaFigure * multiplyArea)
    }

    @Test
    fun divideAreaFigure() {
        val expected = AreaFigure(2, 2)

        assertEquals(expected, biggerAreaFigure / areaFigure)
    }

    @Test
    fun increaseAreaToAnotherArea() {
        areaFigure *= AreaFigure(2, 2)
        assertEquals(biggerAreaFigure, areaFigure)
    }

    @Test
    fun decreaseAreaToAnotherArea() {
        biggerAreaFigure /= AreaFigure(2, 2)
        assertEquals(areaFigure, biggerAreaFigure)
    }

    @Test
    fun checkAreasEquality() {
        val areaFigureWithSimilarProps = AreaFigure(2, 5)
        val areaFigureWithDiffProps = AreaFigure(5, 2)

        assertAll(
            { assertTrue(areaFigure == areaFigureWithSimilarProps) },
            { assertTrue(areaFigure == areaFigureWithDiffProps) },
            { assertFalse(areaFigure.equals(VideoClip("v", "k"))) }
        )

    }

    @Test
    fun checkHashcode() {
        assertTrue((areaFigure.height * areaFigure.width).hashCode() == areaFigure.hashCode())
    }
    @Test
    fun otherAreaInAreaFigure() {
        val areaFigureWithSimilarProps = AreaFigure(2, 5)

        assertAll(
            { assertTrue(areaFigure.contains(areaFigureWithSimilarProps)) },
            { assertTrue(areaFigure.contains(smallerAreaFigure)) },
           { assertFalse(areaFigure.contains(biggerAreaFigure)) },
        )
    }


    @Test
    fun compareAreas() {
        val areaFigureWithSimilarProps = AreaFigure(2, 5)

        assertAll(
            { assertTrue(areaFigure.compareTo(areaFigureWithSimilarProps) == 0 ) },
            { assertTrue(areaFigure > smallerAreaFigure) },
            { assertTrue(areaFigure < biggerAreaFigure) },
        )
    }

    @Test
    fun getAreaFigureFromProp() {
        val height = 5
        val width = 2
        val expected = 10

        assertEquals(expected, areaFigure[height, width] )
    }

    @Test
    fun rangeAreas() {
        val startAreaFigure = AreaFigure(1, 1)
        val expectedList = listOf(
            AreaFigure(1, 1),
            AreaFigure(1, 2),
            AreaFigure(1, 3)
        )

        assertEquals(expectedList, startAreaFigure.rangeTo(smallerAreaFigure))
    }

}