package org.example

import java.time.Duration

sealed class PlayableContentData {
    class Song(val name: String): PlayableContentData()
    class VideoClip(val name: String, val initialDuration: Long): PlayableContentData()
}

fun getContentData(content: PlayableContentData): String {
    val resetColor = "\u001B[0m"
    val whiteTextColor= "\u001b[38;2;255;255;255m"
    val blueBackColor = "\u001b[48;2;92;99;175m"

    return when (content) {
        is PlayableContentData.Song -> "\n" + whiteTextColor + blueBackColor + "Song name - " + content.name + "$resetColor\n"
        is PlayableContentData.VideoClip -> content.name + " " + whiteTextColor + blueBackColor + content.initialDuration.toString() + resetColor
    }
}

sealed interface PlayableContent {
    val timeDelay: () -> Int
    val text: String

    fun play() {
        if (this is VideoClip) this.runClip()

        for (word in text.split(" ")) {
            println(word)
            Thread.sleep(timeDelay().toLong())
        }
    }
}

class Song (
    val name: String,
    val initialDuration: Long,
    override val text: String,
): PlayableContent {
    private val duration: Duration = Duration.ofMinutes(initialDuration)

    private val normalTimeDelay = 3_000
    private val slowlyTimeDelay = 5_000
    private val fastTimeDelay = 1_000

    override var timeDelay: () -> Int = {
        when (duration) {
            in Duration.ofMinutes(0L)..Duration.ofMinutes(2L) -> fastTimeDelay
            in Duration.ofMinutes(5L)..Duration.ofMinutes(7L) -> slowlyTimeDelay
            else -> normalTimeDelay
        }
    }

    constructor(song: Song): this (
        name = song.name,
        initialDuration = song.initialDuration,
        text = song.text
    )


    val availableVideoClip = VideoClip(name, text, duration)

    companion object {
        fun convertDataToSong(data: String): Song {
            val songData = data.split(",")
            require(songData.size > 2)
            return Song(songData[0], songData[1].trim().toLong(), songData[2])
        }
    }
}

open class VideoClip (
    open val name: String,
    override val text: String,
    initialDuration: Duration,
): PlayableContent {

    private val normalTimeDelay = 2_000
    private val fastTimeDelay = 1_000

    override var timeDelay: () -> Int = {
        when (initialDuration) {
            in Duration.ofMinutes(0L)..Duration.ofMinutes(4L) -> fastTimeDelay
            else -> normalTimeDelay
        }
    }

    open fun runClip() {
        println("Launching the clip!")
        println("End of the clip")
    }
}

val notFoundClip = object: VideoClip(
    "Not found",
    "It seems you missed your chance.",
    Duration.ofMinutes(0)
) {
    override fun runClip() {
        println("Oops..$text Clip $name")
    }
}

