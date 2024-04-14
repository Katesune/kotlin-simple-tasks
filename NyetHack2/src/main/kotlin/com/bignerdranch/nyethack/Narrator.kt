package com.bignerdranch.nyethack

import kotlin.random.Random
import kotlin.random.nextInt

var narrationModifier: (String) -> String = {it}

inline fun narrate (
    message: String,
    modifier: (String) -> String = { narrationModifier(it) }
) {
    println (modifier(message)) }

fun changeNarratorMood() {
    val mood: String
    val modifier: (String) -> String
    when (Random.nextInt(1..8)) {
        1 -> {
            mood = "loud"
            modifier = { message ->
                val numExclamationPoints = 3
                message.uppercase() + "!".repeat(numExclamationPoints)
            }
        }
        2 -> {
            mood = "tired"
            modifier = { message ->
                message.lowercase().replace(" ", "... ")
            }
        }
        3 -> {
            mood = "unsure"
            modifier = { message ->
                "$message?"
            }
        }
        4 -> {
            var narrationsGiven = 0
            mood = "like sending an itemized bill"
            modifier = { message ->
                narrationsGiven++
                "$message.\n(I have narrated $narrationsGiven things)"
            }
        }
        5 -> {
            mood = "lazy"
            modifier = { message ->
                message.take(message.length/2)
            }
        }
        6 -> {
            mood = "mysterious"
            modifier = { message ->
                message.lowercase().replace(Regex("[let]")){
                    when (it.value) {
                        "l" -> "1"
                        "e" -> "3"
                        "t" -> "7"
                        else -> " "
                    }
                }
            }
        }
        7 -> {
            mood = "poetic"
            modifier = { message ->
                message.replace(Regex(" ")) {
                    when (Random.nextInt(0..1)) {
                        0 -> " "
                        else -> "\n"
                    }
                }
            }
        }
        else -> {
            mood ="professional"
            modifier = { message ->
                "$message."
            }
        }
    }

    narrationModifier = modifier
    narrate("The narrator begins to feel $mood")
}
