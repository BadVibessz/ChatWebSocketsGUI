package mine.client.gui.windows

import mine.client.gui.Client
import mine.client.gui.ui.GUI
import java.awt.Color
import java.awt.Dimension
import java.lang.StringBuilder
import javax.swing.*
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext


class ChatWindow(gui: GUI) : Chat(gui) {

    private val _minSize = Dimension(200, 300)
    private val _startSize = Dimension(600, 400)

    private val _usersPanel = JPanel().apply { background = Color.LIGHT_GRAY }
    val usersLabel = JLabel()


    private val _textArea = JTextArea().apply {
        isEditable = false
    }

    private val _scrollPane = JScrollPane(_textArea)

    private val _messageTextField = JTextField()

    companion object {
        val SHRINK = GroupLayout.PREFERRED_SIZE
        val GROW = GroupLayout.DEFAULT_SIZE
    }


    init {
        minimumSize = _minSize
        size = _startSize

        setLocationRelativeTo(null)

        _usersPanel.add(usersLabel)

        setupLayout()
        setupEventListeners()
    }


    fun setUsers(users: List<String>) {
        val text = StringBuilder()
        text.append("<html>")
        text.append("<br>Online users:</br>")
        text.append("<br>You</br>")

        users.forEach { text.append("<br>$it</br>") }
        text.append("</html>")

        usersLabel.text = text.toString()
    }

    private fun appendToPane(tp: JTextPane, msg: String, c: Color) {
        val sc = StyleContext.getDefaultStyleContext()
        var aset: AttributeSet? = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c)
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console")
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED)
        val len = tp.document.length
        tp.caretPosition = len
        tp.setCharacterAttributes(aset, false)
        tp.replaceSelection(msg)
    }

    private fun setupEventListeners() {

        _messageTextField.addActionListener {
            // todo: send to server
            //_client.sendMessage(_messageTextField.getText())
            gui.sendToServer(_messageTextField.getText())

            _messageTextField.text = ""
        }
    }

    fun appendToTextArea(msg: String) =
        _textArea.append(msg)


    private fun setupLayout() {
        layout = GroupLayout(contentPane).apply {
            setVerticalGroup(

                createSequentialGroup()
                    .addGap(5)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_usersPanel, GROW, GROW, GROW)
                            .addComponent(_scrollPane, GROW, GROW, GROW)
                    )
                    .addGap(10)
                    .addComponent(_messageTextField, 20, 20, 20)
                    .addGap(5)

            )

            setHorizontalGroup(

                createSequentialGroup()
                    .addGap(5)
                    .addComponent(_usersPanel, 100, 100, 100)
                    .addGap(5)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_scrollPane, GROW, GROW, GROW)
                            .addComponent(_messageTextField, GROW, GROW, GROW)
                    )
            )
        }


    }

}