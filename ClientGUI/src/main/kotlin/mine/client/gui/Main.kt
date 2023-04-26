package mine.client.gui

import kotlinx.coroutines.runBlocking
import mine.client.gui.ui.UIType
import mine.client.gui.windows.LoginWindow


fun main() = runBlocking {
    Client("localhost", 5004, UIType.GUI).start().join()
}

