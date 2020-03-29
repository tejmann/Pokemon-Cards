package tej.mann.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amazonaws.mobile.client.results.SignInResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tej.mann.repository.LoginRepository
import kotlin.coroutines.CoroutineContext

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel(), CoroutineScope {

    fun signInResult(): LiveData<SignInResult> = loginRepository.signInResult()
    fun fetchResult(email: String, password: String){
        loginRepository.signIn(email, password)
    }

    private var _userCredentials = MutableLiveData<String>()
    fun userCredentials(): LiveData<String> = _userCredentials

    fun signUp(email: String, password: String){
        val attributes: Map<String, String> = mapOf(Pair("email","tejpartapsinghmann@gmail.com"))
        loginRepository.signUP(email, password, attributes)
    }

    fun fetchCredentials(){
        Log.d("_CALLED_USER_CRED_FETCH", "NULL")
        launch {
            val t = loginRepository.credentials()
            if (t == "") {
                Log.d("_CALLED_USER_CREDs", "NULL")
            } else {
               _userCredentials.postValue(t)
            }
        }
    }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main


}