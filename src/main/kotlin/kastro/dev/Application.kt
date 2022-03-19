package kastro.dev

import io.ktor.application.*
import kastro.dev.config.initDB
import kastro.dev.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    initDB()
    configureHTTP()
    configureRouting()
    configureSerialization()
}
