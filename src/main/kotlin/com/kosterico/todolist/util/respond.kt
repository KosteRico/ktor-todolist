package com.kosterico.todolist.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.respondEmpty(status: HttpStatusCode) =
    this.respond(status = status, Unit)