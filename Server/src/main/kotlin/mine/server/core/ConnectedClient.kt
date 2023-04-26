package mine.server.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import mine.io.Communicator
import mine.server.core.services.LoginService
import mine.server.core.services.RegisterService
import mine.server.entities.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.StringBuilder
import java.net.Socket

class ConnectedClient(private val socket: Socket) {

    private val _communicator = Communicator(socket)
    private var _lastRequest: String? = null

    var name: String? = null
        private set(value) {

            value?.let { vl ->
                field = vl
                sendToAllConnectedClients({ if (it == this) "NAMEOK" else "NEW" },
                    { if (it != this) vl else "" }
                )
            } ?: {
                _communicator.sendData("REINTR:")

                val operation = if (_lastRequest == "register") "REG:"
                else if (_lastRequest == "login") "LOG:"
                else ""

                if (operation != "")
                    _communicator.sendData(operation)
            }
        }

    init {
        ConnectedClients.list.add(this)
    }

    suspend fun start() {
        coroutineScope {
            launch {
                try {
                    _communicator.startReceiving { parse(it) }
                } catch (e: Throwable) {
                    ConnectedClients.list.remove(this@ConnectedClient)
                    name?.let {
                        sendToAllConnectedClients({ "EXIT" }, { _ -> it })
                    }
                }
            }
            launch {
                _communicator.sendData("LOGorREG:")
            }
        }
    }

    fun parse(data: String) {

        if (data.lowercase() == "login") {
            _communicator.sendData("LOG:")
            return
        } else if (data.lowercase() == "register") {
            _communicator.sendData("REG:")
            return
        } else if (data.lowercase() == "get-users") {
            val str = StringBuilder()
            str.append("GETUSERS:")
            ConnectedClients.list.forEach { if (it.name != this.name) str.append("${it.name};") }
            _communicator.sendData(str.toString())

            return
        }

        val splitted = data.split(' ')

        if (splitted[0] == "register") {

            _lastRequest = splitted[0]

            val nickname = splitted[1]
            val password = splitted[2]

            val users = transaction { User.all().toList() }

            if (users.find { it.nickname == nickname } != null) {
                _communicator.sendData("REINTR:")
                _communicator.sendData("REG:")
                return
            }

            val succeeded = RegisterService().register(nickname, password)

            _communicator.sendData("REGOK:")
            return

        } else if (splitted[0] == "login") {

            _lastRequest = splitted[0]

            val nickname = splitted[1]
            val password = splitted[2]

            val user = LoginService().login(nickname, password)

            if (user != null)
                name = user.nickname
            else {
                _communicator.sendData("WRONG:")
                _communicator.sendData("LOG:")
            }
            return
        }

        if (name != null)
            sendToAllConnectedClients({ "MSG" }, { "${if (it == this) "YOU" else name}: $data" })
        //else name = data
    }


    private fun sendToAllConnectedClients(cmd: (ConnectedClient) -> String, data: (ConnectedClient) -> String) {
        ConnectedClients.list.forEach {
            it.name?.let { _ ->
                it._communicator.sendData("${cmd(it)}:${data(it)}");
            }
        }
    }
}