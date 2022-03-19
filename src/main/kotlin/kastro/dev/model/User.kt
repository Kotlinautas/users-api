package kastro.dev.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.ResultRow
import org.valiktor.functions.hasSize
import org.valiktor.functions.isNotEmpty
import org.valiktor.validate
import java.util.UUID

@Serializable
data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val password: String,
) {
    init {
        validate(this) {
            validate(User::name).isNotEmpty().hasSize(1, 50)
            validate(User::password).isNotEmpty().hasSize(8, 50)
        }
    }
}

object Users: Table() {
    val id: Column<String> = char("id", 36)
    val name: Column<String> = varchar("name", 50)
    val password: Column<String> = varchar("password", 50)
    override val primaryKey = PrimaryKey(id, name = "PK_USER_ID")

    fun toUser(row: ResultRow): User = User(
        id = row[id],
        name = row[name],
        password = row[password]
    )
}