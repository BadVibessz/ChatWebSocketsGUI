package mine.server.core.services

import mine.server.entities.User
import org.jetbrains.exposed.sql.transactions.transaction

class LoginService {

    fun login(nickname: String, password: String): User? =
        transaction { User.all().find { it.nickname == nickname && it.password == password } }
}