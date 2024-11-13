package net.raphdf201.techapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform