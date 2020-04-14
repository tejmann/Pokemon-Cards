package tej.mann.gameroom

data class Room(val name: String = "", val status: Status = Status.EMPTY, val joiner: String? = null, val game_path: String? = null){
}

enum class Status{
    EMPTY,
    IN_PLAY
}