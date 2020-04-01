package tej.mann.login.di

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_signup.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tej.mann.login.InviteFragment
import tej.mann.login.LoginButton
import tej.mann.login.LoginViewModel
import tej.mann.login.R

class SignupFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()

    override fun onStart() {
        super.onStart()
        setupInputField()
    }

    private fun setupInputField() {
        signup_email_edittext.addTextChangedListener(textWatcher)
        signup_confirm_password_edit_text.addTextChangedListener(textWatcher)
        signup_password_edit_text.addTextChangedListener(textWatcher)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.layout_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signup_form_button.updateOnClickListener {
            val email = signup_email.editText?.text.toString()
            val password = signup_password.editText?.text.toString()
            Log.d("_CALLED_ONCLICK_signup", "$email, $password")
            viewModel.signUp(email, password)
            signup_form_button.buttonState = LoginButton.LoginButtonState.LOADING
        }

        viewModel.signUpResult().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                showToast("Verification Email sent!")
                signup_form_button.buttonState = LoginButton.LoginButtonState.DISABLED
                parentFragmentManager.beginTransaction().replace(R.id.container, InviteFragment())
                    .commit()
            } else {
                showToast("Please check you email and try again")
                signup_form_button.buttonState = LoginButton.LoginButtonState.ENABLED
            }
        })
    }

    private fun showToast(messgae: String) {
        Toast.makeText(context, messgae, Toast.LENGTH_SHORT).show()
    }

    private fun hasRequiredInfo(): Boolean {
        return !(signup_email.editText?.text.isNullOrEmpty()
                || signup_confirm_password.editText?.text.isNullOrEmpty()
                || signup_password.editText?.text.isNullOrEmpty()) && correctPassword()
    }

    private fun correctPassword(): Boolean {
        Log.d(
            "_CALLED_EDIT",
            "${signup_confirm_password.editText?.text} vs ${signup_password.editText?.text}"
        )
        return signup_confirm_password.editText?.text.toString() == signup_password.editText?.text.toString()
    }

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                signup_form_button.buttonState =
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
        signup_email_edittext.removeTextChangedListener(textWatcher)
        signup_confirm_password_edit_text.removeTextChangedListener(textWatcher)
        signup_password_edit_text.removeTextChangedListener(textWatcher)

        signup_form_button.updateOnClickListener(null)
    }
}