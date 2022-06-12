package com.kosterico.todolist.routes

import com.kosterico.todolist.dao.TodoDao
import com.kosterico.todolist.dto.UpdateTodoRequest
import com.kosterico.todolist.model.TodoEntity
import com.kosterico.todolist.model.UserEntity
import com.kosterico.todolist.modules.AuthName
import com.kosterico.todolist.util.respondEmpty
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Application.todos() =
    routing {
        authenticate(AuthName.BASIC) {
            route("/todos") {
                getAll()
                create()
                route("/{id}") {
                    delete()
                    getOne()
                    modifyOne()
                }
            }
        }
    }

private fun Route.getAll() = get {
    val user = call.principal<UserEntity>()!!

    val todos = TodoDao.findByUser(user)

    call.respond(todos)
}

private fun Route.create() = post {
    val user = call.principal<UserEntity>()!!
    var todo = call.receive<TodoEntity>()

    todo = TodoDao.create(user, todo)

    call.respond(status = HttpStatusCode.Created, message = todo)
}

private fun Route.delete() = delete {
    val id = call.parameters.getOrFail("id").toLong()
    TodoDao
        .delete(id)
        ?.also {
            call.respond(
                status = HttpStatusCode.NoContent,
                it
            )
        } ?: call.respondEmpty(HttpStatusCode.NotFound)
}

private fun Route.getOne() = get {
    val id = call.parameters.getOrFail("id").toLong()
    TodoDao
        .findById(id)?.also {
            call.respond(it)
        } ?: call.respondEmpty(HttpStatusCode.NotFound)
}

private fun Route.modifyOne() = patch {
    val id = call.parameters.getOrFail("id").toLong()

    val request = call.receive<UpdateTodoRequest>()

    TodoDao
        .modify(id, request)
        ?.also {
            call.respond(it)
        } ?: call.respondEmpty(HttpStatusCode.NotFound)
}

