package tej.mann.gameroom

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.login_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tej.mann.common.views.LoginButton
import tej.mann.common.views.showToast

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
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SignupFragment.newInstance(LoginViewModel.SIGN_UP))
                .addToBackStack(null).commit()
        }

        main_login_button.setOnClickListener {
            val email = login_email.editText?.text.toString()
            val password = login_password.editText?.text.toString()
            viewModel.signIn(email, password)
            main_login_button.buttonState = LoginButton.LoginButtonState.LOADING
        }

        forgot_password_button.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, SignupFragment.newInstance(LoginViewModel.FORGOT_PASSWORD))
                .addToBackStack(null).commit()
        }

        viewModel.toast().observe(viewLifecycleOwner, Observer {
            showToast(it)
            main_login_button.buttonState = LoginButton.LoginButtonState.ENABLED
        })

        viewModel.signInResult().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                showToast(getString(R.string.welcome, it.displayName))
                main_login_button.buttonState = LoginButton.LoginButtonState.DISABLED
                parentFragmentManager.beginTransaction().replace(R.id.container, RoomFragment())
                    .commit()
            }
        })
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

            @Suppress("EmptyFunctionBlock")
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @Suppress("EmptyFunctionBlock")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        login_email_edittext.removeTextChangedListener(textWatcher)
        login_password_edittext.removeTextChangedListener(textWatcher)

        main_login_button.setOnClickListener(null)
    }

}
