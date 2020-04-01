package tej.mann.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import tej.mann.repository.LoginRepository
import kotlin.coroutines.CoroutineContext

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel(), CoroutineScope {

    fun signInResult(): LiveData<FirebaseUser> = loginRepository.signInResult()
    fun signUpResult(): LiveData<FirebaseUser> = loginRepository.signUpResult()

    fun signUp(email: String, password: String) {
        loginRepository.signUp(email, password)
    }

    fun signIn(email: String, password: String) {
        loginRepository.signIn(email, password)
    }

    fun updateProfile(name: String) {
        loginRepository.updateUserProfile(name)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}