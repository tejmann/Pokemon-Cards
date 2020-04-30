package tej.mann.data

data class Room(
    val name: String = "",
    val status: Status = Status.EMPTY, val joiner: String? = null
) : Item(ItemType.ROOM)

enum class Status {
    EMPTY,
    IN_PLAY,
    DELETE
}
