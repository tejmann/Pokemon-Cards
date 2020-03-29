package tej.mann.repository

import android.net.wifi.hotspot2.pps.Credential
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignUpResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LoginRepository(val mAWSMobileClient: AWSMobileClient) {

    private var _signInResult = MutableLiveData<SignInResult>()
    fun signInResult(): LiveData<SignInResult> = _signInResult



    fun signIn(email: String, password: String) {
        mAWSMobileClient.signIn(email, password, null, object : Callback<SignInResult> {
            override fun onResult(result: SignInResult?) {
                Log.d("CALLED_RESULT", result?.signInState.toString())
                _signInResult.postValue(result)
            }

            override fun onError(e: Exception?) {
                Log.d("_CALLED_ERROR", e.toString())
                _signInResult.postValue(null)
            }
        })
    }

    fun signUP(email: String, password: String, attributes: Map<String, String>) {
        mAWSMobileClient.signUp(email, password, attributes, null, object : Callback<SignUpResult> {
            override fun onResult(result: SignUpResult?) {
                Log.d("_CALLED_SIGNUP_SUCC", result?.confirmationState.toString())

            }

            override fun onError(e: java.lang.Exception?) {
                Log.d("_CALLED_SIGNUP_ERROR", e.toString())
            }
        })
    }

    suspend fun credentials(): String {
        var result: String = ""
        withContext(Dispatchers.IO) {
            Log.d("_CALLED_CREDS","0")
            result = mAWSMobileClient.username
        }
        Log.d("_CALLED_CREDS","1")
        return result

    }

}