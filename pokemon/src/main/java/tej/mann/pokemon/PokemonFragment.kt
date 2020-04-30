package tej.mann.pokemon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.pokemon_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokemonFragment: Fragment(){
    private val viewModel: PokemonViewModel by viewModel()
    companion object{
        const val S = 3
    }

    override fun onStart() {
        super.onStart()
        Log.d("_CALLED_","statr_fragment")
        viewModel.fetchPokemon()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("_CALLED_","create_view")
        return inflater.inflate(R.layout.pokemon_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pokemon().observe(viewLifecycleOwner, Observer {pokemon ->
            Log.d("_CALLED_","fragment_observer")
            pokemon_name.text = pokemon.name
            stat1.text = pokemon.stats[0].stat.name
            stat2.text = pokemon.stats[1].stat.name
            stat3.text = pokemon.stats[2].stat.name
            stat4.text = pokemon.stats[S].stat.name
        })
    }
}
