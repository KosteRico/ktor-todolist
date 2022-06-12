package com.kosterico.dao

import com.kosterico.db.tables.Users
import com.kosterico.model.UserEntity
import com.kosterico.modules.query
import com.kosterico.util.hash
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

object UserDao {
    suspend fun findByUsernamePassword(
        username: String,
        password: String,
    ): UserEntity? = query {
        Users.select {
            (Users.username eq username) and
                    (Users.password eq password.hash())
        }.singleOrNull()?.toDto()
    }

    suspend fun create(user: UserEntity): UserEntity = query {
        (Users.insert {
            it[username] = user.username
            it[password] = user.password!!.hash()
        } get Users.id).let {
            Users.select { Users.id eq it }
                .single().toDto()
        }
    }

    private fun ResultRow.toDto(): UserEntity = UserEntity(
        this[Users.id],
        this[Users.username]
    )
}