package tej.mann.gameroom

import tej.mann.data.Stat

data class Game(
    val creator: Player? = null,
    val joiner: Player? = null,
    val turn: String? = null,
    val move: Move = Move.SET,
    val draw: Draw = Draw.NO,
    val stat: Stat? = null,
    val player_1_score: Int = 0,
    val player_2_score: Int = 0
)

data class Player(val name: String = "", val number: Int = 0, val score: Int? = 0, val attr: Attr? = null)

enum class Move {
    SET,
    COMPARE
}

enum class Draw {
    YES,
    NO
}

enum class Attr {
    CREATOR,
    JOINER
}