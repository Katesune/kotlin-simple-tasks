package org.example

import AdminPage
import Navigator
import NewsPage
import Page
import PersonalPage
import UserBase

lateinit var currentUser: User
fun main() {
    convertInputToUser()

    if (WebSite.identify() && WebSite.checkAccessToEntry()) {
        WebSite.browse()
    } else println("Identification failed")

}
private fun convertInputToUser() {
    println("Please enter your email address")
    val inputEmail = readlnOrNull() ?: ""

    println("Please enter your password")
    val inputPassword = readlnOrNull() ?: ""

    require(inputEmail.isNotEmpty() && inputPassword.isNotEmpty()) {
        "Email and password must not be empty"
    }

    currentUser = User(email = inputEmail, password = inputPassword)
}

object WebSite {

    private var beOnWebSite = true

    private val userBase = UserBase()

    fun identify(): Boolean {
        val userFromUserBase = userBase.getUserByEmail(currentUser.email)
        return userFromUserBase.verifyPass(currentUser.password)
    }

    fun checkAccessToEntry(): Boolean {
        return currentUser.status == Status.ACTIVE
    }

    private fun changeUserTypeBasedOnRole() {
        currentUser = userBase[currentUser]

        currentUser = when (currentUser.role) {
            User.Role.ADMIN -> currentUser.toAdmin()
            User.Role.MODERATOR -> currentUser.toModerator()
            else -> currentUser
        }

        navigator = Navigator(currentUser, userBase)
    }

    private var navigator = Navigator(currentUser, userBase) // нужно изменить роль пользователя до создания навигатора
    private var currentPage: Page<out Any> = navigator.getPageByCommand(1)

    val commandNum: () -> Int = {
        println("Please enter the command number")
        val inputCommand = readlnOrNull() ?: ""
        inputCommand.toIntOrNull() ?: throw IllegalStateException("The command could not be converted to a number.")
    }
    fun browse() {
        changeUserTypeBasedOnRole()

        while (beOnWebSite) {
            navigator.printCurrentPagesMenu()

            when (val command = commandNum()) {
                0 -> beOnWebSite = false
                in 1..4 -> switchToPage(command)
                5 -> editPersonalPage(getInputEditEmail())
                else -> println("There is no such command")
            }

        }
    }

    private fun switchToPage(command: Int) {
        currentPage = navigator.getPageByCommand(command)
        currentPage.printWholePage()
    }


    private fun getInputEditEmail(): String {
        println("Please enter the address of the user whose data you want to edit")
        return readlnOrNull() ?: ""
    }

    private fun editPersonalPage(editUserEmail: String) {
        val editUserPage = navigator.getEditPersonalPage(editUserEmail)

        editUserPage.printEditUserMenu()

        val command = commandNum()
        editUserPage.editUserData(command)

        editUserPage.printWholePage()
    }

}
