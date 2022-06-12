package com.kosterico.dto

data class UpdateTodoRequest(
    val text: String? = null,
    val done: Boolean? = null,
)