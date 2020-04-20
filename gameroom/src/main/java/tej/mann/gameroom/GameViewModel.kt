package tej.mann.gameroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tej.mann.data.Pokemon
import tej.mann.data.Sprite
import tej.mann.data.Stat
import tej.mann.repository.PokemonRepository
import kotlin.coroutines.CoroutineContext

class GameViewModel(
    auth: FirebaseAuth, private val database: FirebaseFirestore,
    private val pokemonRepository: PokemonRepository
) : ViewModel(),
    CoroutineScope {

    companion object {
        const val CURR_STAT = "curr_stat"
        const val DRAW = "draw"
        const val LEFT_GAME = "left_game"
        const val END_GAME = "end_game"
        const val WINNER = "winner"
        const val LOSER = "loser"
    }

    private val email: String? = auth.currentUser?.email
    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> = _pokemon
    private var game: String? = null

    var myTurn: Boolean = false

    private val _currentTurn: MutableLiveData<Boolean> = MutableLiveData()
    val currentTurn: LiveData<Boolean> = _currentTurn

    private val _leftGame: MutableLiveData<String> = MutableLiveData()
    val leftGame: LiveData<String> = _leftGame

    private val _draw: MutableLiveData<Boolean> = MutableLiveData()
    val draw: LiveData<Boolean> = _draw

    private val _currentStat: MutableLiveData<String> = MutableLiveData()
    val currentStat: LiveData<String> = _currentStat

    private val _myScore: MutableLiveData<String> = MutableLiveData()
    val myScore: LiveData<String> = _myScore

    private val _oppScore: MutableLiveData<String> = MutableLiveData()
    val oppScore: LiveData<String> = _oppScore

    private val _winner: MutableLiveData<String> = MutableLiveData()
    fun winner(): LiveData<String> = _winner



    private var gameRegistration: ListenerRegistration? = null


    fun setStat(gamePath: String, stat: Stat) {
        val doc = database.document(gamePath)
        doc.get().addOnSuccessListener {
            val game = it.toObject<Game>()
            val oldStat = game?.oldStat
            if (oldStat == null) {
                database.runTransaction { transaction ->
                    transaction.update(doc, Companion.CURR_STAT, stat, Companion.DRAW, Draw.NO)
                }
            }
            oldStat?.let { old ->
                if (old.stat.name == stat.stat.name) {
                    database.runTransaction { transaction ->
                        transaction.update(doc, Companion.CURR_STAT, stat, Companion.DRAW, Draw.NO)
                    }
                }
            }
        }
    }

    fun listenToGame(gamePath: String) {
        val doc = database.document(gamePath)
        game = gamePath
        gameRegistration = doc.addSnapshotListener { snapshot, e ->
            e?.let {
                return@addSnapshotListener
            }
            snapshot?.let { s ->
                val game = s.toObject<Game>()
                val turn = game?.turn
                myTurn = turn == email
                _currentTurn.postValue(myTurn)

                val stat = game?.oldStat
                val baseStat = stat?.baseStat
                val statName = stat?.stat?.name
                val show = if (statName == null) "" else "$statName = $baseStat"
                _currentStat.postValue(show)

                val creator = game?.creator
                val creatorScore = game?.creatorScore
                val joinerScore = game?.joinerScore

                setScore(creator, creatorScore, joinerScore)

                val draw = game?.draw
                setDraw(draw)

                val winner = game?.winner
                setWinner(winner)
                val leftGame = game?.leftGame
                hasLeft(leftGame)
            }
        }
    }

    private fun setScore(creator: String?, creatorScore: Long?, joinerScore: Long?){
        if (creator == email) {
            _myScore.postValue(creatorScore.toString())
            _oppScore.postValue(joinerScore.toString())
        }
        else {
            _myScore.postValue(joinerScore.toString())
            _oppScore.postValue(creatorScore.toString())
        }
    }
     private fun setDraw(draw: Draw?) {
         _draw.postValue(draw == Draw.YES)
     }

    private fun hasLeft(leftGame: String?){
        leftGame?.let {
            _leftGame.postValue(it)
        }
    }

    private fun setWinner(winner: String?) {
        winner?.let{
            if (it == email) _winner.postValue(WINNER) else _winner.postValue(LOSER)
        }
    }


    fun removeRegistration() = gameRegistration?.remove()

    fun leaveGame(){
        val doc = game?.let { database.document(it) }
        _winner.value?.let {
            database.runTransaction {transaction ->
                if (doc != null){
                    transaction.update(doc, END_GAME, true)
                }
            }
            return
        }

        database.runTransaction { transaction ->
            if (doc != null) {
                transaction.update(doc, LEFT_GAME, email)
            }
        }
    }

    fun fetchPokemon() {
        launch() {
            val result = pokemonRepository.fetchPokemon()
            if (result != null) _pokemon.postValue(result) else _pokemon.postValue(
                Pokemon(
                    "ERROR",
                    listOf(),
                    Sprite("error")
                )
            )
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}
