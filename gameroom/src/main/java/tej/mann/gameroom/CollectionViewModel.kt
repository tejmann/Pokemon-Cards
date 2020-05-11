package tej.mann.gameroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import tej.mann.data.Path
import tej.mann.data.Path.COLLECTION_PATH_USERS
import tej.mann.data.Pokemon
import tej.mann.data.PokemonList
import tej.mann.data.Status

class CollectionViewModel(
    val auth: FirebaseAuth, private val database: FirebaseFirestore
) : ViewModel() {

    val email = auth.currentUser?.email

    companion object {
        const val FIELD_STATUS = "status"
    }

    private val pokemon = MutableLiveData<List<Pokemon>>()
    fun pokemon(): LiveData<List<Pokemon>> = pokemon

    fun getPokemon() {
        email?.let {
            database.collection(COLLECTION_PATH_USERS).document(it)
                .get()
                .addOnSuccessListener { doc ->
                    doc.toObject<PokemonList>()?.let { list ->
                        pokemon.postValue(list.pokemonList)
                    }
                }
        }
    }

    fun deleteAndSignOut() {
        email?.let {
            val doc = database.collection(Path.COLLECTION_PATH_ROOMS).document(it)
            database.runTransaction { transaction ->
                transaction.update(doc, FIELD_STATUS, Status.DELETE)
            }.addOnCompleteListener {
                auth.signOut()
            }
        }
    }

}
