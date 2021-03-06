package com.kosterico.todolist

import com.kosterico.todolist.modules.basicAuth
import com.kosterico.todolist.modules.initDb
import com.kosterico.todolist.routes.login
import com.kosterico.todolist.routes.todos
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson()
        }
        basicAuth()

        initDb()

        login()
        todos()
    }.start(wait = true)
}
