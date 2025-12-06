package bd.srabbijan.lenden

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform