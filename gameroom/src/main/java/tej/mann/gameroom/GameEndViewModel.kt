package tej.mann.gameroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tej.mann.data.Pokemon
import tej.mann.data.Sprite
import tej.mann.repository.PokemonRepository
import kotlin.coroutines.CoroutineContext

class GameEndViewModel(
    auth: FirebaseAuth, private val database: FirebaseFirestore,
    private val pokemonRepository: PokemonRepository
) : ViewModel(),
    CoroutineScope {

    private val pokemon = MutableLiveData<Pokemon>()
    fun pokemon(): LiveData<Pokemon> = pokemon

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
