package com.kosterico.todolist.model

data class TodoEntity(
    val id: Long = -1,
    val text: String,
    val done: Boolean
)
