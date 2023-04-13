package mine.server.core.services

import mine.server.entities.User
import org.jetbrains.exposed.sql.transactions.transaction

class RegisterService {

    fun register( nickname: String, password: String): Boolean {

        var success = true

        transaction {

            // user with this nickname already exist
            if (User.all().find { it.nickname == nickname } != null) {
                success = false
                close()
            }

            User.new {
                this.nickname = nickname
                this.password = password
            }

        }

        return success
    }
}