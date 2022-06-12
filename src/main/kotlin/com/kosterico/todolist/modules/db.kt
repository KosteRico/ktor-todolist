package com.kosterico.todolist.modules

import com.kosterico.todolist.db.tables.Todos
import com.kosterico.todolist.db.tables.Users
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionManager
import java.sql.Connection

private lateinit var db: Database

fun Application.initDb() {
    val config = HoconApplicationConfig(ConfigFactory.load())



    db = Database.connect(
        url = config.property("ktor.database.url").getString(),
        driver = config.property("ktor.database.driver").getString(),
        user = config.property("ktor.database.user").getString(),
        password = config.property("ktor.database.password").getString( ),
    )

    transaction(db) {
        SchemaUtils.create(Users, Todos)
    }
    log.info("Database migrations successfully done")
}

suspend fun <T> query(block: () -> T): T =
    withContext(Dispatchers.IO) {
        transaction(
            transactionIsolation = Connection.TRANSACTION_READ_COMMITTED,
            db.transactionManager.defaultRepetitionAttempts,
            db = db
        ) { block() }
    }
