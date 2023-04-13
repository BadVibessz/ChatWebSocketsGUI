package mine.client

import kotlinx.coroutines.*
import mine.io.Communicator
import mine.io.utils.Hasher
import java.net.Socket

class Client(
    host: String,
    port: Int,
) {
    private val s: Socket
    private val cmn: Communicator
    private val mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private var _lastResponse: String? = null

    init {
        s = Socket(host, port)
        cmn = Communicator(s)
    }

    fun start() = mainCoroutineScope.launch {
        launch {
            cmn.startReceiving {
                parse(it)
            }
        }

        launch {
            while (isActive) {
                var s = readlnOrNull() ?: ""

                if (_lastResponse == "LOGorREG") {
                    if (s.lowercase() == "войти")
                        s = "login"
                    else if (s.lowercase() == "зарегистрироваться")
                        s = "register"
                }

                if (_lastResponse == "REG") // todo: enhance security
                {
                    val splitted = s.split(' ')

                    val nickname = splitted[0]
                    val hashedPassword = Hasher.hashString(splitted[1], "SHA-256", Charsets.UTF_8)

                    s = "register $nickname $hashedPassword"
                };
                else if (_lastResponse == "LOG")
                {
                    val splitted = s.split(' ')

                    val nickname = splitted[0]
                    val hashedPassword = Hasher.hashString(splitted[1], "SHA-256", Charsets.UTF_8)

                    s = "login $nickname $hashedPassword"
                };
                cmn.sendData(s)
            }
        }
    }

    private fun parse(data: String) {
        data.split(":", limit = 2).let {

            _lastResponse = it[0]

            when (it[0]) {

                "LOGorREG" -> {
                    println("Хотите зарегистрироваться или войти?: {войти} или {зарегистрироваться}:")
                }

                "REG" -> {
                    println("Зарегистрируйтесь: {nickname} {password}:")
                }

                "LOG" -> {
                    println("Войдите: {nickname} {password}:")
                }

                "WRONG" -> {
                    println("Неверный никнейм или пароль")
                }

                "INTR" -> {
                    print("Представьте себя: ")
                }

                "REINTR" -> {
                    print("Имя занято, выберите другое.")
                }

                "NAMEOK" -> {
                    println("Вы успешно вошли в чат")
                }

                "MSG" -> {
                    println(it[1])
                }

                "NEW" -> {
                    println("Пользователь ${it[1]} вошёл в чат")
                }

                "EXIT" -> {
                    println("Пользователь ${it[1]} покинул чат")
                }
            }
        }
    }
}