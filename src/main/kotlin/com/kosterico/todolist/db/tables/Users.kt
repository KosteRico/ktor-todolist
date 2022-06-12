package com.kosterico.todolist.db.tables

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = long("id").autoIncrement()
    val username = varchar("name", 255).uniqueIndex()
    val password = varchar("password", 255)

    override val primaryKey = PrimaryKey(id)
}