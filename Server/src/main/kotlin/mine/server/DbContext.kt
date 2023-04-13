package mine.server

import mine.server.entities.Users
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.VarCharColumnType
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception

class DbContext {

    private var _connection: Database? = null

    init {
        _connection = getConnection()
        _connection?.let { establishConnection(it) }

    }

    private fun establishConnection(connection: Database) =
        transaction(connection) {

            // creates table if it doesn't exist yet
            SchemaUtils.create(Users)

//            // drop email column
//            val col = Column<Int>(Users, "email", VarCharColumnType())
//            col.dropStatement().forEach { exec(it) }

        }


    private fun getConnection(): Database? {

        var connection: Database? = null
        try {
            connection = Database.connect(
                url = "jdbc:postgresql://localhost:5432/ChatWS",
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = "gunna"
            )
        } catch (e: Exception) {
            println(e.message)
        }

        return connection
    }

}