package org.example

import java.io.File
import java.time.Duration

fun main() {
    val musicPlayer = MusicPlayer
    musicPlayer.playMusic()
}

object MusicPlayer {

    private val songs = File("data/songs.txt")
        .readText()
        .split("\n")
        .map {
            Song.convertDataToSong(it)
        }

    private fun List<Song>.getByName(name: String) = this.filter { song: Song -> song.name == name }

    init {
        println("Start music player")
    }

    fun playMusic() {

        for (song in songs) {
            var currentSong = song

            when(currentSong.duration) {
                in Duration.ofMinutes(0L)..Duration.ofMinutes(2L) -> playFaster()
                in Duration.ofMinutes(5L)..Duration.ofMinutes(7L) -> playSlowly()
                else -> playNormal()
            }

            println("\nSong name - " + song.name)
            for (word in currentSong.text.split(" ")) {
                println(word)
                Thread.sleep(timeDelay)
            }
        }

        suggestVideoClip()
    }

    private var timeDelay = 2_000.toLong()

    private fun playNormal() {
        timeDelay = 2_000
    }

    private fun playSlowly() {
        timeDelay = 3_000
    }

    private fun playFaster() {
        timeDelay = 1_000
    }

    private fun suggestVideoClip() {
        println("Do you want to watch a video clip for any of the songs? Please enter its name")
        val videoName = readlnOrNull() ?: throw IllegalArgumentException("The title of the video clip should not be empty")
        val foundSong = songs.getByName(videoName)
        if (foundSong.isNotEmpty()) foundSong[0].availableVideoClip.runClip()
        else notFoundClip.runClip()
    }
}

class Song (
    val name: String,
    initialDuration: Long,
    val text: String,
) {
    val duration: Duration = Duration.ofMinutes(initialDuration)

    val availableVideoClip = VideoClip(name, text)

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
    open val text: String,
) {
    open fun runClip() {
        println("Launching the clip!")
        println(text)
        println("End of the clip")
    }
}

val notFoundClip = object: VideoClip(
    "Not found",
    "It seems you missed your chance."
) {
    override fun runClip() {
        println("Oops..$text Clip $name")
    }
}







