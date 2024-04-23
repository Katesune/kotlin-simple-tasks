import org.example.Admin
import org.example.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PageTest {
    // NewsPage
    private val news = mutableListOf(
        News("Two singers have released a new fit", "A lot of people liked the new song", "singer,fit,release"),
        News("The city needs volunteers", "Volunteers will help toads cross the road", "volunteer,road,toads"),
        News("The theater will show a reinterpretation of Hamlet", "The reinterpretation will be very different from the original", "theater,Hamlet")
    )

    private val newsPage = NewsPage("Latest news", news)

    @Test
    fun displayEntireNewPage() {
        assertAll(
            { assertEquals(Unit, newsPage.openPage()) },
            { assertEquals(Unit, newsPage.printContent()) },
        )
    }

    //CommentsPage
    private val comments = mutableListOf(
        Comment("naco4@gmail.com", "Add more colors", 25),
        Comment("dopemm4@gmail.com", "Add reactions to comments", 23),
        Comment("sagreo-6749@gmail.com", "Publish more news about animals", 17),
        Comment("dellaye952@gmail.com", "Change the font on the main page", 9),
    )

    private val commentsPage = CommentsPage("Latest comments", comments)

    @Test
    fun displayEntireCommentsPage() {
        assertAll(
            { assertEquals(Unit, commentsPage.openPage()) },
            { assertEquals(Unit, commentsPage.printContent()) },
        )
    }

    //PersonalPage
    private val currentUser = User("katesune.akk@gmail.com", "Katesune", "kEn8djb^Jbcf9")
    private val userBase = UserBase()
    private val personalPage = PersonalPage(currentUser, userBase)

    @Test
    fun displayEntirePersonalPage() {
        assertAll(
            { assertEquals(Unit, personalPage.openPage()) },
            { assertEquals(Unit, personalPage.printContent()) },
        )
    }

    @Test
    fun changeNick() {
        val expected = Unit

        assertAll(
            { assertEquals(expected, personalPage.changeNickName("Yola")) },
            { assertEquals(expected, personalPage.printContent()) },
        )
    }

    //AdminPage

    private val admin = Admin("Koshka@gmail.com", "Koshka", "fO[wf^^WUcn")
    private val adminPage = AdminPage(admin, userBase)

    @Test
    fun displayEntireAdminPage() {
        assertAll(
            { assertEquals(Unit, adminPage.changeNickName("Yola")) },
            { assertEquals(Unit, adminPage.openPage()) },
            { assertEquals(Unit, adminPage.printContent()) },
        )
    }

}