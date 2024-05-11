abstract class PublicAccount(

) : Tracked, Liked {
    override var likesCount = 0

    abstract val favoriteContent: MutableSet<Liked>

    fun addToFavoriteContent(content: Liked) {
        favoriteContent += content
    }

    val purpleColor = "\u001b[38;2;108;65;244m"
    val resetColor = "\u001B[0m"

    fun displayLikedContent() {
        println("Favorite content: ")
        if (favoriteContent.isEmpty()) print("Not found")
        for (favorite in favoriteContent) {
            displayLikedContent(favorite)
        }
        println()
    }
}

class User (
    email: String,
    val nickName: String,
): PublicAccount() {
    override val favoriteContent = mutableSetOf<Liked>()

    init {
        require(email.isNotBlank()) { "The email should not be blank." }
        require(nickName.isNotBlank()) { "The nick name should not be blank." }
    }

    override fun displayMainInformation() {
        println("$purpleColor$nickName $resetColor")
    }

}

class Community (
    val title: String,
    var peopleCount: Int

): PublicAccount() {
    override var likesCount = peopleCount
    override val favoriteContent = mutableSetOf<Liked>()

    init {
        require(title.isNotBlank()) { "The title should not be blank." }
    }

    constructor(
        title: String
    ): this (
        title = title,
        peopleCount = 0
    )

    private var rating = 0

    override fun likeIt(content: Liked) {
        content.likesCount += peopleCount
        addToFavoriteContent(content)
    }

    override fun displayMainInformation() {
        println("Community: \"$purpleColor$title$resetColor\"; $peopleCount subscribers")
    }

    override fun requestForAccess(content: Content) {
        if (rating < 2) println("Your community's rating is not high enough")
        else super.requestForAccess(content)
    }
}