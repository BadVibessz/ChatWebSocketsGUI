package mine.server.core

import kotlinx.coroutines.*
import mine.server.DbContext
import java.net.ServerSocket


class Server(
    val port: Int = 5004,
) {
    private val _serverSocket = ServerSocket(port)
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val _db = DbContext()


    fun start() = _mainCoroutineScope.launch {
        while (isActive) {
            try {
                _serverSocket.accept().apply {
                    ConnectedClient(this).apply {
                        launch {
                            try {
                                start()
                            } catch (e: Throwable){
                                // TODO: Ошибка в подключенном клиенте
                            }
                        }
                    }
                }
            } catch (_: Throwable){
            }
        }
    }

    fun stop(){
        _mainCoroutineScope.cancel("Client off")
        _serverSocket.close()
    }
}