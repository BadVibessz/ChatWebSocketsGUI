package mine.client.gui.ui

import mine.client.gui.Client
import mine.client.gui.windows.ChatWindow
import mine.client.gui.windows.LoginWindow
import mine.client.gui.windows.RegisterWindow

class GUI(sendCallback: (String) -> Unit) : UI(sendCallback) {

    var chatWindow: ChatWindow? = null
    var loginWindow: LoginWindow? = null
    var registerWindow: RegisterWindow? = null

    init {
        // todo: event for client? (callback)s
    }

    override fun requestRegistration() {
        loginWindow?.dispose()
        loginWindow = null

        registerWindow = RegisterWindow(this)
    }

    override fun requestLogin() {
        registerWindow?.dispose()
        registerWindow = null

        loginWindow = LoginWindow(this)
    }

    override fun showAlert(msg: String) {
        registerWindow?.showMessageBox(msg)
        loginWindow?.showMessageBox(msg)
    }

    override fun showMessage(msg: String) {
        chatWindow?.appendToTextArea(msg)
    }

    override fun updateUserList(users: List<String>) {
        chatWindow?.setUsers(users)
    }

    override fun init() {

        loginWindow?.dispose()
        loginWindow = null

        chatWindow = ChatWindow(this)
        showAlert("You have successfully logged in!\n")
    }



}