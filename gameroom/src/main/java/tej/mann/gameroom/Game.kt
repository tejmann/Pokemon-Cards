package tej.mann.gameroom

import tej.mann.data.Stat

data class Game(
    val creator: String? = null,
    val joiner: String? = null,
    val turn: String? = null,
    val move: Move = Move.SET,
    val draw: Draw = Draw.NO,
    val curr_stat: Stat? = null,
    val old_stat: Stat? = null,
    val left_game: String? = null,
    val creator_score: Long = 0,
    val joiner_score: Long = 0
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
