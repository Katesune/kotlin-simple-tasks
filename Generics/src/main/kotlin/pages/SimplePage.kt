import org.example.User
import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction1

open class Page <T> () {
    open val title: String = "Simple Page"
    open val description: String = "A page"

    val resetColor = "\u001B[0m"

    open val contents: MutableList<T> = mutableListOf()

    open val rgbColors = mapOf (
        ColorNames.RESET to resetColor,
        ColorNames.WHITE to ";255;255;255m",
        ColorNames.GRAY to ";118;123;175m",
        ColorNames.BLUE to ";92;99;175m",
        ColorNames.PURPLE to ";108;65;244m",
        ColorNames.RED to ";247;56;56m",
        ColorNames.YELLOW to ";255;184;0m",
    )

    enum class ColorNames {
        RESET,
        WHITE,
        GRAY,
        BLUE,
        PURPLE,
        RED,
        YELLOW,
    }

    fun getTextColor(colorName: ColorNames): String {
        return "\u001b[38;2" + rgbColors[colorName]
    }

    fun getBgColor(colorName: ColorNames): String {
        return "\u001b[48;2" + rgbColors[colorName]
    }

    fun printWholePage() {
        openPage()
        printContent()
    }

    open fun openPage() {
        println(getBgColor(ColorNames.WHITE) + getTextColor(ColorNames.GRAY) + title + resetColor)
        println("$description\n")
    }

    open fun printContent() {
        for (content in contents) {
            println(content.toString())
        }
    }

    val printlnWithColors = { text: Map<String, String> ->
        val resetColor = "\u001B[0m"
        for (t in text) {
            println(t.key + t.value + resetColor)
        }
    }
}


class NewsPage(
    override val title: String = "News Page",
    override val contents: MutableList<News>

): Page<News>() {
    override val description = "News feed"

    override fun printContent() {

        for (content in contents) {

            val mapColor = mapOf(
                getBgColor(ColorNames.GRAY) + getTextColor(ColorNames.WHITE) to content.title,
                getTextColor(ColorNames.BLUE) to content.body,
                getTextColor(ColorNames.PURPLE) to content.readyHashtags
            )

            printlnWithColors(mapColor)
            println()

        }
    }
}

class CommentsPage(
    override val title: String = "Comments Page",
    override val contents: MutableList<Comment>

): Page<Comment>() {
    override val description = "The page with comments suggestions for improving the service."

    override fun printContent() {

        for (content in contents) {
            val mapColor = mapOf(
                getBgColor(ColorNames.YELLOW) + getTextColor(ColorNames.GRAY) to content.author,
                content.body + " " to getTextColor(ColorNames.RED) + content.likes + " likes"
            )

            printlnWithColors(mapColor)
            println()
        }
    }
}
