package com.bignerdranch.nyethack

fun String.addEnthusiasm(entusiasmLevel: Int = 1) = this + "!".repeat(entusiasmLevel)

val String.numVowels
    get() = count { it.lowercase() in "aeiuo" }

inline fun <T> T.print(): T {
    println(this)
    return this
}

operator fun List<List<Room>>.get(coordinate: Coordinate) = getOrNull(coordinate.y)?.getOrNull(coordinate.x)

infix fun Coordinate.move(direction: Direction) = direction.updateCoordinate(this)

inline fun String.frame(padding: Int) : String {
    val formatChar = "*"
    val greeting = "$this!"
    val middle = formatChar
        .padEnd(padding)
        .plus(greeting)
        .plus(formatChar.padStart(padding))
    val end = (0 until middle.length).joinToString("") { formatChar }
    return "$end\n$middle\n$end"
}