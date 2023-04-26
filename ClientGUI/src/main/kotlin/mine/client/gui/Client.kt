package mine.client.gui

import kotlinx.coroutines.*
import mine.client.gui.ui.GUI
import mine.client.gui.ui.UI
import mine.client.gui.ui.UIType
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
    private var _ui: UI?


    /*TODO:
    *  implement client independent of  ways to visualize data
    *  e.g use callbacks in parse method to handle server's response
    * */


    init {
        _socket = Socket(host, port)
        _communicator = Communicator(_socket)

        _ui = when (uiType) {

            UIType.GUI -> GUI(this::sendMessage)

            //todo:
            UIType.Console -> null

            else -> null
        }

        _ui?.requestLogin()
    }

    fun start() = _mainCoroutineScope.launch {
        launch {
            _communicator.startReceiving {
                parse(it)
            }
        }
    }
    fun sendMessage(msg: String) = _communicator.sendData(msg)

    private fun parse(data: String) {
        data.split(":", limit = 2).let {

            when (it[0]) {

                "REGOK" -> {
                    _ui?.showAlert("You have successfully registered!")
                }

                "WRONG" -> {
                    _ui?.showAlert("Wrong nickname or password.")
                }

                "INTR" -> {
                    _ui?.requestLogin()
                }

                "REINTR" -> {
                    _ui?.showAlert("This nickname already taken.")
                }

                "NAMEOK" -> {
                    _ui?.init()
                    _communicator.sendData("get-users")
                }

                "MSG" -> {
                    _ui?.showMessage("${it[1]}\n")

                }

                "NEW" -> {
                    _ui?.showMessage("User ${it[1]} is online now\n")
                    _communicator.sendData("get-users")
                }

                "GETUSERS" -> {
                    val users = it[1].split(';')
                    _ui?.updateUserList(users)
                }

                "EXIT" -> {
                    _ui?.showMessage("User ${it[1]} is offline now\n")
                    _communicator.sendData("get-users")
                }

                else -> {} //todo:
            }
        }
    }
}