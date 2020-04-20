package tej.mann.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tej.mann.data.PokemonList
import tej.mann.repository.PokemonRepository
import kotlin.coroutines.CoroutineContext

class LoginViewModel(
    val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val pokemonRepository: PokemonRepository
) : ViewModel(),
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object{
        const val FORGOT_PASSWORD = "forgot_password"
        const val SIGN_UP = "sign_up"
    }

    private var _signInResult = MutableLiveData<FirebaseUser>()
    fun signInResult(): LiveData<FirebaseUser> = _signInResult

    private var _signUpResult = MutableLiveData<FirebaseUser>()
    fun signUpResult(): LiveData<FirebaseUser> = _signUpResult

    private var _toast = MutableLiveData<String>()
    fun toast(): LiveData<String> = _toast

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _signUpResult.postValue(auth.currentUser)
                fetchPokemon(email)

            } else {
                Log.d("_CALLED_FAILED_SIGNUP", "${it.exception}")
                _toast.postValue(it.exception?.message)
            }
        }
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _signInResult.postValue(auth.currentUser)
            } else {
                _toast.postValue(it.exception?.message)
            }
        }
    }

    private fun fetchPokemon(name: String) {
        launch() {
            pokemonRepository.fetchPokemon()?.let {
                val list = PokemonList(listOf(it))
                database.collection("users").document(name).set(list)
            }
        }
    }

    fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                _toast.postValue("EMAIL SENT YO")
            }
            else {
                _toast.postValue(it.exception?.message)
            }
        }
    }
}
