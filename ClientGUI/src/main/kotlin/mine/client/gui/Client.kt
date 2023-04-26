package mine.client.gui

import kotlinx.coroutines.*
import mine.client.gui.ui.GUI
import mine.client.gui.ui.UI
import mine.client.gui.ui.UIType
import mine.client.gui.windows.ChatWindow
import mine.client.gui.windows.LoginWindow
import mine.client.gui.windows.RegisterWindow
import mine.io.Communicator
import java.net.Socket

class Client(
    host: String,
    port: Int,
    uiType: UIType
) {
    private val _socket: Socket
    private val _communicator: Communicator
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private var _lastResponse: String? = null

    var ui: UI? = null


    /*TODO:
    *  implement client independent of  ways to visualize data
    *  e.g use callbacks in parse method to handle server's response
    * */

    private fun initUI(uiType: UIType): UI? =
        when (uiType) {

            UIType.GUI -> GUI(this)

            //todo:
            UIType.Console -> null

            else -> null
        }


    init {
        _socket = Socket(host, port)
        _communicator = Communicator(_socket)

        ui = initUI(uiType)

        //loginWindow = LoginWindow(this).apply { isVisible = true }
        //GUIHelper.showLoginWindow(this)
        ui?.requestLogin()
    }

    fun start() = _mainCoroutineScope.launch {
        launch {
            _communicator.startReceiving {
                parse(it)
            }
        }

        launch {
            // todo: if instead of while?
//            if (isActive) {
//                //chatWindow?.isVisible = true
//                GUIHelper.showChatWindow(this@Client)
//            }
        }
    }

    fun sendMessage(msg: String) {
        _communicator.sendData(msg)
    }

    private fun parse(data: String) {
        data.split(":", limit = 2).let {

            _lastResponse = it[0]

            when (it[0]) {

                "REGOK" -> {
                    //registerWindow!!.showMessageBox("You have successfully registered!")
                    //GUIHelper.showMessageBox("You have successfully registered!")
                    ui?.showAlert("You have successfully registered!")
                }

                "WRONG" -> {
                    // todo: if/else?
                    //loginWindow!!.showMessageBox("Wrong nickname or password.")
                    // GUIHelper.showMessageBox("Wrong nickname or password.")
                    ui?.showAlert("Wrong nickname or password.")
                }

                "INTR" -> {
                    ui?.requestLogin()
                }

                "REINTR" -> {
                    // todo: if/else?
                    // registerWindow!!.showMessageBox("This nickname already taken.")
                    //GUIHelper.showMessageBox("This nickname already taken.")
                    ui?.showAlert("This nickname already taken.")
                }

                "NAMEOK" -> {
                    ui?.init()
                    _communicator.sendData("get-users")
                }

                "MSG" -> {
                    //chatWindow!!.appendToTextArea("${it[1]}\n")
                    //GUIHelper.appendMessageToChatWindow("${it[1]}\n")
                    ui?.showMessage("${it[1]}\n")

                }

                "NEW" -> {
                    //chatWindow!!.appendToTextArea("User ${it[1]} is online now\n")

                    //GUIHelper.appendMessageToChatWindow("User ${it[1]} is online now\n")
                    ui?.showMessage("User ${it[1]} is online now\n")

                    _communicator.sendData("get-users")
                }

                "GETUSERS" -> {
                    val users = it[1].split(';')
                    ui?.updateUserList(users)
                    //GUIHelper.setUsersToChatWindow(users)
                    //chatWindow!!.setUsers(users)
                }

                "EXIT" -> {
                    //chatWindow!!.appendToTextArea("User ${it[1]} is offline now\n")

                    //GUIHelper.appendMessageToChatWindow("User ${it[1]} is offline now\n")
                    ui?.showMessage("User ${it[1]} is offline now\n")
                    _communicator.sendData("get-users")
                }

                else -> {} //todo:
            }
        }
    }
}