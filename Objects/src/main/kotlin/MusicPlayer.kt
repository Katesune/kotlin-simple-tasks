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
            val currentSongData = PlayableContentData.Song(song.name)
            println(getContentData(currentSongData))
            song.play()
        }

        suggestVideoClip()
    }

    private fun suggestVideoClip() {
        val foundSong = requestVideoName()

        foundSong?.let {
                val videoClip =  foundSong.availableVideoClip
                println(getContentData(PlayableContentData.VideoClip(foundSong.name, foundSong.initialDuration)))
                videoClip.play()
        } ?: notFoundClip.runClip()

    }

    private fun requestVideoName(): Song? {
        println("Do you want to watch a video clip for any of the songs? Please enter its name")
        val videoName = readlnOrNull() ?: throw IllegalArgumentException("The title of the video clip should not be empty")
        return songs.getByName(videoName).getOrNull(0)
    }
}





