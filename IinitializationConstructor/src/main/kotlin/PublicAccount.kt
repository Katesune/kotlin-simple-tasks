abstract class PublicAccount(

) : Tracked {
    abstract override val favoriteContent: MutableSet<Content>

    open fun displayMainInformation() {
        for (favorite in favoriteContent) {
            println(favorite.title + " " + favorite.popularity)
        }
    }

    open fun displayFavoriteContent() {
        println(favoriteContent.joinToString(", ") { it.title })
    }
}

class User (
    private val email: String,
    private val nickName: String,
    val contentCollection: Set<Content> = mutableSetOf<Content>()

): PublicAccount() {
    override val favoriteContent = mutableSetOf<Content>()

    init {
        require(email.isNotBlank()) { "The email should not be blank." }
        require(nickName.isNotBlank()) { "The nick name should not be blank." }
    }

    override var likesCount = 0
        set(value) {
            field += value
        }

    override fun likeIt(content: Content) {
        favoriteContent += content
        super.likeIt(content)
    }

    override fun displayMainInformation() {
        println(nickName)
        super.displayMainInformation()
    }

    override fun displayFavoriteContent() {
        println("Current user $nickName favorite content:")
        super.displayFavoriteContent()
    }

}

class Community (
    private val title: String,
    private var peopleCount: Int

): PublicAccount() {
    override val favoriteContent = mutableSetOf<Content>()

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

    override var likesCount = 0
        set(value) {
            rating += value
            field += value
        }

    override fun likeIt(content: Content) {
        content.likesCount = peopleCount
        super.likeIt(content)
    }

    override fun displayMainInformation() {
        println("$title ; people = $peopleCount")
        super.displayMainInformation()
    }

    override fun displayFavoriteContent() {
        println("Current community $title favorite content:")
        super.displayFavoriteContent()
    }

    override fun requestForAccess(content: Content) {
        if (rating < 2) println("Your community's rating is not high enough")
        else super.requestForAccess(content)
    }
}