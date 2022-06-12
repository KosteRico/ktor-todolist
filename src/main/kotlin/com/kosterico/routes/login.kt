package com.kosterico.routes

import com.kosterico.dao.UserDao
import com.kosterico.model.UserEntity
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.login() =
    routing {
        post("/register") {
            val user = call.receive<UserEntity>()
                .let { UserDao.create(it) }

            call.respond(user)
        }
    }
