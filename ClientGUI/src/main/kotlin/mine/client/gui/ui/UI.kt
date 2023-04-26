package mine.client.gui.ui


abstract class UI(private val sendCallback: (String) -> Unit) {

    abstract fun requestRegistration()
    abstract fun requestLogin()
    abstract fun showAlert(msg: String)
    abstract fun showMessage(msg: String)
    abstract fun updateUserList(users: List<String>)
    abstract fun init()

    fun sendToServer(message: String) = sendCallback(message)

}