package kastro.dev.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kastro.dev.model.User
import kastro.dev.model.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.userRoute() {
    route("/users") {
        createUser()
        listAll()
        getUser()
        updateUser()
        deleteUser()
    }
}

fun Route.createUser() {
    post {
        try {
            val payloadUser = call.receive<User>()
            val insertion = transaction {
                Users.insert {
                    it[id] = payloadUser.id
                    it[name] = payloadUser.name
                    it[password] = payloadUser.password
                }
            }

            if (insertion.equals(0)) throw Exception()

            return@post call.respond(payloadUser)
        } catch (ex: Exception) {
            return@post call.respondText(
                "Error creating user!",
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}

fun Route.listAll() {
    get {
        try {
            val users = transaction {
                Users.selectAll().map { Users.toUser(it) }
            }

            return@get call.respond(users)
        } catch (ex: Exception) {
            return@get call.respondText(
                "Error fetching users!",
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}

fun Route.getUser() {
    get("{id}") {
        try {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Enter an ID",
                status = HttpStatusCode.PaymentRequired
            )

            val user = transaction {
                Users.select { Users.id eq id }.firstOrNull()
            } ?: return@get call.respondText("User not found!", status = HttpStatusCode.BadRequest)

            return@get call.respond(Users.toUser(user))
        } catch (erro: Exception) {
            return@get call.respondText(
                "Error fetching user",
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}

fun Route.updateUser() {
    put("{id}") {
        try {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Enter an ID",
                status = HttpStatusCode.PaymentRequired
            )
            val user = call.receive<User>()
            val edition = transaction {
                Users.update({ Users.id eq id }) {
                    it[name] = user.name
                    it[password] = user.password
                }
            }
            if (edition.equals(0)) throw Exception()
            return@put call.respond(User(id, user.name, user.password))
        } catch (ex: Exception) {
            return@put call.respondText("Error updating user")
        }
    }
}

fun Route.deleteUser() {
    delete("{id}") {
        try {
            val id = call.parameters["id"] ?: return@delete call.respondText(
                "Enter an ID",
                status = HttpStatusCode.PaymentRequired
            )

            transaction {
                Users.deleteWhere { Users.id eq id }
            }

            return@delete call.respond(status = HttpStatusCode.NoContent, message = "")
        } catch (ex: Exception) {
            return@delete call.respondText("Error removing user")
        }
    }
}