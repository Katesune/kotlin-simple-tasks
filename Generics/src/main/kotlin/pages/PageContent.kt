class News(
    val title: String,
    val body: String,
    hashtags: String
) {
    val readyHashtags = hashtags
        get() = field.split(",").joinToString { "#$it " }
}

class Comment(
    val author: String,
    val body: String,
    val likes: Int
) {}