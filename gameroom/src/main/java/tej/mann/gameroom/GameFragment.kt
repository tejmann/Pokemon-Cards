package tej.mann.gameroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tej.mann.common.views.showToast
import tej.mann.data.Stat

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModel()

    private var backPressHandler: BackPressHandler? = null


    companion object {
        const val KEY_CREATOR = "creator"

        fun newInstance(creator: String): GameFragment = GameFragment().apply {
            arguments = Bundle().apply {
                putString(KEY_CREATOR, creator)
            }
        }
    }

    private var path: String = ""

    private fun setView(view: TextView, stat: Stat) {
        with(view) {
            val display = "${stat.stat.name}/${stat.baseStat}"
            text = display
            setOnClickListener {
                if (viewModel.myTurn) {
                    viewModel.setStat(path, stat)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        backPressHandler = activity as? BackPressHandler
        return inflater.inflate(R.layout.layout_game, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(KEY_CREATOR)?.let {
            path = it
            viewModel.listenToGame(it)
            viewModel.fetchPokemon()
        }

        viewModel.leftGame().observe(viewLifecycleOwner, Observer {
            showToast("$it left the game.")
            parentFragmentManager.beginTransaction().replace(R.id.container, RoomFragment()).commit()
        })
        viewModel.draw().observe(viewLifecycleOwner, Observer {
            if (it) viewModel.fetchPokemon()
        })

        viewModel.currentTurn().observe(viewLifecycleOwner, Observer {
            turn.text = it
        })
        viewModel.currentStat().observe(viewLifecycleOwner, Observer {
            player_name.text = it
        })
        viewModel.pokemon().observe(viewLifecycleOwner, Observer { pokemon ->
            pokemon_name.text = pokemon.name
            Glide.with(this).load(pokemon.sprites.frontDefault).into(imageView2)
            setView(stat1, pokemon.stats[0])
            setView(stat2, pokemon.stats[1])
            setView(stat3, pokemon.stats[2])
            setView(stat4, pokemon.stats[3])
        })

    }

    override fun onStart() {
        super.onStart()
        backPressHandler?.selectFragment(this)
    }

    override fun onDestroy() {
        viewModel.removeRegistration()
        viewModel.leaveGame()
        backPressHandler?.selectFragment(null)
        super.onDestroy()
    }
}

interface BackPressHandler{
    fun selectFragment(fragment: Fragment?)
}
