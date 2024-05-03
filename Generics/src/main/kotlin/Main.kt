package org.example

import AdminPage
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

    private val navigator = Navigator(currentUser, userBase)
    private var currentPage: Page<out Any> = navigator.getPageByCommand(1)

    val recycleCommandToInt: () -> Int = {
        println("Please enter the command number")
        val inputCommand = readlnOrNull() ?: ""
        inputCommand.toIntOrNull() ?: throw IllegalStateException("The command could not be converted to a number.")
    }

    fun browse() {
        while (beOnWebSite) {
            navigator.currentPage = currentPage

            navigator.printCommandsCatalog()

            when (val commandNum = recycleCommandToInt()) {
                0 -> beOnWebSite = false
                in navigator.switchCommandsKeys() -> switchToPage(commandNum)
                in navigator.optionalCommandsKeys() -> {
                    changeUserDataInPersonalPage(commandNum)
                    navigator.updateSwitchCatalog()
                }
                else -> println("There is no such command")
            }
        }
    }

    private fun switchToPage(inputCommandNum: Int) {
        currentPage = navigator.getPageByCommand(inputCommandNum)
        currentPage.printWholePage()
    }

    private fun changeUserDataInPersonalPage(inputCommandNum: Int) {
        val editCommand = navigator.getEditCommand(inputCommandNum)
        val editPage = editCommand.getCurrentEditPage()

        printEditPageCatalog(editPage)
        val inputEditCommand = recycleCommandToInt()
        
        if (inputEditCommand != 0) {
            editCommand.runChangingDataProcess(editPage, inputEditCommand)
        }
    }
    
    private fun printEditPageCatalog(editPage: PersonalPage<User, UserBase>) {
        navigator.printExitCommand()
        editPage.printChangeCatalog()
    }
}
