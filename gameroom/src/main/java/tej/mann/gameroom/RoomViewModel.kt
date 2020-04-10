package tej.mann.gameroom

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tej.mann.data.Pokemon
import tej.mann.data.Stat
import tej.mann.repository.PokemonRepository
import kotlin.coroutines.CoroutineContext

class RoomViewModel(
    private val roomRepository: RoomRepository,
    private val pokemonRepository: PokemonRepository
) : ViewModel(),
    CoroutineScope {
    val ref: String = ""
    fun joined() = roomRepository.joined()
    fun created() = roomRepository.created()
    fun emptyRooms() = roomRepository.emptyRooms()
    fun turn() = roomRepository.myTurn


    private val pokemon = MutableLiveData<Pokemon>()
    fun pokemon(): LiveData<Pokemon> = pokemon


    fun listenToGame(){
        roomRepository.listenToGame()
    }

    fun createRoom() {
        roomRepository.createRoom()
    }

    fun createGame(creater: String) {
        roomRepository.createGame(creater)
    }

    fun draw() = roomRepository.draw()


    fun checkAvailableRooms() = roomRepository.checkAvailableRooms()

    fun fetchPokemon() {
        Log.d("_CALLED_", "fetch_viewmodel")
        launch() {
            val result = pokemonRepository.fetchPokemon()
            if (result != null) pokemon.postValue(result) else pokemon.postValue(
                Pokemon(
                    "ERROR",
                    listOf()
                )
            )
        }
    }

    fun setOrCompare(stat: Stat){
        roomRepository.setOrCompare(stat)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}