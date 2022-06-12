package com.kosterico.todolist.util

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashKey = hex("68656c6c6f66")

private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun String.hash(): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(this.toByteArray(Charsets.UTF_8)))
}