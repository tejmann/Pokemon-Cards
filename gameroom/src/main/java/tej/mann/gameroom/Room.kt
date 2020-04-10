package tej.mann.gameroom

data class Room(val name: String = "", val status: Status = Status.EMPTY){
}

enum class Status{
    EMPTY,
    IN_PLAY
}