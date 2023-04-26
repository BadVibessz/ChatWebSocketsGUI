package mine.client.gui.windows

import mine.client.gui.Client
import mine.client.gui.ui.GUI
import mine.io.utils.Hasher
import java.awt.Dimension
import javax.swing.*

class RegisterWindow (client: Client, gui: GUI) : Chat(client, gui) {

    private val _loginLabel = JLabel("Nickname")
    private val _passwordLabel = JLabel("Password")
    private val _registerLable = JLabel("Already registered?")

    private val _loginTextField = JTextField()
    private val _passwordTextField = JPasswordField()

    private val _minSize = Dimension(100, 200)
    private val _startSize = Dimension(200, 300)

    private val _submitButton = JButton("Register")
    private val _loginButton = JButton("Log in")

    companion object {
        val SHRINK = GroupLayout.PREFERRED_SIZE
        val GROW = GroupLayout.DEFAULT_SIZE
    }

    init {
        minimumSize = _minSize
        size = _startSize

        setLocationRelativeTo(null)
        setupLayout()
        setupEventListeners()
    }

    private fun setupLayout() {
        layout = GroupLayout(contentPane).apply {
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addComponent(_loginLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_loginTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_passwordLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_passwordTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_submitButton, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_registerLable, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_loginButton, 20, 20, 20)
                    .addGap(10)
            )

            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_loginLabel, GROW, GROW, GROW)
                            .addComponent(_loginTextField, GROW, GROW, GROW)
                            .addComponent(_passwordLabel, GROW, GROW, GROW)
                            .addComponent(_passwordTextField, GROW, GROW, GROW)
                            .addComponent(_submitButton, GROW, GROW, GROW)
                            .addComponent(_registerLable, GROW, GROW, GROW)
                            .addComponent(_loginButton, GROW, GROW, GROW)
                    )
                    .addGap(10)
            )
        }

    }

    fun showMessageBox(message: String) {
        JOptionPane.showMessageDialog(this, message)
        _loginTextField.text = ""
        _passwordTextField.text = ""
    }

    private fun setupEventListeners() {
        _submitButton.addActionListener {
            val nickname = _loginTextField.text
            val password = _passwordTextField.text
            val hashedPassword = Hasher.hashString(password, "SHA-256", Charsets.UTF_8)

            val request = "register $nickname $hashedPassword"

            _client.sendMessage("register")
            _client.sendMessage(request)
        }

        _loginButton.addActionListener {
            gui.requestLogin()
//            dispose()
//            gui.registerWindow = null
//            gui.loginWindow = LoginWindow(_client, gui)
        }

    }


}