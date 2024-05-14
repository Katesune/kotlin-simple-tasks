import kotlin.random.Random

abstract class Badge {
    abstract val name: String

    fun printBadgeName() {
        println("You have a badge \"$name\"")
    }
}

interface Exchanged {
    val name: String
    val value: Int

    fun printValue() {
        println("You can exchange your badge $name for $value likes")
        println("If your badge is from a rare category, you will receive additional likes for it")
    }
}

class Beautiful(
    override val name: String,
) : Badge() {}

class Popular(
    override val name: String,
    override val value: Int,
) : Badge(), Exchanged {}

abstract class Smart() : Badge(), Exchanged

class SmartChoice(
    override val name: String,
    initialValue: Int,
) : Smart() {
    override val value = initialValue + 10
}

class SmartContent(
    override val name: String,
    override val value: Int,
) : Smart() {}

class BadgeCollection<out T: Badge>(val badge: T) {
    companion object {
        fun getRandomBadge(): BadgeCollection<Badge> = BadgeCollection(
            badge = when (Random.nextInt(0,100)) {
                in 0..10 -> Beautiful("Beautiful account")
                in 11..20 -> Popular("Popular account", 10)
                in 21..30 -> SmartContent("Good content", 15)
                in 31..40 -> SmartChoice("Collector", 15)
                in 41..50 -> Popular("Very popular account", 20)
                in 51..60 -> SmartContent("Best content", 25)
                in 61..70 -> SmartChoice("Connoisseur", 25)
                else -> SmartChoice("Unique choice", Random.nextInt(35,100))
            }
        )
    }
}

class BadgeExchanger<T> where T: Badge, T: Exchanged {
    fun exchange(exchangedBadge: T): Int {
        return exchangedBadge.value + 5
    }
}




