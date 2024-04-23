package org.example

import AdminPage
import News
import PersonalPage
import UserBase
import java.io.File
import javax.crypto.KeyGenerator

fun main() {

    val currentUser = User("katesune.akk@gmail.com", "Katesune", "rbheoijw")
    val admin = Admin("katesune.akk@gmail.com", "Katesune", "rbheoijw")

    val userBase = UserBase()

    val adminPage = AdminPage(admin, userBase)
    adminPage.openPage()
    adminPage.printContent()

    val personalPage = PersonalPage(currentUser, userBase)
    personalPage.printContent()
    personalPage.changeNickName("Yolla")
    personalPage.changeEmail("fheiufhiefh@gmail")
    personalPage.openPage()
    personalPage.printContent()
}
