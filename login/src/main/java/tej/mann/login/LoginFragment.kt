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
import tej.mann.login.di.SignupFragment
import kotlin.math.sign

class LoginFragment : Fragment() {

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
        computer_button.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.container, SignupFragment())
                .addToBackStack(null).commit()
        }
    }

    fun showToast(messgae: String) {
        Toast.makeText(context, messgae, Toast.LENGTH_SHORT).show()
    }


}