import com.bignerdranch.nyethack.narrate
import kotlin.random.Random

interface Fightable {
    val name: String
    var healthPoints: Int
    val diceCount: Int
    val diceSides : Int

    fun takeDamage(damage: Int)

    fun attack(opponent: Fightable) {
        val damageRoll = (0 until diceCount).sumOf {
            Random.nextInt(diceSides + 1)
        }
        narrate("$name deals $damageRoll to ${opponent.name}")
        opponent.takeDamage(damageRoll)
    }
}

abstract class Monster(
    override val name: String,
    val description: String,
    override var healthPoints: Int
) : Fightable {
    override fun takeDamage(damage: Int) {
        healthPoints -= damage
    }
}

class Goblin(
    name: String = "Goblin",
    description: String = "A nasty-looking goblin",
    healthPoints: Int = 30
) : Monster(name, description, healthPoints) {
    override val diceCount = 2
    override val diceSides = 8
}

class Draugr(
    name: String = "Draugr",
    description: String = "A hot-mighty draugr",
    healthPoints: Int = 40
) : Monster(name, description, healthPoints) {
    override val diceCount = 5
    override val diceSides = 16
}

class Werewolf(
    name: String = "Werewolf",
    description: String = "A pathetic loser werewolf",
    healthPoints: Int = 20
) : Monster(name, description, healthPoints) {
    override val diceCount: Int = 3
    override val diceSides: Int = 5
}

class Dragon(
    name: String = "Dragon",
    description: String = "Strong and beautiful dragon",
    healthPoints: Int = 60
) : Monster(name, description, healthPoints) {
    override val diceCount: Int = 8
    override val diceSides: Int = 4
}