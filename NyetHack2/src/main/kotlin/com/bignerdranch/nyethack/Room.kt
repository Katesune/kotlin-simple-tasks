package com.bignerdranch.nyethack

import kotlin.random.Random
import kotlin.random.nextInt
import Monster
import Goblin
import Draugr
import Werewolf
import Dragon
import DropOffBox
import Gemstones
import Hat
import Loot
import LootBox
import Sellable

open class Room(val name: String) {

    protected open val status = "Calm"
    open val lootBox: LootBox<Loot> = LootBox.random()

    open fun description() = "$name (Currently: $status)"
    open fun enterRoom() {
        narrate("There is nothing to do here")
    }
}

open class MonsterRoom(
    name: String,
    var monster: Monster? = when(Random.nextInt(0..4)) {
        0 -> { null }
        1 -> { Goblin() }
        2 -> { Draugr() }
        3 -> { Werewolf() }
        else -> { Dragon() }
    }
) : Room(name) {

    override fun description() = super.description() + " (Creature: ${monster?.description ?: "None"}"

    override fun enterRoom() {
        if (monster == null) {
            super.enterRoom()
        } else {
            narrate("Danger is lurking in this room")
        }
    }
}

class TownSquare : Room("The Town Square") {
    override val status = "Building"
    private var bellSound = "GWONG"
    val hatDropOffBox = DropOffBox<Hat>()
    val gemDropOffBox = DropOffBox<Gemstones>()

    final override fun enterRoom() {
        narrate("The villagers rally and cheer as the hero enters")
        ringBell()
    }

    fun ringBell() {
        narrate("The bell tower announces the hero's presence: $bellSound")
    }

    fun <T> sellLoot(
        loot: T
    ): Int where T: Loot, T : Sellable {
        return when (loot) {
            is Hat -> hatDropOffBox.sellLoot(loot)
            is Gemstones -> gemDropOffBox.sellLoot(loot)
            else -> 0
        }
    }
}