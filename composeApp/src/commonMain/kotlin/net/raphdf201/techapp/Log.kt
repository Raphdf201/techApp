package net.raphdf201.techapp

expect fun log(message: String, tag: String)

fun networkLog(message: String) {
    log(message, "network")
}

fun serializationLog(message: String) {
    log(message, "serialization")
}

fun debugLog(message: String) {
    log(message, "debug")
}

fun exceptionLog(exception: Exception) {
    log(exception.message.toString(), "exceptions")
}
