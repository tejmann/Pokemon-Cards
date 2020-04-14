package tej.mann.gameroom

import android.util.Log
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

    private val email: String? = auth.currentUser?.email
    private val pokemon = MutableLiveData<Pokemon>()
    private var game: String? = null

    var myTurn: Boolean = false

    private val _currentTurn: MutableLiveData<String> = MutableLiveData()
    fun currentTurn(): LiveData<String> = _currentTurn

    private val _leftGame: MutableLiveData<String> = MutableLiveData()
    fun leftGame(): LiveData<String> = _leftGame

    private val _draw: MutableLiveData<Boolean> = MutableLiveData()
    fun draw(): LiveData<Boolean> = _draw

    private val _currentStat: MutableLiveData<String> = MutableLiveData()
    fun currentStat(): LiveData<String> = _currentStat



    fun pokemon(): LiveData<Pokemon> = pokemon

    private var gameRegistration: ListenerRegistration? = null


    fun setStat(gamePath: String, stat: Stat) {
        val doc = database.document(gamePath)
        doc.get().addOnSuccessListener {
            val game = it.toObject<Game>()
            val oldStat = game?.old_stat
            if (oldStat == null) {
                database.runTransaction { transaction ->
                    transaction.update(doc, "curr_stat", stat, "draw", Draw.NO)
                }
            }
            oldStat?.let { old ->
                if (old.stat.name == stat.stat.name ) {
                    database.runTransaction { transaction ->
                        transaction.update(doc, "curr_stat", stat, "draw", Draw.NO)
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
                Log.d("_CALLED_", e.toString())
                return@addSnapshotListener
            }
            snapshot?.let { s ->
                val game = s.toObject<Game>()
                val turn = game?.turn
                myTurn = turn == email
                val current = if (myTurn) "YOUR TURN"
                else {
                    "OPPONENTS TURN"
                }
                _currentTurn.postValue(current)

                _currentStat.postValue(game?.old_stat?.stat?.name)

                val draw = game?.draw
                _draw.postValue(draw == Draw.YES)

                val leftGame = game?.left_game
                leftGame?.let {
                    _leftGame.postValue(it)
                }
            }
        }
    }

    fun removeRegistration() = gameRegistration?.remove()

    fun leaveGame() {
        val doc = game?.let { database.document(it) }
        database.runTransaction { transaction ->
            if (doc != null) {
                transaction.update(doc, "left_game",email)
            }
        }
    }


    fun fetchPokemon() {
        launch() {
            val result = pokemonRepository.fetchPokemon()
            if (result != null) pokemon.postValue(result) else pokemon.postValue(
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