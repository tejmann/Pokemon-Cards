package tej.mann.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.results.SignInResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tej.mann.repository.LoginRepository
import kotlin.coroutines.CoroutineContext

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel(), CoroutineScope {

    enum class ButtonState{
        LOADING,
        ENABLED,
        DISABLED
    }

    fun signInResult(): LiveData<SignInResult> = loginRepository.signInResult()
    fun signUpResult(): LiveData<FirebaseUser> = loginRepository.signUpResult()

    private var _signUpButtonState = MutableLiveData<ButtonState>()
    fun signUpButtonState(): LiveData<ButtonState> = _signUpButtonState

    fun updateSignupState(buttonState: ButtonState) {
        _signUpButtonState.postValue(buttonState)
    }

    fun signUp(email: String, password: String){
        loginRepository.signUp(email, password)
    }

    fun updateProfile(name: String) {
        loginRepository.updateUserProfile(name)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}