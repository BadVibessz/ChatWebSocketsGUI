package mine.client.gui.windows

import mine.client.gui.Client
import mine.client.gui.ui.GUI
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JOptionPane

abstract class Chat(protected val gui: GUI) : JFrame() {

    init {
        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(windowEvent: WindowEvent) {
                if (JOptionPane.showConfirmDialog(
                        this@Chat,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    ) == JOptionPane.YES_OPTION
                ) {
                    System.exit(0) // todo: client.stop()?
                }
            }
        })

        isVisible = true
    }

}