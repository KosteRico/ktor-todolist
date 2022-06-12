package com.kosterico

import com.fasterxml.jackson.databind.ObjectMapper
import com.kosterico.model.UserEntity
import com.kosterico.modules.basicAuth
import com.kosterico.util.hash
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*

val mapper: ObjectMapper = ObjectMapper()

val user: UserEntity = UserEntity(1, "user1", "123".hash())

fun ApplicationTestBuilder.customClient() = createClient {
    install(Auth) {
        basic {
            credentials {
                BasicAuthCredentials(user.username, user.password!!)
            }
        }
    }
}

fun ApplicationTestBuilder.configApp() = application {
    install(ContentNegotiation) {
        jackson()
    }
    basicAuth()
}