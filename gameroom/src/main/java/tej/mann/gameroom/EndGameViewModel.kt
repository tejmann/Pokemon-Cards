package tej.mann.gameroom

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tej.mann.repository.PokemonRepository
import kotlin.coroutines.CoroutineContext

class EndGameViewModel(
    val auth: FirebaseAuth, private val database: FirebaseFirestore,
    private val pokemonRepository: PokemonRepository
) : ViewModel(),
    CoroutineScope {

    companion object{
        const val POKEMON_LIST = "pokemon_list"
    }

    fun fetchPokemon() {
        val name = auth.currentUser?.email ?: ""
        launch() {
            pokemonRepository.fetchPokemon()?.let { pokemon ->
                val doc = database.collection("users").document(name)
                database.runTransaction { transaction ->
                    transaction.update(doc, POKEMON_LIST, FieldValue.arrayUnion(pokemon))
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}
