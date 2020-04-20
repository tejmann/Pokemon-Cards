package tej.mann.gameroom

import com.google.firebase.firestore.PropertyName
import tej.mann.data.Stat

data class Game(
    val creator: String? = null,
    val joiner: String? = null,
    val turn: String? = null,
    val move: Move = Move.SET,
    val draw: Draw = Draw.NO,
    @get:PropertyName("curr_stat") @set:PropertyName("curr_stat") var currStat: Stat? = null,
    @get:PropertyName("old_stat") @set:PropertyName("old_stat") var oldStat: Stat? = null,
    @get:PropertyName("left_game") @set:PropertyName("left_game") var leftGame: String? = null,
    @get:PropertyName("creator_score") @set:PropertyName("creator_score") var creatorScore: Long = 0,
    @get:PropertyName("joiner_score") @set:PropertyName("joiner_score") var joinerScore: Long = 0,
    val winner: String? = null
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
