package tej.mann.gameroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_welcome.*
import org.koin.android.ext.android.inject
import tej.mann.common.views.showToast


class WelcomeFragment : Fragment() {

    val auth: FirebaseAuth by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.layout_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_play_online.setOnClickListener {
            if (auth.currentUser == null) parentFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    LoginFragment()
                ).addToBackStack(null).commit()
            else parentFragmentManager.beginTransaction().replace(R.id.container, RoomFragment())
                .addToBackStack(null).commit()

        }

        button_play_computer.setOnClickListener {
            showToast(getString(R.string.play_computer_feature_soon))
        }
    }
}
