package com.kosterico.model

import io.ktor.server.auth.*
import java.io.Serializable

data class UserEntity(
    val id: Long,
    val username: String,
    var password: String? = null
) : Principal, Serializable