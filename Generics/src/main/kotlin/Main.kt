package org.example

import AdminPage
import AvailableToSwitchPages
import Navigator
import Page
import PersonalPage
import UserBase

lateinit var currentUser: User
fun main() {
    convertInputToUser()

    WebSite.browse()
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

    init {
        if (checkAccessToEntry() && identify()) {
            changeUserTypeBasedOnRole()
        } else throw IllegalStateException("Identification failed")
    }

    private fun checkAccessToEntry(): Boolean {
        return currentUser.status == Status.ACTIVE
    }
    private fun identify(): Boolean {
        val userFromUserBase = userBase.getUserByEmail(currentUser.email)
        return userFromUserBase.verifyPass(currentUser.password)
    }

    private fun changeUserTypeBasedOnRole() {
        currentUser = userBase[currentUser]

        currentUser = when (currentUser.role) {
            User.Role.ADMIN -> currentUser.toAdmin()
            User.Role.MODERATOR -> currentUser.toModerator()
            else -> currentUser
        }
    }

    private val availablePages = AvailableToSwitchPages(currentUser, userBase)
    private val navigator = Navigator(availablePages)
    private var currentPage: Page<out Any> = navigator.getPageByCommand(1)

    val commandNum: () -> Int = {
        println("Please enter the command number")
        val inputCommand = readlnOrNull() ?: ""
        inputCommand.toIntOrNull() ?: throw IllegalStateException("The command could not be converted to a number.")
    }

    fun browse() {
        while (beOnWebSite) {
            navigator.printCurrentPagesMenu(currentPage)

            when (val command = commandNum()) {
                0 -> beOnWebSite = false
                in navigator.switchCommandsKeys() -> switchToPage(command)
                in navigator.optionalCommandsKeys(currentPage) ->
                {
                    val optionEditCommand = navigator.editDataCommandsMenu(currentPage)[command]
                        ?: throw IllegalStateException("No such command")

                    editPersonalPage(optionEditCommand.executeCommand(currentPage))
                }
//                {
//                    if (currentPage is AdminPage) {
//                        val editUserEmail = getInputEditEmail()
//                        editPersonalPage(navigator.getEditPageFromAdminPage())
//                    }
//                    else if (currentPage is PersonalPage<*, *>) {
//                        editPersonalPage(currentPage as PersonalPage<*, *>)
//                    }
//                }
                else -> println("There is no such command")
            }

        }
    }

    private fun switchToPage(command: Int) {
        currentPage = navigator.getPageByCommand(command)
        currentPage.printWholePage()
    }

    private fun editPersonalPage(editUserPage: PersonalPage<*, *>) {
        editUserPage.printEditMenu()

        val command = commandNum()
        editUserPage.editUserData(command)

        currentUser = editUserPage.currentUser
        editUserPage.printWholePage()
    }
}
