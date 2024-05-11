sealed interface Tracked {

    fun likeIt(content: Liked) {
        content.likesCount += 1
        if (this is PublicAccount) this.addToFavoriteContent(content)
        if (content is Community) content.peopleCount += 1
    }

    fun requestForAccess(content: Content) {
        if (content.accessForWatching == AccessForWatching.AVAILABLE)
            println("Access is already allowed")
        else {
            content.accessForWatching = AccessForWatching.AVAILABLE
            println("Access is allowed")
        }
    }
}

sealed interface Liked {
    var likesCount: Int

    fun displayMainInformation()

    fun displayLikedContent(content: Liked) {
        when (content) {
            is Content -> println("\"" + content.title + "\" ; popularity - " + content.popularity)
            is User -> println("User: ${content.nickName}")
            is Community -> println("Community: \"" + content.title + "\"; " + content.peopleCount + " subscribers")
            else -> println("Favorite Content is not yet supported")
        }
    }

    fun displayLikes() {
        println("\nLikes - $likesCount\n")
    }
}


