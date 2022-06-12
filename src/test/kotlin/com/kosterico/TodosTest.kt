package com.kosterico

import com.kosterico.dao.TodoDao
import com.kosterico.dao.UserDao
import com.kosterico.model.TodoEntity
import com.kosterico.routes.todos
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockkObject
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class TodosTest {

    @Before
    fun initMock() {
        mockkObject(TodoDao)
        mockkObject(UserDao)

        coEvery {
            UserDao.findByUsernamePassword(any(), any())
        } returns user
    }

    @Test
    fun testTodos() = testApplication {
        configApp()
        application {
            todos()
        }

        val client = customClient()

        val todos = listOf(TodoEntity(1, "text", false))

        coEvery {
            TodoDao.findByUser(eq(user))
        } returns todos

        client.get("/todos").apply {
            assertEquals(HttpStatusCode.OK, status)

            assertEquals(
                mapper.writeValueAsString(todos),
                bodyAsText()
            )
        }
    }

    @Test
    fun testTodosId() = testApplication {
        configApp()
        application {
            todos()
        }

        val client = customClient()

        val todo = TodoEntity(1, "text", false)

        coEvery {
            TodoDao.findById(eq(todo.id))
        } returns todo

        client.get("/todos/${todo.id}").apply {
            assertEquals(HttpStatusCode.OK, status)

            assertEquals(
                mapper.writeValueAsString(todo),
                bodyAsText()
            )
        }
    }

    @Test
    fun testCreateTodos() = testApplication {
        configApp()
        application {
            todos()
        }

        val client = customClient()

        val todo = TodoEntity(text = "text", done = false)

        coEvery {
            TodoDao.create(eq(user), eq(todo))
        } returns todo

        client.post("/todos") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(mapper.writeValueAsString(todo))
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
        }
    }
}
