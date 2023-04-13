package mine.client

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    Client("localhost", 5004).start().join()
}
