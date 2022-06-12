package com.kosterico.todolist.db.tables

import org.jetbrains.exposed.sql.Table

object Todos : Table() {
    val id = long("id").autoIncrement()
    val userId = long("user_id").references(Users.id)
    val text = text("text")
    val done = bool("done")

    override val primaryKey = PrimaryKey(id)
}
