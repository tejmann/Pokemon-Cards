package tej.mann.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amazonaws.mobile.client.results.SignInResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest


class LoginRepository(val auth: FirebaseAuth) {

    private var _signInResult = MutableLiveData<SignInResult>()
    fun signInResult(): LiveData<SignInResult> = _signInResult
    private var _signUpResult = MutableLiveData<FirebaseUser>()
    fun signUpResult(): LiveData<FirebaseUser> = _signUpResult


    fun signUp(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _signUpResult.postValue(auth.currentUser)
                auth.currentUser?.sendEmailVerification()
            }
            else {
                Log.d("_CALLED_FAILED_SIGNUP", "${it.exception}")
                _signUpResult.postValue(null)
            }

        }
    }

    fun updateUserProfile(name: String){
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {
            if(it.isSuccessful) Log.d("_CALLED_USER_UPDATED", name)
        }
    }


}