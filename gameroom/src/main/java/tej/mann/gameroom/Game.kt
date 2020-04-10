package tej.mann.gameroom

import tej.mann.data.Stat

data class Game(
    val player_1_name: String? = null,
    val player_2_name: String? = null,
    val turn: String? = null,
    val move: Move = Move.SET,
    val draw: Draw = Draw.NO,
    val stat: Stat? = null,
    val player_1_score: Int = 0,
    val player_2_score: Int = 0
)


enum class Move {
    SET,
    COMPARE
}

enum class Draw {
    YES,
    NO
}
