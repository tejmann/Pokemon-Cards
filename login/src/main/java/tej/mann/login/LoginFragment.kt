package tej.mann.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.login_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tej.mann.gameroom.RoomFragment
import tej.mann.login.di.SignupFragment

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.login_main, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupInputField()
    }

    private fun setupInputField() {
        login_email_edittext.addTextChangedListener(textWatcher)
        login_password_edittext.addTextChangedListener(textWatcher)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        computer_button.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.container, SignupFragment())
                .addToBackStack(null).commit()
        }
        main_login_button.updateOnClickListener {
            val email = login_email.editText?.text.toString()
            val password = login_password.editText?.text.toString()
            viewModel.signIn(email, password)
            main_login_button.buttonState = LoginButton.LoginButtonState.LOADING
        }

        viewModel.signInResult().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToast("Welcome ${it.displayName}")
                main_login_button.buttonState = LoginButton.LoginButtonState.DISABLED
                parentFragmentManager.beginTransaction().replace(R.id.container, RoomFragment())
                    .commit()
            } else {
                showToast("User not found.")
                main_login_button.buttonState = LoginButton.LoginButtonState.ENABLED
            }
        })
    }


    fun showToast(messgae: String) {
        Toast.makeText(context, messgae, Toast.LENGTH_SHORT).show()
    }

    private fun hasRequiredInfo(): Boolean {
        return !(login_email.editText?.text.isNullOrEmpty()
                || login_password.editText?.text.isNullOrEmpty())
    }

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                main_login_button.buttonState =
                    if (hasRequiredInfo()) LoginButton.LoginButtonState.ENABLED
                    else LoginButton.LoginButtonState.DISABLED
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        login_email_edittext.removeTextChangedListener(textWatcher)
        login_password_edittext.removeTextChangedListener(textWatcher)

        main_login_button.updateOnClickListener(null)
    }

}