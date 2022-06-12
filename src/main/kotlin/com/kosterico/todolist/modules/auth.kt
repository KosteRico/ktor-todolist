package com.kosterico.todolist.modules

import com.kosterico.todolist.dao.UserDao
import io.ktor.server.application.*
import io.ktor.server.auth.*

object AuthName {
    const val BASIC = "basic"
}

fun Application.basicAuth() = install(Authentication) {
    basic(AuthName.BASIC) {
        validate { credential ->
            UserDao.findByUsernamePassword(
                credential.name,
                credential.password
            )
        }
    }

}