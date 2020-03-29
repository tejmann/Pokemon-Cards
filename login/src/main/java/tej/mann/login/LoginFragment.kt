package tej.mann.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.results.SignInState
import kotlinx.android.synthetic.main.login_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.sign

class LoginFragment : Fragment(){

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.login_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login_button.setOnClickListener {
            val email = login_email.editText?.text.toString()
            val password = login_password.editText?.text.toString()
            Log.d("_CALLED_ONCLICK_signin", "$email, $password")
            viewModel.fetchResult(email, password)
        }
        computer_button.setOnClickListener {
            val email = login_email.editText?.text.toString()
            val password = login_password.editText?.text.toString()
            Log.d("_CALLED_ONCLICK_signup", "$email, $password")
            viewModel.signUp(email, password)
        }
        viewModel.signInResult().observe(viewLifecycleOwner, Observer {signInResult ->
            if(signInResult == null) {showToast("unexpected error")}
            else {
                Log.d("_CALLED_USER", signInResult?.signInState.toString())
                when (signInResult.signInState) {
                    SignInState.DONE -> {parentFragmentManager.beginTransaction()
                        .replace(R.id.container, InviteFragment()).commit()

                    viewModel.fetchCredentials()}
                    SignInState.SMS_MFA -> showToast("SMS_MFA")
                    SignInState.NEW_PASSWORD_REQUIRED -> showToast("NEW_PASSWORD_REQUIRED")
                    else -> showToast("${signInResult.signInState}")
                }
            }

        })
        viewModel.userCredentials().observe(viewLifecycleOwner, Observer {
            showToast(it)
            Log.d("_CALLED_CREDNTIALS", it)
        })
    }

    fun showToast(messgae: String){
        Toast.makeText(context, messgae, Toast.LENGTH_SHORT).show()
    }



}