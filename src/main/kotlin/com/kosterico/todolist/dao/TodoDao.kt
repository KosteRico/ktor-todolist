package com.kosterico.todolist.dao

import com.kosterico.todolist.db.tables.Todos
import com.kosterico.todolist.dto.UpdateTodoRequest
import com.kosterico.todolist.model.TodoEntity
import com.kosterico.todolist.model.UserEntity
import com.kosterico.todolist.modules.query
import org.jetbrains.exposed.sql.*

object TodoDao {
    suspend fun findByUser(user: UserEntity): List<TodoEntity> = query {
        Todos.select { Todos.userId eq user.id }
            .map { it.toEntity() }
    }

    suspend fun findById(id: Long): TodoEntity? = query {
        Todos.select { Todos.id eq id }.singleOrNull()
            ?.toEntity()
    }

    suspend fun create(user: UserEntity, todo: TodoEntity): TodoEntity = query {
        val todoId = Todos.insert {
            it[userId] = user.id
            it[text] = todo.text
            it[done] = false
        } get Todos.id
        Todos.select { Todos.id eq todoId }
            .single().toEntity()
    }

    suspend fun delete(id: Long): TodoEntity? = query {
        Todos.select { Todos.id eq id }
            .singleOrNull()
            ?.toEntity()
            .also {
                Todos.deleteWhere { Todos.id eq id }
            }
    }

    suspend fun modify(id: Long, todo: UpdateTodoRequest): TodoEntity? = query {
        Todos.update({ Todos.id eq id }) {
            if (todo.text != null) {
                it[text] = todo.text
            }
            if (todo.done != null) {
                it[done] = done
            }
        }
        Todos.select { Todos.id eq id }
            .singleOrNull()?.toEntity()
    }

    private fun ResultRow.toEntity() = TodoEntity(
        this[Todos.id],
        this[Todos.text],
        this[Todos.done]
    )
}