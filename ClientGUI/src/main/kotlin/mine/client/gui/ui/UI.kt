package mine.client.gui.ui


abstract class UI {

    abstract fun requestRegistration()
    abstract fun requestLogin()
    abstract fun showAlert(msg: String)
    abstract fun showMessage(msg: String)
    abstract fun updateUserList(users: List<String>)
    abstract fun init()

    fun sendToServer(message: String, callback: (String) -> Unit) = callback(message)

}