package kastro.dev.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kastro.dev.routes.userRoute

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        userRoute()
    }
}
