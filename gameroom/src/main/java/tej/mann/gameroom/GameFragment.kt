package tej.mann.gameroom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.layout_game.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment: Fragment() {

    val viewModel: RoomViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.layout_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.draw().observe(viewLifecycleOwner, Observer {
            if (it) viewModel.fetchPokemon()
        })
        viewModel.pokemon().observe(viewLifecycleOwner, Observer {pokemon ->
            Log.d("_CALLED_","fragment_observer")
            pokemon_name.text = pokemon.name
            stat1.text = pokemon.stats[0].stat.name
            stat2.text = pokemon.stats[1].stat.name
            stat3.text = pokemon.stats[2].stat.name
            stat4.text = pokemon.stats[3].stat.name
            stat1.setOnClickListener {
                it as TextView
                if (viewModel.turn()) viewModel.setOrCompare(pokemon.stats[0])
            }
        })

    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchPokemon()
        viewModel.listenToGame()
    }
}