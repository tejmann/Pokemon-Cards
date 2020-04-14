package tej.mann.gameroom

import tej.mann.data.Stat

data class Game(
    val player_1_name: String? = null,
    val player_2_name: String? = null,
    val turn: String? = null,
    val move: Move = Move.SET,
    val draw: Draw = Draw.NO,
    val curr_stat: Stat? = null,
    val old_stat: Stat? = null,
    val left_game: String? = null
)


enum class Move {
    SET,
    COMPARE,
    EVALUATE
}

enum class Draw {
    YES,
    NO
}
