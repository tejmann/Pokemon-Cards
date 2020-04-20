package tej.mann.gameroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.layout_win.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tej.mann.common.views.showToast

class GameEndFragment : Fragment() {


    private val viewModel: EndGameViewModel by viewModel()

    companion object {
        const val KEY_ANSWER = "answer"
        fun newInstance(winner: String): GameEndFragment = GameEndFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_ANSWER, winner)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.layout_win, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(KEY_ANSWER)?.let {
            return_room.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.container, RoomFragment())
                    .commit()
            }
            if (it == GameViewModel.WINNER) {
                viewModel.fetchPokemon()
                showToast(getString(R.string.pokemon_added), Toast.LENGTH_LONG)
                won_game.visibility = View.VISIBLE
            } else {
                lost_game.visibility = View.VISIBLE
            }
        }
    }
}
